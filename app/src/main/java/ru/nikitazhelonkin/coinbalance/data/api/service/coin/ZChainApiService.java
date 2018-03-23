package ru.nikitazhelonkin.coinbalance.data.api.service.coin;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.nikitazhelonkin.coinbalance.data.api.response.ZChainBalanceResponse;

public interface ZChainApiService {

    @GET("v2/mainnet/accounts/{address}")
    Single<ZChainBalanceResponse> balance(@Path("address") String address);

}
