package ru.nikitazhelonkin.coinbalance.data.api.client.exchange;


import java.util.HashMap;

import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.HttpErrorTransformer;
import ru.nikitazhelonkin.coinbalance.data.api.response.CoinbaseBalanceResponse;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.CoinbaseApiService;
import ru.nikitazhelonkin.coinbalance.utils.DigestUtil;

public class CoinbaseApiClient implements ExchangeApiClient {

    private CoinbaseApiService mApiService;

    public CoinbaseApiClient(CoinbaseApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Single<HashMap<String, Float>> getBalances(String apiKey, String apiSecret) {
        return mApiService.getTime().flatMap(r -> {
            String timestamp = r.data.epoch;
            String payload = timestamp + "GET" + "/v2/accounts";
            String signature = DigestUtil.hmacString(payload, apiSecret, "HmacSHA256");
            return mApiService.balances(apiKey, signature, timestamp)
                    .map(CoinbaseBalanceResponse::getNonZeroBalances)
                    .compose(new HttpErrorTransformer<>());
        });

    }

}
