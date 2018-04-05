package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ZChainApiService;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletBalance;

public class ZECApiClient implements ApiClient {

    private ZChainApiService mApiService;

    public ZECApiClient(ZChainApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Single<WalletBalance> getBalance(String address) {
        return mApiService.balance(address)
                .map(response -> response.balance)
                .map(WalletBalance::new);
    }
}
