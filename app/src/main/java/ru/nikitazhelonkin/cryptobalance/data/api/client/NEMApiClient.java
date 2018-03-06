package ru.nikitazhelonkin.cryptobalance.data.api.client;


import io.reactivex.Single;
import ru.nikitazhelonkin.cryptobalance.data.api.NEMApiService;

public class NEMApiClient implements ApiClient {

    public NEMApiService mApiService;

    public NEMApiClient(NEMApiService apiService){
        mApiService = apiService;
    }

    @Override
    public Single<String> getBalance(String address) {
        return mApiService.balance(address).map(nemResponse -> convert(nemResponse.account.balance));
    }

    private String convert(String s) {
        return String.valueOf(Double.parseDouble(s) / (1000000d));
    }
}
