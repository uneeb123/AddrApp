package com.addrapp;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.wallet.Wallet;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
          List<Address> all_addresses = bw.getWallet().getWatchedAddresses();
          addr = all_addresses.get(0);
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
  public void createTransaction(String amount, String to, Promise promise) {
    try {
      Coin value = Coin.parseCoin(amount);
      Wallet wallet = bw.getWallet();
      Address toAddr = Address.fromBase58(wallet.getParams(), to);
      WritableMap map = Arguments.createMap();
      Wallet.SendResult result = wallet.sendCoins(bw.getKit().peerGroup(), toAddr, value);
      map.putString("hash", result.tx.getHashAsString());
      promise.resolve(map);
    } catch (Exception e) {
      promise.reject(e);
    }
  }

  @ReactMethod
  public void getTransactions(Promise promise) {
    try {
      WritableMap map = Arguments.createMap();
      WritableArray amountsJS = Arguments.createArray();
      WritableArray addressJS = Arguments.createArray();
      WritableArray dateJS = Arguments.createArray();

      Wallet wallet = bw.getWallet();

      List<Transaction> transactions = wallet.getTransactionsByTime();
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
          // TODO: only display relevant outputs
          amountsJS.pushString(output.getValue().toFriendlyString());
          String addrStr;
          try {
            addrStr = output.getAddressFromP2PKHScript(params).toString();
          } catch (NullPointerException e) {
            addrStr = "Unknown";
          }
          addressJS.pushString(addrStr);
          dateJS.pushString(tx.getUpdateTime().toString());
        }
      }
      if (amountsJS.size() == 0) {
        throw new Exception();
      }
      if ((amountsJS.size() != addressJS.size()) && (addressJS.size() != dateJS.size())) {
        throw new Exception();
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
