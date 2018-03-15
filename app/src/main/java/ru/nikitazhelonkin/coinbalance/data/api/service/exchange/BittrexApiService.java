package ru.nikitazhelonkin.coinbalance.data.api.service.exchange;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import ru.nikitazhelonkin.coinbalance.data.api.response.BittrexBalancesResponse;

public interface BittrexApiService {

    @GET("api/v1.1/account/getbalances")
    Single<BittrexBalancesResponse> balances(@Query("apikey") String apiKey,
                                             @Query("nonce") String nonce,
                                             @Header("apisign") String signature);
}
