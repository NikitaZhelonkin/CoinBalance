package ru.nikitazhelonkin.coinbalance.data.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = @Index(name = "wallet_index", value = "wallet_address"),
        foreignKeys = @ForeignKey(entity = Wallet.class,
                parentColumns = "address",
                childColumns = "wallet_address",
                onDelete = ForeignKey.CASCADE))
public class Token {

    @PrimaryKey(autoGenerate = true)
    private final int mId;

    @ColumnInfo(name = "wallet_address")
    private final String mWalletAddress;
    @ColumnInfo(name = "token_ticker")
    private final String mTokenTicker;
    @ColumnInfo(name = "token_name")
    private final String mTokenName;
    @ColumnInfo(name = "balance")
    private final float mBalance;

    public Token(int id, String walletAddress, String tokenTicker, String tokenName, float balance) {
        mId = id;
        mWalletAddress = walletAddress;
        mTokenTicker = tokenTicker;
        mTokenName = tokenName;
        mBalance = balance;
    }

    public int getId() {
        return mId;
    }

    public String getWalletAddress() {
        return mWalletAddress;
    }

    public String getTokenTicker() {
        return mTokenTicker;
    }

    public String getTokenName() {
        return mTokenName;
    }

    public float getBalance() {
        return mBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        return mId == token.mId;
    }

    @Override
    public int hashCode() {
        return mId;
    }


    @Override
    public String toString() {
        return "Token{" +
                "mId=" + mId +
                ", mWalletAddress=" + mWalletAddress +
                ", mTokenName=" + mTokenName +
                ", mTokenTicker='" + mTokenTicker + '\'' +
                ", mBalance=" + mBalance +
                '}';
    }
}
