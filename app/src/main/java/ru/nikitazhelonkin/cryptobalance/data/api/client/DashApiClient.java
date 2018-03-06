package ru.nikitazhelonkin.cryptobalance.data.api.client;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import ru.nikitazhelonkin.cryptobalance.data.api.ChainzApiService;

public class DashApiClient implements ApiClient {

    private ChainzApiService mChainzApiService;

    public DashApiClient(ChainzApiService chainzApiService) {
        mChainzApiService = chainzApiService;
    }

    @Override
    public Single<String> getBalance(String address) {
        return mChainzApiService.balance("dash", address).map(ResponseBody::string);
    }

}
