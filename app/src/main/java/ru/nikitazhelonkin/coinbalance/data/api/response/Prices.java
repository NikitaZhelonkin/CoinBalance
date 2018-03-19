package ru.nikitazhelonkin.coinbalance.data.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;

@JsonDeserialize(using = PricesDeserializer.class)
public class Prices {

    private HashMap<String, Price> mPrices;

    private String mCurrency;

    public Prices(HashMap<String, Price> prices) {
        mPrices = prices;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public Price getPrice(String coin) {
        return mPrices.get(coin);
    }

    public static class Price{

        @JsonProperty("TOSYMBOL")
        public String currency;
        @JsonProperty("PRICE")
        public float price;
        @JsonProperty("OPEN24HOUR")
        public float open24;
        @JsonProperty("CHANGE24HOUR")
        public float change24;
    }
}
