package ru.nikitazhelonkin.coinbalance.data.api.response;


import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import ru.nikitazhelonkin.coinbalance.utils.ListUtils;

public class NeoScanBalanceResponse {

    private List<Balance> balances;

    @JsonProperty("balance")
    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    @JsonProperty("balance")
    public List<Balance> getBalances() {
        return balances;
    }

    @Ignore
    public String getBalance(@NonNull String asset) {
        return balances == null ? null : ListUtils.find(balances, b -> asset.equalsIgnoreCase(b.asset)).amount;
    }

    public static class Balance {

        public String asset;

        public String amount;
    }
}
