package ru.nikitazhelonkin.cryptobalance.data.entity;


import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import ru.nikitazhelonkin.cryptobalance.data.api.response.Prices;
import ru.nikitazhelonkin.cryptobalance.utils.ListUtils;

public class MainViewModel {

    private Prices mPrices;

    private List<Wallet> mWallets;

    private List<Coin> mCoins;

    private String mCurrency;

    public MainViewModel(String currency, List<Wallet> wallets, List<Coin> coins, Prices prices) {
        mCurrency = currency;
        mWallets = wallets;
        mCoins = coins;
        mPrices = prices;
    }

    public List<Wallet> getWallets() {
        return mWallets;
    }

    public Wallet getWallet(int position){
        return mWallets.get(position);
    }

    public int getWalletCount(){
        return mWallets.size();
    }

    public Coin getCoin(@NonNull String coin) {
        return ListUtils.find(mCoins, c -> c.getTicker().equals(coin));
    }

    public void  swapWallets(int fromPosition, int toPosition){
        Collections.swap(mWallets, fromPosition, toPosition);
    }

    public float getPrice(String coin) {
        return mPrices.getPrice(coin);
    }

    public String getCurrency() {
        return mCurrency;
    }

    public float getTotalBalance() {
        return ListUtils.reduce(mWallets, 0f,
                (b, wallet) -> b + wallet.getBalance() * getPrice(wallet.getCoinTicker()));
    }

}
