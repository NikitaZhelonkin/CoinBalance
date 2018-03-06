package ru.nikitazhelonkin.coinbalance.data.api.client;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.ETCApiService;

public class ETCApiClient implements ApiClient {


    private ETCApiService mApiService;

    public ETCApiClient(ETCApiService apiService){
        mApiService = apiService;
    }

    @Override
    public Single<String> getBalance(String address) {
        return mApiService.balance(address).map(etcResponse -> etcResponse.balance);
    }
}
