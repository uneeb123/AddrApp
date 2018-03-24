package com.addrapp;

import org.bitcoinj.params.MainNetParams;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Map;
import java.util.HashMap;

public class BitcoinWalletModule extends ReactContextBaseJavaModule {

  private ReactApplicationContext context;
  private BitcoinWalletAppKitInterface bw;

  private String firstAddress;

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
  public void initiateWallet(Promise promise) {
    try {
      bw.downloadBlockChain(false);
      firstAddress = bw.newAddress();
      WritableMap map = Arguments.createMap();
      map.putString("address", firstAddress);
      promise.resolve(map);
    } catch (Exception e) {
      promise.reject(e);
    }
  }

  @ReactMethod 
  public void getAddress(Promise promise) {
    if (firstAddress != null) {
      WritableMap map = Arguments.createMap();
      map.putString("address", firstAddress);
      promise.resolve(map);
    } else {
      promise.reject("ERROR", "No address found");
    }
  }

  @ReactMethod
  public void getBalance(Promise promise) {
    try {
      WritableMap map = Arguments.createMap();
      map.putString("balance", bw.getBalance());
      promise.resolve(map);
    } catch (Exception e) {
      promise.reject("ERROR", "No address found");
    }
  }
}
