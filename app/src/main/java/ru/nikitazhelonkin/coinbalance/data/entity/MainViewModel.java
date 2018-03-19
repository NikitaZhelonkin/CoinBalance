package ru.nikitazhelonkin.coinbalance.data.entity;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ru.nikitazhelonkin.coinbalance.data.api.response.Prices;
import ru.nikitazhelonkin.coinbalance.utils.ListUtils;

public class MainViewModel {

    private Prices mPrices;

    private List<Wallet> mWallets;

    private List<Exchange> mExchanges;

    private List<ExchangeBalance> mExchangeBalances;

    private List<ListItem> mItems;

    public MainViewModel(List<Wallet> wallets,
                         List<Exchange> exchanges,
                         List<ExchangeBalance> exchangeBalances,
                         Prices prices) {
        mWallets = wallets;
        mExchanges = exchanges;
        mExchangeBalances = exchangeBalances;
        mPrices = prices;

        mItems = new ArrayList<>();
        mItems.addAll(mWallets);
        mItems.addAll(mExchanges);
        Collections.sort(mItems, new ListItemComparator());
    }

    public List<AssetItem> getAllAssets() {
        HashMap<String, Float> balances = new HashMap<>();
        for (Wallet w : getWallets()) {
            Float balance = balances.get(w.getCoinTicker());
            balances.put(w.getCoinTicker(), balance == null ? w.getBalance() : balance + w.getBalance());
        }
        for (Exchange e : getExchanges()) {
            List<ExchangeBalance> ebalances = getExchangeBalances(e.getId());
            for (ExchangeBalance b : ebalances) {
                Float balance = balances.get(b.getCoinTicker());
                balances.put(b.getCoinTicker(), balance == null ? b.getBalance() : balance + b.getBalance());

            }
        }
        List<AssetItem> items = new ArrayList<>();
        for (String coin : balances.keySet()) {
            float balance = balances.get(coin);
            Prices.Price price = getPrice(coin);

            float priceValue = price == null ? 0 : price.price;
            float change24 = price == null ? 0 : price.change24 / price.open24 * 100;
            float currencyBalance = balance * priceValue;
            items.add(new AssetItem(coin,
                    balance,
                    getCurrency(),
                    priceValue, change24,
                    currencyBalance / getTotalBalance() * 100));

        }
        Collections.sort(items, (a1, a2) -> {
            float b1 = a1.getCurrencyBalance();
            float b2 = a2.getCurrencyBalance();
            return b1 > b2 ? -1 : b1 < b2 ? 1 : 0;
        });
        return items;
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

    public String getCurrency() {
        return mPrices.getCurrency();
    }

    public float getTotalBalance() {
        return getWalletsBalance() + getExchangeBalances();
    }

    private float getWalletsBalance() {
        return ListUtils.reduce(mWallets, 0f,
                (b, wallet) -> b + wallet.getBalance() * getPriceValue(wallet.getCoinTicker()));
    }

    private float getExchangeBalances() {
        return getBalance(mExchangeBalances);
    }

    public List<ExchangeBalance> getExchangeBalances(long exchangeId) {
        return ListUtils.filter(mExchangeBalances,
                exchangeBalance -> exchangeBalance.getExchangeId() == exchangeId);
    }

    public float getBalance(List<ExchangeBalance> balances) {
        return ListUtils.reduce(balances, 0f,
                (b, exchangeBalance) -> b + exchangeBalance.getBalance() * getPriceValue(exchangeBalance.getCoinTicker())
        );
    }

    public Prices.Price getPrice(String coin) {
        return mPrices.getPrice(coin);
    }

    public float getPriceValue(String coin) {
        Prices.Price price = getPrice(coin);
        return price == null ? 0 : price.price;
    }

}
