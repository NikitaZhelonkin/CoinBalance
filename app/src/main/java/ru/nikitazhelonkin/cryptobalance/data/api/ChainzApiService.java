package ru.nikitazhelonkin.cryptobalance.data.api;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChainzApiService {

    @GET("{ticker}/api.dws?q=getbalance")
    Single<ResponseBody> balance(@Path("ticker") String coin, @Query("a") String address);
}
