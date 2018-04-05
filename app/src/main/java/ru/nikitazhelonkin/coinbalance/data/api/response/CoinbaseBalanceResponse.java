package ru.nikitazhelonkin.coinbalance.data.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

import ru.nikitazhelonkin.coinbalance.utils.L;

public class CoinbaseBalanceResponse {

    private List<Account> mAccounts;

    @JsonProperty("data")
    public void setAccounts(List<Account> accounts) {
        mAccounts = accounts;
    }

    public HashMap<String, Float> getNonZeroBalances() {
        HashMap<String, Float> map = new HashMap<>();
        for (Account account : mAccounts) {
            if ("crypto".equalsIgnoreCase(account.currency.type)) {
                Float value = Float.parseFloat(account.balance.balance);
                if (value != 0)
                    map.put(account.balance.currency, value);
            }
        }
        return map;
    }

    public static class Account{

        public Currency currency;
        public Balance balance;

    }

    public static class Balance{
        @JsonProperty("currency")
        public String currency;
        @JsonProperty("amount")
        public String balance;
    }

    public static class Currency{
        public String type;
    }
}
