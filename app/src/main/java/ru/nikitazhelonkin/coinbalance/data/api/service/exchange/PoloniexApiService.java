package ru.nikitazhelonkin.coinbalance.data.api.service.exchange;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.nikitazhelonkin.coinbalance.data.api.response.PoloniexBalancesResponse;

public interface PoloniexApiService {

    @POST("tradingApi")
    @FormUrlEncoded
    Single<PoloniexBalancesResponse> balances(
            @Field("nonce") String nonce,
            @Field("command") String command,
            @Header("Key") String apiKey,
            @Header("Sign") String apiSign);
}
