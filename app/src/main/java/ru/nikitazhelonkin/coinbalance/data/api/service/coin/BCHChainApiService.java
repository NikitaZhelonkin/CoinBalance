package ru.nikitazhelonkin.coinbalance.data.api.service.coin;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.nikitazhelonkin.coinbalance.data.api.response.BCHChainResponse;

public interface BCHChainApiService {

    @GET("v3/address/{address}")
    Single<BCHChainResponse> balance(@Path("address") String address);
}
