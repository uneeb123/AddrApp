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

  public BitcoinWalletModule(ReactApplicationContext reactContext) {
    super(reactContext);
    context = reactContext;
  }

  @Override
  public String getName() {
    return "BitcoinWallet";
  }

  @ReactMethod
  public void port(Promise promise) {
    try {
      int port = MainNetParams.get().getPort();

      BitcoinWallet walletInterface = new BitcoinWallet(context);

      WritableMap map = Arguments.createMap();
      map.putInt("port", port);
      promise.resolve(map);
    } catch (Exception e) {
      promise.reject("ERROR!", e);
    }
  }
}
