package com.addrapp;

import org.bitcoinj.core.Address;
import org.bitcoinj.params.MainNetParams;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.security.spec.ECField;
import java.util.Map;
import java.util.HashMap;
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
        addr = bw.getWallet().freshReceiveAddress();
        bw.getWallet().addWatchedAddress(addr);
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
}
