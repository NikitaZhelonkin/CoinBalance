package ru.nikitazhelonkin.cryptobalance.data.api;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nikitazhelonkin.cryptobalance.data.api.response.ETCResponse;

public interface ETCApiService {

    @GET("api/v1/getAddressBalance")
    Single<ETCResponse> balance(@Query("address") String address);
}
