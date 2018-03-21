package com.addrapp;

import org.bitcoinj.params.MainNetParams;

import android.widget.Toast;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Map;
import java.util.HashMap;

public class BitcoinWalletModule extends ReactContextBaseJavaModule {

  public BitcoinWalletModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "BitcoinWallet";
  }

  @ReactMethod
  public void port() {
    Toast.makeText(getReactApplicationContext(),
        "Using Port: " + MainNetParams.get().getPort(),
        Toast.LENGTH_SHORT).show();
  }
}
