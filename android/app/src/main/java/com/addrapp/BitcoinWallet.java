package com.addrapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.widget.Toast;

import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.WalletProtobufSerializer;
import org.bitcoinj.wallet.UnreadableWalletException;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;

import com.facebook.react.bridge.ReactApplicationContext;

public class BitcoinWallet {

  public static boolean TEST = true;

  public static final String WALLET_FILENAME_PROTOBUF = "wallet-protobuf";
  public static final NetworkParameters NETWORK_PARAMETERS = TEST ? TestNet3Params.get() : MainNetParams.get();

  private File walletFile;
  private Wallet wallet;
  private ReactApplicationContext reactContext;

  BitcoinWallet(ReactApplicationContext _reactContext) {
    reactContext = _reactContext;
    walletFile = reactContext.getFileStreamPath(WALLET_FILENAME_PROTOBUF);
    loadWallet();
    postLoadWallet();
  }

  public void initMnemonicCode() {
    Toast.makeText(reactContext, "Mnemonic code not generated!", Toast.LENGTH_LONG).show();
  }

  public void backupWallet() {
    Toast.makeText(reactContext, "Backup wallet not supported!", Toast.LENGTH_LONG).show();
  }

  public void restoreWalletFromBackup() {
    Toast.makeText(reactContext, "Restore wallet not supported!", Toast.LENGTH_LONG).show();
  }

  public void loadWallet() {
    if (walletFile.exists()) {
      FileInputStream walletStream = null;

      try {
        walletStream = new FileInputStream(walletFile);
        wallet = new WalletProtobufSerializer().readWallet(walletStream);
      } catch (final FileNotFoundException x) {
        Toast.makeText(reactContext, "Wallet file not found!", Toast.LENGTH_LONG).show();
        restoreWalletFromBackup();
      } catch (final UnreadableWalletException x) {
        Toast.makeText(reactContext, "Wallet file corrupted!", Toast.LENGTH_LONG).show();
        restoreWalletFromBackup();
      } finally {
        if (walletStream != null) {
          try {
            walletStream.close();
          } catch (final IOException x) {
            // swallow
          }
        }
      }

      if (!wallet.isConsistent()) {
        Toast.makeText(reactContext, "inconsistent wallet: " + walletFile, Toast.LENGTH_LONG).show();
        restoreWalletFromBackup();
      }

      if (!wallet.getParams().equals(NETWORK_PARAMETERS))
        throw new Error("bad wallet network parameters: " + wallet.getParams().getId());
    } else {
      wallet = new Wallet(NETWORK_PARAMETERS);

      try {
        wallet.saveToFile(walletFile);
      } catch (final IOException x) {
        throw new RuntimeException(x);
      }
      backupWallet();

      Toast.makeText(reactContext, "New wallet created!", Toast.LENGTH_LONG).show();
    }
  }

  private void postLoadWallet() {
    wallet.autosaveToFile(walletFile, 100, TimeUnit.MILLISECONDS, null);
    wallet.cleanup();
    backupWallet();
  }
}
