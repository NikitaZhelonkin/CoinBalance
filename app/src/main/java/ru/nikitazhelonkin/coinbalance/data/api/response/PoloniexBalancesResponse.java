package ru.nikitazhelonkin.coinbalance.data.api.response;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.HashMap;

@JsonDeserialize(using = PoloniexBalancesResponse.Deserializer.class)
public class PoloniexBalancesResponse {

    private HashMap<String, Float> mBalances;

    public PoloniexBalancesResponse(HashMap<String, Float> map) {
        mBalances = map;
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

    public static class Deserializer extends JsonDeserializer<PoloniexBalancesResponse> {

        @Override
        public PoloniexBalancesResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectMapper mapper = (ObjectMapper) p.getCodec();
            HashMap<String, Float> map = mapper.readValue(p, new TypeReference<HashMap<String, Float>>() {});
            return new PoloniexBalancesResponse(map);
        }

    }
}
