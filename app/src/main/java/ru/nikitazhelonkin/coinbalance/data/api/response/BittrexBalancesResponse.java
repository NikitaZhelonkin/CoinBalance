package ru.nikitazhelonkin.coinbalance.data.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

public class BittrexBalancesResponse {

    private List<Balance> mResult;

    public static class Balance {
        @JsonProperty("Currency")
        public String currency;
        @JsonProperty("Balance")
        public String balance;
    }

    public void setResult(List<Balance> balances) {
        mResult = balances;
    }

    public HashMap<String, Float> toMap() {
        HashMap<String, Float> map = new HashMap<>();
        for (Balance b : mResult) {
            map.put(coinTicker(b.currency), Float.parseFloat(b.balance));
        }
        return map;
    }

    private String coinTicker(String currency) {
        if ("BCC".equalsIgnoreCase(currency))
            currency = "BCH";
        return currency.toUpperCase();
    }

}
