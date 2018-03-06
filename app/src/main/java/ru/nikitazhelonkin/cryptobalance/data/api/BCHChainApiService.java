package ru.nikitazhelonkin.cryptobalance.data.api;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.nikitazhelonkin.cryptobalance.data.api.response.BCHChainResponse;

public interface BCHChainApiService {

    @GET("v3/address/{address}")
    Single<BCHChainResponse> balance(@Path("address") String address);
}
