package ru.nikitazhelonkin.cryptobalance.data.api;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nikitazhelonkin.cryptobalance.data.api.response.Prices;

public interface CryptoCompareApiService {


    @GET("data/pricemulti")
    Single<Prices> getPrices(@Query("fsyms") String fsyms, @Query("tsyms") String tsyms);


}
