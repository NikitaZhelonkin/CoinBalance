package ru.nikitazhelonkin.cryptobalance.data.api.client;


import io.reactivex.Single;
import ru.nikitazhelonkin.cryptobalance.data.api.ChainsoApiService;

public class LTCApiClient implements ApiClient {

    private ChainsoApiService mChainsoApiService;

    public LTCApiClient(ChainsoApiService chainsoApiService) {
        mChainsoApiService = chainsoApiService;
    }

    @Override
    public Single<String> getBalance(String address) {
        return mChainsoApiService.balance("LTC", address)
                .map(response -> response.data.confirmedBalance);
    }

}
