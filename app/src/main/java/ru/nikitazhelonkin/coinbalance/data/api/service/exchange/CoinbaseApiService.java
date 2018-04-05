package ru.nikitazhelonkin.coinbalance.data.api.service.exchange;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import ru.nikitazhelonkin.coinbalance.data.api.response.CoinbaseBalanceResponse;
import ru.nikitazhelonkin.coinbalance.data.api.response.CoinbaseTimeResponse;

public interface CoinbaseApiService {

    @GET("/v2/time")
    Single<CoinbaseTimeResponse> getTime();

    @GET("/v2/accounts")
    Single<CoinbaseBalanceResponse> balances(@Header("CB-ACCESS-KEY") String apiKey,
                                             @Header("CB-ACCESS-SIGN") String signature,
                                             @Header("CB-ACCESS-TIMESTAMP") String timestamp);
}
