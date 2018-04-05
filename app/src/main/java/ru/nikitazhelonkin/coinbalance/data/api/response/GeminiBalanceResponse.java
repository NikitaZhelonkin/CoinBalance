package ru.nikitazhelonkin.coinbalance.data.api.response;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@JsonDeserialize(using = GeminiBalanceResponse.Deserializer.class)
public class GeminiBalanceResponse {

    private HashMap<String, Float> mBalances;

    public GeminiBalanceResponse(HashMap<String, Float> balances) {
        mBalances = balances;
    }

    public HashMap<String, Float> getBalances() {
        return mBalances;
    }

    public HashMap<String, Float> getNonZeroBalances() {
        HashMap<String, Float> nonZeroBalance = new HashMap<>();
        for (String key : mBalances.keySet()) {
            Float value = mBalances.get(key);
            if (value != 0)
                nonZeroBalance.put(key, value);
        }
        return nonZeroBalance;
    }

    public static class Deserializer extends JsonDeserializer<GeminiBalanceResponse> {

        @Override
        public GeminiBalanceResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectMapper mapper = (ObjectMapper) p.getCodec();
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, Balance.class);
            List<Balance> balances = mapper.readValue(p, type);
            HashMap<String, Float> balancesMap = new HashMap<>();
            for (Balance balance : balances) {
                String coin = balance.currency;
                Float amount = Float.parseFloat(balance.amount);
                Float value = balancesMap.get(coin);
                balancesMap.put(coin, value == null ? amount : value + amount);
            }
            return new GeminiBalanceResponse(balancesMap);
        }


        public static class Balance {
            public String amount;
            public String available;
            public String currency;
        }
    }
}
