package ru.nikitazhelonkin.coinbalance.data.api.service.coin;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.nikitazhelonkin.coinbalance.data.api.response.XrpResponse;

public interface XRPApiService {


    @GET("v2/accounts/{address}/balances")
    Single<XrpResponse> balance(@Path("address") String address);
}
