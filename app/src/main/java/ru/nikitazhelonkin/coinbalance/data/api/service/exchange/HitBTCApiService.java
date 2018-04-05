package ru.nikitazhelonkin.coinbalance.data.api.service.exchange;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import ru.nikitazhelonkin.coinbalance.data.api.response.HitBTCBalancesResponse;

public interface HitBTCApiService {

    @GET("/api/2/account/balance")
    Single<HitBTCBalancesResponse> balances(@Header("Authorization") String authorization);
}
