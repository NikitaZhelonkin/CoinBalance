package ru.nikitazhelonkin.coinbalance.data.api.client.exchange;


import java.util.HashMap;

import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BittrexApiService;
import ru.nikitazhelonkin.coinbalance.data.exception.NoPermissionException;
import ru.nikitazhelonkin.coinbalance.data.exception.UnknownException;
import ru.nikitazhelonkin.coinbalance.utils.DigestUtil;

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
        return mApiService.balances(apiKey, nonce, signature)
                .map(response -> {
                    if (response.isSuccess()) {
                        return response.getNonZeroBalances();
                    } else if ("INVALID_PERMISSION".equals(response.getMessage())) {
                        throw new NoPermissionException();
                    } else {
                        throw new UnknownException("Error getting bittrex balances");
                    }
                });
    }
}
