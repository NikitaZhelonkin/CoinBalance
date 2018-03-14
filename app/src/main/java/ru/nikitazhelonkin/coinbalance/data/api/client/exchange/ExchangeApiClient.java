package ru.nikitazhelonkin.coinbalance.data.api.client.exchange;


import java.util.HashMap;

import io.reactivex.Single;

public interface ExchangeApiClient {

    Single<HashMap<String, Float>> getBalances(String apiKey, String apiSecret);
}
