package ru.nikitazhelonkin.coinbalance.data.api.response;


import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import ru.nikitazhelonkin.coinbalance.utils.ListUtils;

public class XrpResponse {

    @JsonProperty("balances")
    public List<Balance> mBalances;

    public Balance getBalance(@NonNull String currency){
        return ListUtils.find(mBalances, b -> currency.equalsIgnoreCase(b.currency));
    }

    public static class Balance {

        @JsonProperty("currency")
        public String currency;
        @JsonProperty("value")
        public String balance;

    }
}
