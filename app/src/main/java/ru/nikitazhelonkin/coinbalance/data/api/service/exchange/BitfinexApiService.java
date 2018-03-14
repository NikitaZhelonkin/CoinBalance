package ru.nikitazhelonkin.coinbalance.data.api.service.exchange;


import io.reactivex.Single;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.nikitazhelonkin.coinbalance.data.api.response.BitfinexBalancesResponse;

public interface BitfinexApiService {


    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("/v1/balances")
    Single<BitfinexBalancesResponse> balances(@Header("X-BFX-APIKEY") String apiKey,
                                              @Header("X-BFX-PAYLOAD") String payload,
                                              @Header("X-BFX-SIGNATURE") String signature);
}
