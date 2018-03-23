package com.addrapp;

import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.core.Address;

import java.util.List;

import java.io.File;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;

public class BitcoinWalletAppKitInterface {
        
  private static final String FILEPREFIX = "addr-wallet-test";
  private static final boolean TESTMODE = true;

  private WalletAppKit kit;
  private NetworkParameters params;
  private ReactApplicationContext reactContext;

  BitcoinWalletAppKitInterface(ReactApplicationContext _reactContext) {
    reactContext = _reactContext;
    if (TESTMODE) {
      params = TestNet3Params.get();
    }
    kit = new WalletAppKit(params, new File("."), FILEPREFIX);
  }

  public boolean downloadBlockChain(boolean blocking) {
    kit.startAsync();
    debug("Blockchain sync initiated");
    if (blocking) {
      kit.awaitRunning();
      debug("Blockchain sync completed");
      return true;
    }
    return false;
  }

  public List<Address> getAddresses() {
    return kit.wallet().getWatchedAddresses();
  }
  
  public String newAddress() {
    Address addr = kit.wallet().freshReceiveAddress();
    kit.wallet().addWatchedAddress(addr);
    debug("New address " + addr.toString() + " allocated");
    return addr.toString();
  }

  public String getBalance() {
    return kit.wallet().getBalance().toFriendlyString();
  }
  
  public String addrToStr(Address addr) {
    return addr.toString();
  }

  private void debug(String statement) {
    if (TESTMODE) {
      Toast.makeText(reactContext, statement, Toast.LENGTH_LONG).show();
    }
  }
}
