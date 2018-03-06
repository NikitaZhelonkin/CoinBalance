package ru.nikitazhelonkin.coinbalance.data.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public class XrpResponse {

    @JsonProperty("account_data")
    public AccountData accountData;

    public static class AccountData {

        @JsonProperty("initial_balance")
        public String balance;

    }
}
