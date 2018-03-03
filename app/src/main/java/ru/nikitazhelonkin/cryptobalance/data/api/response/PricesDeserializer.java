package ru.nikitazhelonkin.cryptobalance.data.api.response;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import ru.nikitazhelonkin.cryptobalance.utils.L;

public class PricesDeserializer extends JsonDeserializer<Prices> {

    @Override
    public Prices deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        Iterator<String> fields = node.fieldNames();
        HashMap<String, Float> prices = new HashMap<>();
        while (fields.hasNext()) {
            String coin = fields.next();
            JsonNode coinNode = node.get(coin);
            String value = coinNode.get(coinNode.fieldNames().next()).toString();
            prices.put(coin, Float.parseFloat(value));
        }
        return new Prices(prices);
    }
}
