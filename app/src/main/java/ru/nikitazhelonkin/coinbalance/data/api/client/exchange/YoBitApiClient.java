package ru.nikitazhelonkin.coinbalance.data.api.client.exchange;


import java.util.HashMap;

import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.HttpErrorTransformer;
import ru.nikitazhelonkin.coinbalance.data.api.response.YoBitBalanceResponse;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.YoBitApiService;
import ru.nikitazhelonkin.coinbalance.data.exception.ApiError;
import ru.nikitazhelonkin.coinbalance.utils.DigestUtil;

public class YoBitApiClient implements ExchangeApiClient {

    private YoBitApiService mApiService;

    public YoBitApiClient(YoBitApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Single<HashMap<String, Float>> getBalances(String apiKey, String apiSecret) {
        return request("getInfo", apiKey, apiSecret)
                .map(response -> {
                    if (response.isSuccess()) {
                        return response.getNonZeroBalances();
                    } else {
                        throw new ApiError(response.error);
                    }
                });
    }

    private Single<YoBitBalanceResponse> request(String method, String apiKey, String apiSecret) {
        String nonce = String.valueOf(System.currentTimeMillis() / 1000);
        String payload = String.format("method=%s&nonce=%s", method, nonce);
        String signature = DigestUtil.hmacString(payload, apiSecret, "HmacSHA512");
        return mApiService.request(method, nonce, apiKey, signature)
                .compose(new HttpErrorTransformer<>());
    }
}
