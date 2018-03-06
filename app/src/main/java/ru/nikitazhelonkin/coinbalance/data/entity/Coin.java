package ru.nikitazhelonkin.coinbalance.data.entity;


import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

public class Coin {

    private String mTicker;
    private String mName;
    @DrawableRes
    private int mIconResId;

    public Coin(@NonNull String ticker, @NonNull String name, @DrawableRes int iconResId) {
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

    @DrawableRes
    public int getIconResId() {
        return mIconResId;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "mTicker='" + mTicker + '\'' +
                ", mName='" + mName + '\'' +
                '}';
    }
}
