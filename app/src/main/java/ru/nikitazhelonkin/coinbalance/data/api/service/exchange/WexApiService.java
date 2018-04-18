package ru.nikitazhelonkin.coinbalance.data.api.service.exchange;


import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.nikitazhelonkin.coinbalance.data.api.response.WexBalanceResponse;

public interface WexApiService {

    @POST("tapi")
    @FormUrlEncoded
    Single<WexBalanceResponse> request(
            @Field("method") String method,
            @Field("nonce") String nonce,
            @Header("Key") String apiKey,
            @Header("Sign") String signature);
}
