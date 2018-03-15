package ru.nikitazhelonkin.coinbalance.data.api.service.exchange;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import ru.nikitazhelonkin.coinbalance.data.api.response.BinanceBalancesResponse;

public interface BinanceApiService {

    @GET("api/v3/account")
    Single<BinanceBalancesResponse> balances(
            @Query("timestamp") String timeStamp,
            @Query("signature") String signature,
            @Header("X-MBX-APIKEY") String apiKey);
}
