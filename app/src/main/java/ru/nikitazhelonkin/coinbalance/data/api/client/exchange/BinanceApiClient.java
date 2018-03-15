package ru.nikitazhelonkin.coinbalance.data.api.client.exchange;


import java.util.HashMap;

import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.utils.DigestUtil;
import ru.nikitazhelonkin.coinbalance.data.api.response.BinanceBalancesResponse;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BinanceApiService;

public class BinanceApiClient implements ExchangeApiClient {

    private BinanceApiService mApiService;

    public BinanceApiClient(BinanceApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Single<HashMap<String, Float>> getBalances(String apiKey, String apiSecret) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String queryString = String.format("timestamp=%s",  timeStamp);
        String signature = DigestUtil.hmacString(queryString, apiSecret, "HmacSHA256");
        return mApiService.balances(timeStamp, signature, apiKey).map(BinanceBalancesResponse::getNonZeroBalances);
    }
}
