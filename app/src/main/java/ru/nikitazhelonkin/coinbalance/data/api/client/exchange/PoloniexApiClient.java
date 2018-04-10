package ru.nikitazhelonkin.coinbalance.data.api.client.exchange;


import java.util.HashMap;

import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.HttpErrorTransformer;
import ru.nikitazhelonkin.coinbalance.data.api.response.PoloniexBalancesResponse;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.PoloniexApiService;
import ru.nikitazhelonkin.coinbalance.utils.DigestUtil;

public class PoloniexApiClient implements ExchangeApiClient {

    private PoloniexApiService mApiService;
    public PoloniexApiClient(PoloniexApiService apiService){
        mApiService = apiService;
    }

    @Override
    public Single<HashMap<String, Float>> getBalances(String apiKey, String apiSecret) {
        String nonce = String.valueOf(System.currentTimeMillis());
        String signature = DigestUtil.hmacString("nonce="+nonce+"&command=returnBalances", apiSecret, "HmacSHA512");
        return mApiService.balances(nonce,"returnBalances", apiKey, signature)
                .map(PoloniexBalancesResponse::getNonZeroBalances)
                .compose(new HttpErrorTransformer<>());
    }
}
