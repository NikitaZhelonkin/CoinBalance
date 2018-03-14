package ru.nikitazhelonkin.coinbalance.data.api.service.coin;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nikitazhelonkin.coinbalance.data.api.response.EthResponse;

public interface ETHApiService {

    @GET("api?module=account&action=balance")
    Single<EthResponse> balance(@Query("address") String address);
}
