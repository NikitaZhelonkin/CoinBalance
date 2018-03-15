package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ChainzApiService;

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
