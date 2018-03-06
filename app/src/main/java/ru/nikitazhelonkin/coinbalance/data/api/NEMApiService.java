package ru.nikitazhelonkin.coinbalance.data.api;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nikitazhelonkin.coinbalance.data.api.response.NEMResponse;

public interface NEMApiService {

    @GET("api3/account_net")
    Single<NEMResponse> balance(@Query("address") String address);

}
