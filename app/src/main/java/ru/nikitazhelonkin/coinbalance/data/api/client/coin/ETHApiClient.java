package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.EthplorerApiService;
import ru.nikitazhelonkin.coinbalance.utils.L;

public class ETHApiClient implements ApiClient {

    private EthplorerApiService mApiService;

    public ETHApiClient(EthplorerApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Single<String> getBalance(String address) {
        return mApiService.balance(address, "freekey")
                .doOnSuccess(ethplorerApiResponse -> L.e(""+ethplorerApiResponse.toString()))
                .map(ethResponse -> ethResponse.ETH.balance);
    }

}
