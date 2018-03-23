package ru.nikitazhelonkin.coinbalance.data.api.service.coin;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.nikitazhelonkin.coinbalance.data.api.response.NeoScanBalanceResponse;

public interface NeoScanApiService {

    @GET("api/main_net/v1/get_address/{address}")
    Single<NeoScanBalanceResponse> balance(@Path("address") String address);
}
