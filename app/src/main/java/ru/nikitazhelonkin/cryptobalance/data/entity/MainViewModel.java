package ru.nikitazhelonkin.cryptobalance.data.entity;


import android.support.annotation.NonNull;

import java.util.List;

import ru.nikitazhelonkin.cryptobalance.data.api.response.Prices;
import ru.nikitazhelonkin.cryptobalance.utils.ListUtils;

public class MainViewModel {

    private Prices mPrices;

    private List<Wallet> mWallets;

    private List<Coin> mCoins;

    public MainViewModel(List<Wallet> wallets, List<Coin> coins, Prices prices) {
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

//    public int indexOf(Coin coin) {
//        if (coin == null) {
//            return -1;
//        }
//        for (int i = 0; i < getCoinCount(); i++) {
//            if (coin.equals(getEntry(i).getKey())) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    public int getCoinCount() {
//        return mWallets.size();
//    }
//
//    public Map.Entry<Coin, List<Wallet>> getEntry(int position) {
//        Set<Map.Entry<Coin, List<Wallet>>> entries = mWallets.entrySet();
//        int i = 0;
//        for (Map.Entry<Coin, List<Wallet>> entry : entries)
//            if (i++ == position) return entry;
//
//        return null;
//    }
//
//    public List<Wallet> getWallets(Coin coin) {
//        return mWallets.get(coin);
//    }

    public float getPrice(String coin) {
        return mPrices.getPrice(coin);
    }

    public float getTotalBalance() {
        return ListUtils.reduce(mWallets, 0f,
                (b, wallet) -> b + wallet.getBalance() * getPrice(wallet.getCoinTicker()));
    }

}
