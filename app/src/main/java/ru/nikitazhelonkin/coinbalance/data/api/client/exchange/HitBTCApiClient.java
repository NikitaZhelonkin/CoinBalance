package ru.nikitazhelonkin.coinbalance.data.api.client.exchange;


import java.util.HashMap;

import io.reactivex.Single;
import okhttp3.Credentials;
import ru.nikitazhelonkin.coinbalance.data.api.HttpErrorTransformer;
import ru.nikitazhelonkin.coinbalance.data.api.response.HitBTCBalancesResponse;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.HitBTCApiService;

public class HitBTCApiClient implements ExchangeApiClient {

    private HitBTCApiService mApiService;

    public HitBTCApiClient(HitBTCApiService apiService){
        mApiService = apiService;
    }

    @Override
    public Single<HashMap<String, Float>> getBalances(String apiKey, String apiSecret) {
        return mApiService.balances( Credentials.basic(apiKey, apiSecret))
                .map(HitBTCBalancesResponse::getNonZeroBalances)
                .compose(new HttpErrorTransformer<>());
    }
}
