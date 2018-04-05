package ru.nikitazhelonkin.coinbalance.data.api.response;


import com.fasterxml.jackson.annotation.JsonCreator;
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

    public static class Price {

        public String fromSymbol;
        public String currency;
        public float price;
        public float open24;
        public float change24;

        @JsonCreator
        public Price(@JsonProperty("FROMSYMBOL") String fromSymbol,
                     @JsonProperty("TOSYMBOL") String currency,
                     @JsonProperty("PRICE") float price,
                     @JsonProperty("OPEN24HOUR") float open24,
                     @JsonProperty("CHANGE24HOUR") float change24) {
            this.fromSymbol = fromSymbol;
            this.currency = currency;
            this.price = price;
            this.open24 = open24;
            this.change24 = change24;

            if (fromSymbol.equals(currency)) {
                this.price = 1f;
                this.open24 = 1f;
                this.change24 = 0f;
            }
        }
    }

    public float getPriceValue(String coin) {
        Prices.Price price = getPrice(coin);
        return price == null ? 0 : price.price;
    }
}
