package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ChainzApiService;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletBalance;

public class LTCApiClient implements ApiClient {

    private ChainzApiService mChainzApiService;

    public LTCApiClient(ChainzApiService chainzApiService) {
        mChainzApiService = chainzApiService;
    }

    @Override
    public Single<WalletBalance> getBalance(String address) {
        return mChainzApiService.balance("ltc", address)
                .map(ResponseBody::string)
                .map(WalletBalance::new);
    }

}
