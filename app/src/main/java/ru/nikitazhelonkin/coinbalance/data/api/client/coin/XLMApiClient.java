package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.response.XLMResponse;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.XLMApiService;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletBalance;

public class XLMApiClient implements ApiClient {

    private XLMApiService mApiService;

    public XLMApiClient(XLMApiService apiService){
        mApiService = apiService;
    }
    @Override
    public Single<WalletBalance> getBalance(String address) {
        return mApiService.balance(address)
                .map(XLMResponse::getNativeBalance)
                .map(WalletBalance::new);
    }
}
