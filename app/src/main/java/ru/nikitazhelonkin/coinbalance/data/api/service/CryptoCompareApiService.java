package ru.nikitazhelonkin.coinbalance.data.api.service;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nikitazhelonkin.coinbalance.data.api.response.Prices;

public interface CryptoCompareApiService {


    @GET("data/pricemultifull")
    Single<Prices> getPrices(@Query("fsyms") String fsyms, @Query("tsyms") String tsyms);


}
