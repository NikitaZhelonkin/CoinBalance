package ru.nikitazhelonkin.coinbalance.data.entity;


import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.nikitazhelonkin.coinbalance.R;

public enum Coin {

    BTC("BTC", "Bitcoin", R.drawable.btc),
    BCH("BCH", "Bitcoin Cash", R.drawable.bch),
    ETH("ETH", "Ethereum", R.drawable.eth),
    ETC("ETC", "Ethereum Classic", R.drawable.etc),
    LTC("LTC", "Litecoin", R.drawable.ltc),
    XRP("XRP", "Ripple", R.drawable.xrp),
    DASH("DASH", "Dash", R.drawable.dash),
    XEM("XEM", "Nem", R.drawable.xem),
    XLM("XLM", "Stellar", R.drawable.xlm),
    ADA("ADA", "Cardano", R.drawable.ada),
    NEO("NEO", "Neo", R.drawable.neo),
    ZEC("ZEC", "ZCash", R.drawable.zec),
    DOGE("DOGE", "Doge", R.drawable.doge);

    private String mTicker;
    private String mName;
    private int mIconResId;

    Coin(@NonNull String ticker, @NonNull String name, @DrawableRes int iconResId) {
        mTicker = ticker;
        mName = name;
        mIconResId = iconResId;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    @NonNull
    public String getTicker() {
        return mTicker;
    }

    public int getIconResId() {
        return mIconResId;
    }

    @Nullable
    public static Coin forTicker(String ticker) {
        for (Coin coin : values()) {
            if (coin.getTicker().equalsIgnoreCase(ticker)) {
                return coin;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getTicker();
    }
}
