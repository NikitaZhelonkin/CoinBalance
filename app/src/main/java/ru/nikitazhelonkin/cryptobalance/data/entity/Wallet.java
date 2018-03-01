package ru.nikitazhelonkin.cryptobalance.data.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(indices =
@Index(name = "index_name", value = "coin_ticker", unique = true))
public class Wallet {

    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = "coin_ticker")
    private String mCoinTicker;
    @ColumnInfo(name = "address")
    private String mAddress;
    @ColumnInfo(name = "alias")
    private String mAlias;

    public Wallet(@NonNull String coinTicker, @NonNull String address, @Nullable String alias) {
        mCoinTicker = coinTicker;
        mAddress = address;
        mAlias = alias;
    }

    public Wallet() {

    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public String getCoinTicker() {
        return mCoinTicker;
    }

    @NonNull
    public String getAddress() {
        return mAddress;
    }

    @Nullable
    public String getAlias() {
        return mAlias;
    }

    public void setCoinTicker(String coinTicker) {
        mCoinTicker = coinTicker;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public void setAlias(String alias) {
        mAlias = alias;
    }
}
