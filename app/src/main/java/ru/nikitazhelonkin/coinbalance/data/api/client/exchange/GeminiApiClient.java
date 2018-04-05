package ru.nikitazhelonkin.coinbalance.data.api.client.exchange;


import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.HttpErrorTransformer;
import ru.nikitazhelonkin.coinbalance.data.api.response.GeminiBalanceResponse;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.GeminiApiService;
import ru.nikitazhelonkin.coinbalance.utils.DigestUtil;

public class GeminiApiClient implements ExchangeApiClient {

    private GeminiApiService mApiService;

    public GeminiApiClient(GeminiApiService apiService){
        mApiService = apiService;
    }

    @Override
    public Single<HashMap<String, Float>> getBalances(String apiKey, String apiSecret) {
        String payload;
        try {
            JSONObject jo = new JSONObject();
            jo.put("nonce", Long.toString(System.currentTimeMillis()));
            jo.put("request", "/v1/balances");
            payload = jo.toString();
        } catch (JSONException e) {
            return Single.error(e);
        }
        String payloadBase64 = Base64.encodeToString(payload.getBytes(), Base64.NO_WRAP);
        String signature = DigestUtil.hmacString(payloadBase64, apiSecret, "HmacSHA384");
        return mApiService.balances(apiKey,payloadBase64, signature)
                .map(GeminiBalanceResponse::getNonZeroBalances)
                .compose(new HttpErrorTransformer<>());
    }
}
