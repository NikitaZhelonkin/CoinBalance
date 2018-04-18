package ru.nikitazhelonkin.coinbalance.data.api.service.exchange;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.nikitazhelonkin.coinbalance.data.api.response.YoBitBalanceResponse;

public interface YoBitApiService {

    @POST("tapi")
    @FormUrlEncoded
    Single<YoBitBalanceResponse> request(
            @Field("method") String method,
            @Field("nonce") String nonce,
            @Header("Key") String apiKey,
            @Header("Sign") String signature);
}
