package ru.nikitazhelonkin.cryptobalance.data.api;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.nikitazhelonkin.cryptobalance.data.api.response.XLMResponse;

public interface XLMApiService {

    @GET("accounts/{address}")
    Single<XLMResponse> balance(@Path("address") String address);
}
