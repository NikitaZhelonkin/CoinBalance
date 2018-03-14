package ru.nikitazhelonkin.coinbalance.data.api.service.coin;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.nikitazhelonkin.coinbalance.data.api.response.XLMResponse;

public interface XLMApiService {

    @GET("accounts/{address}")
    Single<XLMResponse> balance(@Path("address") String address);
}
