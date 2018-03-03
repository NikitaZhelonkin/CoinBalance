package ru.nikitazhelonkin.cryptobalance.data.api.client;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import ru.nikitazhelonkin.cryptobalance.data.api.ChainsoApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ChainzApiService;

public class LTCApiClient implements ApiClient {

    private ChainzApiService mChainzApiService;

    public LTCApiClient(ChainzApiService chainzApiService) {
        mChainzApiService = chainzApiService;
    }

    @Override
    public Single<String> getBalance(String address) {
        return mChainzApiService.balance("ltc", address).map(ResponseBody::string);
    }

}
