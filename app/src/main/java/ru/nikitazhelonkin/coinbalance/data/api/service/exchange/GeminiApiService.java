package ru.nikitazhelonkin.coinbalance.data.api.service.exchange;


import io.reactivex.Single;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.nikitazhelonkin.coinbalance.data.api.response.GeminiBalanceResponse;

public interface GeminiApiService {

    @POST("v1/balances")
    Single<GeminiBalanceResponse> balances(@Header("X-GEMINI-APIKEY") String apiKey,
                                           @Header("X-GEMINI-PAYLOAD") String payload,
                                           @Header("X-GEMINI-SIGNATURE") String signature);
}
