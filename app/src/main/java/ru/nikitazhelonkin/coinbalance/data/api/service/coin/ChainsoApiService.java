package ru.nikitazhelonkin.coinbalance.data.api.service.coin;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.nikitazhelonkin.coinbalance.data.api.response.ChainsoResponse;

public interface ChainsoApiService {

    @GET("api/v2/get_address_balance/{ticker}/{address}")
    Single<ChainsoResponse> balance(@Path("ticker") String ticker, @Path("address") String address);
}
