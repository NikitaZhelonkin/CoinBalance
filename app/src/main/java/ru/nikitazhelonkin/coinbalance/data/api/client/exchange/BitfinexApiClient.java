package ru.nikitazhelonkin.coinbalance.data.api.client.exchange;


import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.HttpErrorTransformer;
import ru.nikitazhelonkin.coinbalance.data.api.response.BitfinexBalancesResponse;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BitfinexApiService;
import ru.nikitazhelonkin.coinbalance.utils.DigestUtil;

public class BitfinexApiClient implements ExchangeApiClient {

    private BitfinexApiService mApiService;

    public BitfinexApiClient(BitfinexApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Single<HashMap<String, Float>> getBalances(String apiKey, String apiSecret) {
        String payload;
        try {
            JSONObject jo = new JSONObject();
            jo.put("request", "/v1/balances");
            jo.put("nonce", Long.toString(System.currentTimeMillis()));
            payload = jo.toString();
        } catch (JSONException e) {
            return Single.error(e);
        }
        String payloadBase64 = Base64.encodeToString(payload.getBytes(), Base64.NO_WRAP);
        String payloadSha384hmac = DigestUtil.hmacString(payloadBase64, apiSecret, "HmacSHA384");
        return mApiService.balances(apiKey, payloadBase64, payloadSha384hmac)
                .map(BitfinexBalancesResponse::getNonZeroBalances)
                .compose(new HttpErrorTransformer<>());
    }
}
