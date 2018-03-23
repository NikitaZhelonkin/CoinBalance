package ru.nikitazhelonkin.coinbalance.data.api.service.coin;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.nikitazhelonkin.coinbalance.data.api.response.AdaBalanceResponse;

public interface AdaApiService {

    @GET("api/addresses/summary/{address}")
    Single<AdaBalanceResponse> balance(@Path("address") String address);
}
