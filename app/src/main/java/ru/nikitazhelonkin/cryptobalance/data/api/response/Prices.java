package ru.nikitazhelonkin.cryptobalance.data.api.response;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;

@JsonDeserialize(using = PricesDeserializer.class)
public class Prices {

    private HashMap<String, Float> mPrices;

    public Prices(HashMap<String, Float> prices) {
        mPrices = prices;
    }

    public float getPrice(String coin) {
        Float price = mPrices.get(coin);
        return price != null ? price : 0f;
    }
}
