package ru.nikitazhelonkin.coinbalance.data.api.service.coin;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nikitazhelonkin.coinbalance.data.api.response.ETCResponse;

public interface ETCApiService {

    @GET("api/v1/getAddressBalance")
    Single<ETCResponse> balance(@Query("address") String address);
}
