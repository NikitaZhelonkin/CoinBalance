package ru.nikitazhelonkin.coinbalance.data.api.service.coin;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nikitazhelonkin.coinbalance.data.api.response.ETCResponse;
import ru.nikitazhelonkin.coinbalance.data.api.response.GasTrackerBalanceResponse;

public interface GasTrackerApiService {

    @GET("v1/addr/{address}")
    Single<GasTrackerBalanceResponse> balance(@Path("address") String address);

}
