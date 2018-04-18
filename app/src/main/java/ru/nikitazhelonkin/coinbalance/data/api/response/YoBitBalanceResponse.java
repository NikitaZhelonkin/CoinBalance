package ru.nikitazhelonkin.coinbalance.data.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;


public class YoBitBalanceResponse {

    public int success;
    public String error;

    @JsonProperty("return")
    public Data data;

    public boolean isSuccess(){
        return success == 1;
    }

    public HashMap<String, Float> getNonZeroBalances(){
        if (data != null && data.funds != null) {
            HashMap<String, Float> result = new HashMap<>();
            for (String key : data.funds.keySet()) {
                float factor = (float) Math.pow(10, 6);
                float value = Math.round(data.funds.get(key) * factor) / factor;
                if (value != 0) {
                    result.put(coinTicker(key), value);
                }
            }
            return result;
        }
        return null;
    }

    private String coinTicker(String key) {
        if ("dsh".equalsIgnoreCase(key))
            key = "dash";
        return key.toUpperCase();
    }

    public static class Data{

        public HashMap<String,Float> funds;
    }


}
