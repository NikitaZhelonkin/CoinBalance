package ru.nikitazhelonkin.coinbalance.data.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

public class BittrexBalancesResponse {

    private String mMessage;

    private boolean mSuccess;

    private List<Balance> mResult;

    public static class Balance {
        @JsonProperty("Currency")
        public String currency;
        @JsonProperty("Balance")
        public String balance;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setSuccess(boolean success) {
        mSuccess = success;
    }

    public boolean isSuccess() {
        return mSuccess;
    }

    public void setResult(List<Balance> balances) {
        mResult = balances;
    }

    public HashMap<String, Float> getNonZeroBalances() {
        HashMap<String, Float> map = new HashMap<>();
        for (Balance b : mResult) {
            Float value =Float.parseFloat(b.balance);
            if (value != 0)
                map.put(coinTicker(b.currency), value);
        }
        return map;
    }

    private String coinTicker(String currency) {
        if ("BCC".equalsIgnoreCase(currency))
            currency = "BCH";
        return currency.toUpperCase();
    }

}
