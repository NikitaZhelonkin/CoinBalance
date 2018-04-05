package ru.nikitazhelonkin.coinbalance.data.entity;


import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.nikitazhelonkin.coinbalance.R;

public enum  ExchangeService {

    BITFINEX("Bitfinex", R.drawable.bitfinex),
    BITTREX("Bittrex", R.drawable.bittrex),
    BINANCE("Binance", R.drawable.binance),
    COINBASE("Coinbase", R.drawable.coinbase),
    GEMINI("Gemini", R.drawable.gemini),
    HITBTC("HitBTC", R.drawable.hitbtc),
    KRAKEN("Kraken", R.drawable.kraken),
    POLONIEX("Poloniex", R.drawable.poloniex);

    String name;
    int iconResId;

    ExchangeService(@NonNull String name, @DrawableRes int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }

    @Nullable
    public static ExchangeService forName(String name) {
        for (ExchangeService service : values()) {
            if (service.getName().equalsIgnoreCase(name)) {
                return service;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }
}
