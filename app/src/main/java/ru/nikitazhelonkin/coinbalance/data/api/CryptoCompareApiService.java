package ru.nikitazhelonkin.coinbalance.data.api;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nikitazhelonkin.coinbalance.data.api.response.Prices;

public interface CryptoCompareApiService {


    @GET("data/pricemulti")
    Single<Prices> getPrices(@Query("fsyms") String fsyms, @Query("tsyms") String tsyms);


}
