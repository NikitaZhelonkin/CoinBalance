package ru.nikitazhelonkin.cryptobalance.data.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ChainsoResponse {

    public ChainsoBalance data;

    public static class ChainsoBalance {

        @JsonProperty("confirmed_balance")
        public String confirmedBalance;
    }
}
