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

@JsonDeserialize(using = BitfinexBalancesResponse.Deserializer.class)
public class BitfinexBalancesResponse {

    private HashMap<String, Float> mBalances;

    public BitfinexBalancesResponse(HashMap<String, Float> balances) {
        mBalances = balances;
    }

    public HashMap<String, Float> getBalances() {
        return mBalances;
    }

    public static class Deserializer extends JsonDeserializer<BitfinexBalancesResponse> {

        @Override
        public BitfinexBalancesResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectMapper mapper = (ObjectMapper) p.getCodec();
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, Balance.class);
            List<Balance> balances = mapper.readValue(p, type);
            HashMap<String, Float> balancesMap = new HashMap<>();
            for (Balance balance : balances) {
                String coin = coinTicker(balance.currency);
                Float amount = Float.parseFloat(balance.amount);
                Float value = balancesMap.get(coin);
                balancesMap.put(coin, value == null ? amount : value + amount);
            }
            return new BitfinexBalancesResponse(balancesMap);
        }

        private String coinTicker(String key) {
            if ("dsh".equalsIgnoreCase(key))
                key = "dash";
            return key.toUpperCase();
        }

        public static class Balance {
            public String amount;
            public String available;
            public String currency;

            @Override
            public String toString() {
                return "{" +
                        "amount='" + amount + '\'' +
                        ", available='" + available + '\'' +
                        ", currency='" + currency + '\'' +
                        '}';
            }
        }
    }
}
