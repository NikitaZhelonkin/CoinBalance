package ru.nikitazhelonkin.cryptobalance.data.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class XLMResponse {

    public List<Balance> balances;

    public String getNativeBalance() {
        if (balances != null) {
            for (Balance b : balances) {
                if ("native".equals(b.type)) {
                    return b.balance;
                }
            }
        }
        return "0.0";
    }

    public static class Balance {
        public String balance;
        @JsonProperty("asset_type")
        public String type;
    }
}
