package ru.nikitazhelonkin.coinbalance.data.entity;


import java.util.List;

import ru.nikitazhelonkin.coinbalance.data.api.response.Prices;

public class WalletDetailViewModel {

    private Wallet mWallet;

    private List<Token> mTokenList;

    private Prices mPrices;

    public WalletDetailViewModel(Wallet wallet, List<Token> tokenList, Prices prices){
        mWallet = wallet;
        mTokenList = tokenList;
        mPrices = prices;
    }

    public Wallet getWallet() {
        return mWallet;
    }

    public List<Token> getTokenList() {
        return mTokenList;
    }

    public float getPrice(){
        return mPrices.getPriceValue(mWallet.getCoinTicker());
    }

    public String getCurrency() {
        return mPrices.getCurrency();
    }

    public Prices getPrices() {
        return mPrices;
    }
}
