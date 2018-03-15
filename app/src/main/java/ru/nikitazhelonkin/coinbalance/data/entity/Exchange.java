package ru.nikitazhelonkin.coinbalance.data.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.nikitazhelonkin.coinbalance.data.db.ExchangeServiceConverter;

@Entity(indices =
@Index(name = "exchange_index", value = {"service", "api_key"}, unique = true))
public class Exchange implements ListItem {

    public static final int STATUS_NONE = 0;
    public static final int STATUS_ERROR = -1;
    public static final int STATUS_ERROR_NO_PERMISSION = -2;
    public static final int STATUS_OK = 1;

    @PrimaryKey(autoGenerate = true)
    private int mId;
    @TypeConverters(ExchangeServiceConverter.class)
    @ColumnInfo(name = "service")
    private ExchangeService mService;
    @ColumnInfo(name = "api_key")
    private String mApiKey;
    @ColumnInfo(name = "api_secret")
    private String mApiSecret;
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "position")
    private int mPosition;
    @ColumnInfo(name = "created_at")
    private long mCreatedAt;
    @ColumnInfo(name = "status")
    private int mStatus;

    public Exchange() {

    }

    @Ignore
    public Exchange(@NonNull ExchangeService service,
                    @NonNull String apiKey,
                    @NonNull String apiSecret,
                    @Nullable String title) {
        this(service, apiKey, apiSecret, title, -1, System.currentTimeMillis(), STATUS_NONE);
    }

    @Ignore
    public Exchange(@NonNull ExchangeService service,
                    @NonNull String apiKey,
                    @NonNull String apiSecret,
                    @Nullable String title,
                    int position,
                    long createdAt,
                    int status) {
        mService = service;
        mApiKey = apiKey;
        mApiSecret = apiSecret;
        mTitle = title;
        mPosition = position;
        mCreatedAt = createdAt;
        mStatus = status;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public ExchangeService getService() {
        return mService;
    }

    public void setService(ExchangeService service) {
        mService = service;
    }

    public String getApiKey() {
        return mApiKey;
    }

    public void setApiKey(String apiKey) {
        mApiKey = apiKey;
    }

    public String getApiSecret() {
        return mApiSecret;
    }

    public void setApiSecret(String apiSecret) {
        mApiSecret = apiSecret;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(long createdAt) {
        mCreatedAt = createdAt;
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

        Exchange exchange = (Exchange) o;

        return mId == exchange.mId;
    }

    @Override
    public int hashCode() {
        return mId;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "mId=" + mId +
                ", mService=" + mService +
                ", mTitle='" + mTitle + '\'' +
                '}';
    }

}
