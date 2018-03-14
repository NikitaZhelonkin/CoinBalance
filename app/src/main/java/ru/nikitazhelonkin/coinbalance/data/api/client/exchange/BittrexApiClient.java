package ru.nikitazhelonkin.coinbalance.data.api.client.exchange;


import java.util.HashMap;

import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.utils.DigestUtil;
import ru.nikitazhelonkin.coinbalance.data.api.response.BittrexBalancesResponse;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BittrexApiService;

public class BittrexApiClient implements ExchangeApiClient {

    private BittrexApiService mApiService;

    public BittrexApiClient(BittrexApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Single<HashMap<String, Float>> getBalances(String apiKey, String apiSecret) {
        String nonce = String.valueOf(System.currentTimeMillis());
        String url = String.format("https://bittrex.com/api/v1.1/account/getbalances?apikey=%s&nonce=%s", apiKey, nonce);
        String signature = DigestUtil.hmacString(url, apiSecret, "HmacSHA512");
        return mApiService.balances(apiKey, nonce, signature).map(BittrexBalancesResponse::toMap);
    }
}
