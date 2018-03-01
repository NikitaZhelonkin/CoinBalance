package ru.nikitazhelonkin.cryptobalance.data.entity;


public class WalletViewModel {

    private String mCoinTicker;
    private String mCoinName;
    private int mCoinIconResId;
    private String mAddress;
    private String mAlias;
    private String mBalance;

    public WalletViewModel(Wallet wallet, Coin coin, String balance) {
        mCoinTicker = wallet.getCoinTicker();
        mCoinName = coin.getName();
        mCoinIconResId = coin.getIconResId();
        mAddress = wallet.getAddress();
        mAlias = wallet.getAlias();
        mBalance = balance;
    }

    public String getCoinTicker() {
        return mCoinTicker;
    }

    public String getCoinName() {
        return mCoinName;
    }

    public int getCoinIconResId() {
        return mCoinIconResId;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getAlias() {
        return mAlias;
    }

    public String getBalance() {
        return mBalance;
    }
}
