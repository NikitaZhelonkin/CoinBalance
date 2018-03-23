package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.AdaApiService;

public class AdaApiClient implements ApiClient {

    private AdaApiService mApiService;

    public AdaApiClient(AdaApiService apiService){
        mApiService = apiService;
    }

    @Override
    public Single<String> getBalance(String address) {
        return mApiService.balance(address).map(adaBalanceResponse -> convert(adaBalanceResponse.data.balance.value));
    }

    private String convert(String s) {
        return String.valueOf(Double.parseDouble(s) / (1000000d));
    }
}
