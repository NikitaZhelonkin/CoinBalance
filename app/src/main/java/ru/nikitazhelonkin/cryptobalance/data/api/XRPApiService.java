package ru.nikitazhelonkin.cryptobalance.data.api;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.nikitazhelonkin.cryptobalance.data.api.response.XrpResponse;

public interface XRPApiService {


    @GET("v2/accounts/{address}")
    Single<XrpResponse> balance(@Path("address") String address);
}
