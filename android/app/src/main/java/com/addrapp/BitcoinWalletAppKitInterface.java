package com.addrapp;

import android.support.annotation.Nullable;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.core.listeners.TransactionConfidenceEventListener;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.bitcoinj.wallet.listeners.WalletCoinsSentEventListener;

import java.io.File;
import java.util.Date;
import java.util.List;

public class BitcoinWalletAppKitInterface {

  private static final String FILEPREFIX = "addr-wallet-test";
  private static final boolean TESTMODE = true;

  private AddrWalletKit kit;
  private NetworkParameters params;
  private ReactApplicationContext reactContext;
  private AddrDownloadListener downloadListener = new AddrDownloadListener();
  private AddrCoinsReceivedListener receivedListener = new AddrCoinsReceivedListener();
  private AddrCoinsSentListener sentListener = new AddrCoinsSentListener();
  private AddrTransactionConfidenceListener transactionListener = new AddrTransactionConfidenceListener();

  BitcoinWalletAppKitInterface(ReactApplicationContext _reactContext) {
    reactContext = _reactContext;
    if (TESTMODE) {
      params = TestNet3Params.get();
    }

    File dir = reactContext.getFilesDir().getParentFile();
    kit = new AddrWalletKit(params, dir, FILEPREFIX);
  }

  public Wallet getWallet() {
    return kit.wallet();
  }

  public WalletAppKit getKit() {
    return kit;
  }

  public boolean downloadBlockChain(boolean blocking) {
    kit.setDownloadListener(downloadListener);
    kit.setBlockingStartup(blocking);
    kit.startAsync();
    debug("Blockchain sync initiated");
    if (blocking) {
      kit.awaitRunning();
      debug("Blockchain sync completed");
      return true;
    }
    return false;
  }

  private void sendEvent(ReactContext reactContext,
                         String eventName,
                         @Nullable WritableMap params) {
    reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
  }

  public List<Address> getAddresses() {
    return kit.wallet().getWatchedAddresses();
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

  private class AddrWalletKit extends WalletAppKit {

    public AddrWalletKit(NetworkParameters params, File directory, String filePrefix) {
      super(params, directory, filePrefix);
    }

    @Override
    protected void onSetupCompleted() {
      this.wallet().addCoinsReceivedEventListener(receivedListener);
      this.wallet().addCoinsSentEventListener(sentListener);
      this.wallet().addTransactionConfidenceEventListener(transactionListener);
    }
  }

  private class AddrDownloadListener extends DownloadProgressTracker {

    @Override
    protected void progress(double pct, int blocksSoFar, Date date) {
      super.progress(pct, blocksSoFar, date);
      WritableMap params = Arguments.createMap();
      params.putDouble("percent", pct);
      sendEvent(reactContext, "syncPercent", params);
    }

    @Override
    protected void doneDownload() {
      super.doneDownload();

      sendEvent(reactContext, "syncCompleted", null);
    }
  }


  private class AddrCoinsReceivedListener implements WalletCoinsReceivedEventListener {
    @Override
    public void onCoinsReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
    }
  }

  private class AddrCoinsSentListener implements WalletCoinsSentEventListener {
    @Override
    public void onCoinsSent(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {

    }
  }

  private class AddrTransactionConfidenceListener implements TransactionConfidenceEventListener {
    @Override
    public void onTransactionConfidenceChanged(Wallet wallet, Transaction tx) {
    }
  }
}
