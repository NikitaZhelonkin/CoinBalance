package ru.nikitazhelonkin.coinbalance.data.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = @Index(name = "exchange_id_index", value = "exchange_id"),
        foreignKeys = @ForeignKey(entity = Exchange.class,
                parentColumns = "mId",
                childColumns = "exchange_id",
                onDelete = ForeignKey.CASCADE))
public class ExchangeBalance {

    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "exchange_id")
    private int mExchangeId;
    @ColumnInfo(name = "coin_ticker")
    private String mCoinTicker;
    @ColumnInfo(name = "balance")
    private float mBalance;

    public ExchangeBalance(int exchangeId, String coinTicker, float balance) {
        mExchangeId = exchangeId;
        mCoinTicker = coinTicker;
        mBalance = balance;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getExchangeId() {
        return mExchangeId;
    }

    public void setExchangeId(int exchangeId) {
        mExchangeId = exchangeId;
    }

    public String getCoinTicker() {
        return mCoinTicker;
    }

    public void setCoinTicker(String coinTicker) {
        mCoinTicker = coinTicker;
    }

    public float getBalance() {
        return mBalance;
    }

    public void setBalance(float balance) {
        mBalance = balance;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExchangeBalance that = (ExchangeBalance) o;

        return mId == that.mId;
    }

    @Override
    public int hashCode() {
        return mId;
    }

    @Override
    public String toString() {
        return "ExchangeBalance{" +
                "mId=" + mId +
                ", mExchangeId=" + mExchangeId +
                ", mCoinTicker='" + mCoinTicker + '\'' +
                ", mBalance=" + mBalance +
                '}';
    }
}
