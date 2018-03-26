package com.addrapp;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.Wallet;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeArray;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class BitcoinWalletModule extends ReactContextBaseJavaModule {

  private ReactApplicationContext context;
  private BitcoinWalletAppKitInterface bw;
  private Address addr;

  public BitcoinWalletModule(ReactApplicationContext reactContext) {
    super(reactContext);
    context = reactContext;
    bw = new BitcoinWalletAppKitInterface(reactContext);
  }

  @Override
  public String getName() {
    return "BitcoinWallet";
  }

  @ReactMethod
  public void initiateWallet() {
    boolean blockingOperation = false;
    bw.downloadBlockChain(blockingOperation);
  }

  @ReactMethod
  public void getWallet(Promise promise) {
    try {
      if (addr == null) {
        if (bw.getWallet().getWatchedAddresses().isEmpty()) {
          addr = bw.getWallet().freshReceiveAddress();
          bw.getWallet().addWatchedAddress(addr);
        }
        else {
          addr = bw.getWallet().getWatchedAddresses().get(0);
        }
      }
      WritableMap map = Arguments.createMap();
      int waitCount = 0;
      while (!bw.getWallet().isConsistent()) {
        // wait for a second
        TimeUnit.SECONDS.sleep(1);
        waitCount++;
        // break
        if (waitCount > 5) throw new Exception();
      }
      String balance = bw.getBalance();
      map.putString("balance", balance);
      map.putString("address", addr.toString());
      promise.resolve(map);
    } catch (Exception e) {
      promise.reject("ERROR","Wallet not ready");
    }
  }

  private boolean receivingTransaction(Transaction tx, Wallet wallet) {
    List<TransactionOutput> outputs = tx.getOutputs();
    Iterator<TransactionOutput> iterOutputs = outputs.iterator();
    // if any of the outputs is addressed to self
    while(iterOutputs.hasNext()){
      TransactionOutput output = iterOutputs.next();
      if (output.isMine(wallet)) {
        return true;
      }
    }
    return false;
  }

  @ReactMethod
  public void getTransactions(Promise promise) {
    try {
      WritableMap map = Arguments.createMap();
      WritableStringArray amountsJS = new WritableStringArray();
      WritableStringArray addressJS = new WritableStringArray();
      WritableStringArray dateJS = new WritableStringArray();

      Wallet wallet = bw.getWallet();

      Set<Transaction> transactions = wallet.getTransactions(false);
      NetworkParameters params = wallet.getNetworkParameters();

      Iterator<Transaction> iterator = transactions.iterator();
      while (iterator.hasNext()) {
        Transaction tx = iterator.next();
        boolean received = false;
        if (receivingTransaction(tx, wallet)) {
          received = true;
        }
        List<TransactionOutput> outputs = tx.getOutputs();
        Iterator<TransactionOutput> iterOutputs = outputs.iterator();
        while (iterOutputs.hasNext()) {
          TransactionOutput output = iterOutputs.next();
          if (received) {
            if (output.isMine(wallet)) {
              amountsJS.pushString(output.getValue().toFriendlyString());
              addressJS.pushString(output.getAddressFromP2PKHScript(params).toString());
              dateJS.pushString(tx.getUpdateTime().toString());
            }
          } else {
            amountsJS.pushString("-" + output.getValue().toFriendlyString());
            addressJS.pushString(output.getAddressFromP2PKHScript(params).toString());
            dateJS.pushString(tx.getUpdateTime().toString());
          }
        }
      }
      map.putArray("amounts", amountsJS);
      map.putArray("addresses", addressJS);
      map.putArray("dates", dateJS);
      promise.resolve(map);
    } catch (Exception e) {
      promise.reject("ERROR","Unable to retreive transactions");
    }
  }
}
