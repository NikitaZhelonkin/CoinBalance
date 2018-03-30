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
    private int mId;

    @ColumnInfo(name = "wallet_address")
    private String mWalletAddress;
    @ColumnInfo(name = "token_ticker")
    private String mTokenTicker;
    @ColumnInfo(name = "token_name")
    private String mTokenName;
    @ColumnInfo(name = "balance")
    private float mBalance;

    public Token(String walletAddress, String tokenTicker, String tokenName, float balance) {
        mWalletAddress = walletAddress;
        mTokenTicker = tokenTicker;
        mTokenName = tokenName;
        mBalance = balance;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }


    public String getWalletAddress() {
        return mWalletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        mWalletAddress = walletAddress;
    }

    public String getTokenTicker() {
        return mTokenTicker;
    }

    public void setTokenTicker(String tokenTicker) {
        mTokenTicker = tokenTicker;
    }

    public void setTokenName(String tokenName) {
        mTokenName = tokenName;
    }

    public String getTokenName() {
        return mTokenName;
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
