package ru.nikitazhelonkin.cryptobalance.data.api.response;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DogeApiService {

    @GET("chain/Dogecoin/q/addressbalance/{address}")
    public Single<ResponseBody> balance(@Path("address") String address);
}
