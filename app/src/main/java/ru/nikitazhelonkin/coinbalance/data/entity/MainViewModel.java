package ru.nikitazhelonkin.coinbalance.data.entity;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.nikitazhelonkin.coinbalance.data.api.response.Prices;
import ru.nikitazhelonkin.coinbalance.utils.ListUtils;

public class MainViewModel {

    private Prices mPrices;

    private List<Wallet> mWallets;

    private List<Exchange> mExchanges;

    private List<ExchangeBalance> mExchangeBalances;

    private String mCurrency;

    private List<ListItem> mItems;

    public MainViewModel(String currency,
                         List<Wallet> wallets,
                         List<Exchange> exchanges,
                         List<ExchangeBalance> exchangeBalances,
                         Prices prices) {
        mCurrency = currency;
        mWallets = wallets;
        mExchanges = exchanges;
        mExchangeBalances = exchangeBalances;
        mPrices = prices;

        mItems = new ArrayList<>();
        mItems.addAll(mWallets);
        mItems.addAll(mExchanges);
        Collections.sort(mItems, new ListItemComparator());
    }

    public List<Wallet> getWallets() {
        return mWallets;
    }

    public List<Exchange> getExchanges() {
        return mExchanges;
    }

    public List<ListItem> getItems() {
        return mItems;
    }

    public ListItem getItem(int position) {
        return mItems.get(position);
    }

    public Coin getCoin(@NonNull String coin) {
        return Coin.forTicker(coin);
    }

    public void swapItems(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
    }

    public float getPrice(String coin) {
        return mPrices.getPrice(coin);
    }

    public String getCurrency() {
        return mCurrency;
    }

    public float getTotalBalance() {
        return getWalletsBalance() + getExchangeBalances();
    }

    private float getWalletsBalance() {
        return ListUtils.reduce(mWallets, 0f,
                (b, wallet) -> b + wallet.getBalance() * getPrice(wallet.getCoinTicker()));
    }

    private float getExchangeBalances() {
        return getBalances(mExchangeBalances);
    }

    public float getExchangeBalances(long exchangeId) {
        List<ExchangeBalance> filtered = ListUtils.filter(mExchangeBalances,
                exchangeBalance -> exchangeBalance.getExchangeId() == exchangeId);
        return getBalances(filtered);
    }

    private float getBalances(List<ExchangeBalance> balances) {
        return ListUtils.reduce(balances, 0f,
                (b, exchangeBalance) -> b + exchangeBalance.getBalance() * getPrice(exchangeBalance.getCoinTicker())
        );
    }

}
