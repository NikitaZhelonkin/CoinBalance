package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.BCHChainApiService;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletBalance;

public class BCHApiClient implements ApiClient {

    private BCHChainApiService mApiService;

    public BCHApiClient(BCHChainApiService apiService){
        mApiService = apiService;
    }

    @Override
    public Single<WalletBalance> getBalance(String address) {
        return mApiService.balance(address)
                .map(bchChainResponse -> convert(bchChainResponse.getData().balance))
                .map(WalletBalance::new);
    }

    private String convert(String s) {
        return String.valueOf(Double.parseDouble(s) / (100000000d));
    }
}
