package ru.nikitazhelonkin.coinbalance.data.api.response;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

@JsonDeserialize(using = KrakenBalancesResponse.Deserializer.class)
public class KrakenBalancesResponse {

    private HashMap<String, Float> mBalances;

    public KrakenBalancesResponse(HashMap<String, Float> balances) {
        mBalances = balances;
    }

    public HashMap<String, Float> getBalances() {
        return mBalances;
    }

    public static class Deserializer extends JsonDeserializer<KrakenBalancesResponse> {

        @Override
        public KrakenBalancesResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = p.getCodec().readTree(p);
            JsonNode result = node.get("result");
            HashMap<String, Float> balancesMap = new HashMap<>();
            Iterator<String> iterator = result.fieldNames();
            for (; iterator.hasNext(); ) {
                String key = iterator.next();
                String value = result.get(key).asText();
                balancesMap.put(coinTicker(key), Float.parseFloat(value));
            }
            return new KrakenBalancesResponse(balancesMap);
        }

        private String coinTicker(String key) {
            key = key.substring(1, key.length());
            if (key.equalsIgnoreCase("xbt"))
                key = "btc";
            return key.toUpperCase();
        }
    }
}
