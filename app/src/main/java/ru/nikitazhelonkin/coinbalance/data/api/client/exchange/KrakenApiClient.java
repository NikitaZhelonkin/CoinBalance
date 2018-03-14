package ru.nikitazhelonkin.coinbalance.data.api.client.exchange;


import android.util.Base64;

import java.nio.charset.Charset;
import java.util.HashMap;

import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.response.KrakenBalancesResponse;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.KrakenApiService;
import ru.nikitazhelonkin.coinbalance.utils.DigestUtil;

public class KrakenApiClient implements ExchangeApiClient {

    private KrakenApiService mApiService;

    public KrakenApiClient(KrakenApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Single<HashMap<String, Float>> getBalances(String apiKey, String apiSecret) {
        String nonce = String.valueOf(System.currentTimeMillis());
        byte[] decodedSecret = Base64.decode(apiSecret, Base64.DEFAULT);
        byte[] pathBytes = "/0/private/Balance".getBytes(Charset.forName("UTF-8"));
        byte[] hash256Bytes = DigestUtil.sha256(nonce + "nonce=" + nonce);
        byte[] signature = DigestUtil.hmac(concat(pathBytes, hash256Bytes), decodedSecret, "HmacSHA512");
        String signatureEncoded = Base64.encodeToString(signature, Base64.NO_WRAP);
        return mApiService.getBalances(nonce, apiKey, signatureEncoded).map(KrakenBalancesResponse::getBalances);
    }

    private static byte[] concat(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

}
