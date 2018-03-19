package ru.nikitazhelonkin.coinbalance.data.api.service.coin;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nikitazhelonkin.coinbalance.data.api.response.EthplorerApiResponse;

public interface EthplorerApiService {

    @GET("getAddressInfo/{address}")
    Single<EthplorerApiResponse> balance(@Path("address") String address, @Query("apiKey") String apiKey);
}
