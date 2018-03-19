package ru.nikitazhelonkin.coinbalance.data.api.response;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class PricesDeserializer extends JsonDeserializer<Prices> {

    @Override
    public Prices deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        JsonNode raw = node.get("RAW");
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        Iterator<String> fields = raw.fieldNames();
        HashMap<String, Prices.Price> prices = new HashMap<>();
        while (fields.hasNext()) {
            String coin = fields.next();
            JsonNode coinNode = raw.get(coin);
            JsonNode priceNode = coinNode.get(coinNode.fieldNames().next());
            Prices.Price price = mapper.readValue(priceNode.toString(), Prices.Price.class);
            prices.put(coin, price);
        }
        return new Prices(prices);
    }
}
