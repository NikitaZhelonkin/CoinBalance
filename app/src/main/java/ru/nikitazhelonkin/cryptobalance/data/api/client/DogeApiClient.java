package ru.nikitazhelonkin.cryptobalance.data.api.client;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import ru.nikitazhelonkin.cryptobalance.data.api.response.DogeApiService;

public class DogeApiClient implements ApiClient {

    private DogeApiService mApiService;

    public DogeApiClient(DogeApiService apiService){
        mApiService = apiService;
    }
    @Override
    public Single<String> getBalance(String address) {
        return mApiService.balance(address).map(ResponseBody::string);
    }
}
