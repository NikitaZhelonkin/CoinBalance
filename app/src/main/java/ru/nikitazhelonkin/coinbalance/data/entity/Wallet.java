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
    private final int mId;
    @ColumnInfo(name = "coin_ticker")
    private final String mCoinTicker;
    @ColumnInfo(name = "address")
    private final String mAddress;
    @ColumnInfo(name = "alias")
    private String mAlias;
    @ColumnInfo(name = "balance")
    private float mBalance;
    @ColumnInfo(name = "position")
    private int mPosition;
    @ColumnInfo(name = "create_at")
    private final long mCreatedAt;
    @ColumnInfo(name = "status")
    private int mStatus;


    @Ignore
    public Wallet(@NonNull String coinTicker, @NonNull String address, @Nullable String alias) {
        this(0, coinTicker, address, alias, 0, -1, System.currentTimeMillis(), STATUS_NONE);
    }

    @ParcelConstructor
    public Wallet(int id,
                  @NonNull String coinTicker,
                  @NonNull String address,
                  @Nullable String alias,
                  float balance,
                  int position,
                  long createdAt,
                  int status) {
        mId = id;
        mCoinTicker = coinTicker;
        mAddress = address;
        mAlias = alias;
        mBalance = balance;
        mPosition = position;
        mCreatedAt = createdAt;
        mStatus = status;
    }

    @Override
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

    public void setAlias(String alias) {
        mAlias = alias;
    }

    public float getBalance() {
        return mBalance;
    }

    public void setBalance(float balance) {
        mBalance = balance;
    }

    public int getPosition() {
        return mPosition;
    }

    @Override
    public void setPosition(int position) {
        mPosition = position;
    }

    public long getCreatedAt() {
        return mCreatedAt;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
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
