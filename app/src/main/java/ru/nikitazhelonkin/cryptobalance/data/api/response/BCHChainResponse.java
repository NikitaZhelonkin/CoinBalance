package ru.nikitazhelonkin.cryptobalance.data.api.response;


public class BCHChainResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {

        public String balance;
    }
}
