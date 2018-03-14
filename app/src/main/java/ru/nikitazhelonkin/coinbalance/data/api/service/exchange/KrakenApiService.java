package ru.nikitazhelonkin.coinbalance.data.api.service.exchange;


import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.nikitazhelonkin.coinbalance.data.api.response.KrakenBalancesResponse;

public interface KrakenApiService {

    @POST("0/private/Balance")
    @FormUrlEncoded
    Single<KrakenBalancesResponse> getBalances(@Field("nonce") String nonce,
                                               @Header("API-Key") String apiKey,
                                               @Header("API-Sign") String apiSign);
}
