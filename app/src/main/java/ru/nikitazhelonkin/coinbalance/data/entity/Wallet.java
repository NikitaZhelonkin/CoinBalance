package ru.nikitazhelonkin.coinbalance.data.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Entity(indices =
@Index(name = "index_name", value = "address", unique = true))
@Parcel(Parcel.Serialization.BEAN)
public class Wallet implements ListItem {

    public static final int STATUS_NONE = 0;
    public static final int STATUS_ERROR = -1;
    public static final int STATUS_OK = 1;

    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = "coin_ticker")
    private String mCoinTicker;
    @ColumnInfo(name = "address")
    private String mAddress;
    @ColumnInfo(name = "alias")
    private String mAlias;
    @ColumnInfo(name = "balance")
    private float mBalance;
    @ColumnInfo(name = "position")
    private int mPosition;
    @ColumnInfo(name = "create_at")
    private long mCreatedAt;
    @ColumnInfo(name = "status")
    private int mStatus;

    public Wallet() {

    }

    @Ignore
    public Wallet(@NonNull String coinTicker, @NonNull String address, @Nullable String alias) {
        this(coinTicker, address, alias, 0, -1, System.currentTimeMillis(), STATUS_NONE);
    }

    @ParcelConstructor
    @Ignore
    public Wallet(@NonNull String coinTicker,
                  @NonNull String address,
                  @Nullable String alias,
                  float balance,
                  int position,
                  long createdAt,
                  int status) {
        mCoinTicker = coinTicker;
        mAddress = address;
        mAlias = alias;
        mBalance = balance;
        mPosition = position;
        mCreatedAt = createdAt;
        mStatus = status;
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

    public void setBalance(float balance) {
        mBalance = balance;
    }

    public float getBalance() {
        return mBalance;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public void setCreatedAt(long createdAt) {
        mCreatedAt = createdAt;
    }

    public long getCreatedAt() {
        return mCreatedAt;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public int getStatus() {
        return mStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wallet wallet = (Wallet) o;

        return mId == wallet.mId;
    }

    @Override
    public int hashCode() {
        return mId;
    }

}
