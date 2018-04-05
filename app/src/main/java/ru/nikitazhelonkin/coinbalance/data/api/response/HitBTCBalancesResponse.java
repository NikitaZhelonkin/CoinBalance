package ru.nikitazhelonkin.coinbalance.data.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@JsonDeserialize(using = HitBTCBalancesResponse.Deserializer.class)
public class HitBTCBalancesResponse {

    private List<Balance> mBalances;

    public HitBTCBalancesResponse(List<Balance> balances){
        mBalances = balances;
    }

    public HashMap<String, Float> getNonZeroBalances() {
        HashMap<String, Float> map = new HashMap<>();
        for (Balance b : mBalances) {
            Float value =Float.parseFloat(b.balance);
            if (value != 0)
                map.put(b.currency, value);
        }
        return map;
    }

    public static class Balance {
        @JsonProperty("currency")
        public String currency;
        @JsonProperty("available")
        public String balance;
    }

    public static class Deserializer extends JsonDeserializer<HitBTCBalancesResponse> {

        @Override
        public HitBTCBalancesResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectMapper mapper = (ObjectMapper) p.getCodec();
            return new HitBTCBalancesResponse(mapper.readValue(p, new TypeReference<List<Balance>>() {}));
        }
    }
}
