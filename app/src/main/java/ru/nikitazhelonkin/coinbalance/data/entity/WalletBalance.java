package ru.nikitazhelonkin.coinbalance.data.entity;


import android.support.annotation.NonNull;

import java.util.List;

public class WalletBalance {

    private String mBalance;

    private List<Token> mTokenList;

    public WalletBalance(String balance){
        this(balance, null);
    }

    public WalletBalance(String balance, List<Token> tokens){
        mBalance = balance;
        mTokenList = tokens;
    }

    public String getBalance() {
        return mBalance;
    }

    public List<Token> getTokenList() {
        return mTokenList;
    }
}
