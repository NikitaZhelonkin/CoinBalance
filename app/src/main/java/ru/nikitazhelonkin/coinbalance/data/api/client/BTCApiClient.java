package ru.nikitazhelonkin.coinbalance.data.api.client;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.BTCApiService;

public class BTCApiClient implements ApiClient {


    private BTCApiService mBTCApiService;

    public BTCApiClient(BTCApiService apiService) {
        mBTCApiService = apiService;
    }

    @Override
    public Single<String> getBalance(String address) {
        return mBTCApiService.balance(address).map(responseBody -> convert(responseBody.string()));
    }

    private String convert(String s) {
        return String.valueOf(Double.parseDouble(s) / (100000000d));
    }
}
