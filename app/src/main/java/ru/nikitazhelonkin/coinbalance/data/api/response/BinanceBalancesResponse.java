package ru.nikitazhelonkin.coinbalance.data.api.response;


import java.util.HashMap;
import java.util.List;

public class BinanceBalancesResponse {

    private List<Balance> mBalances;

    public static class Balance {
        public String asset;
        public String free;
    }

    public void setBalances(List<Balance> balances) {
        mBalances = balances;
    }

    public HashMap<String, Float> toMap() {
        HashMap<String, Float> map = new HashMap<>();
        for (Balance b : mBalances) {
            float balance = Float.parseFloat(b.free);
            if (balance != 0f) {
                map.put(b.asset, Float.parseFloat(b.free));
            }
        }
        return map;
    }
}
