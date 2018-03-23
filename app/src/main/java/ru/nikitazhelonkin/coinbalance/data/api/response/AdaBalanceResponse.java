package ru.nikitazhelonkin.coinbalance.data.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public class AdaBalanceResponse {

    @JsonProperty("Right")
    public Data data;

    public static class Data{

        @JsonProperty("caBalance")
        public Balance balance;
    }

    public static class Balance{
        @JsonProperty("getCoin")
        public String value;
    }
}
