package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.response.XLMResponse;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.XLMApiService;

public class XLMApiClient implements ApiClient {

    private XLMApiService mApiService;

    public XLMApiClient(XLMApiService apiService){
        mApiService = apiService;
    }
    @Override
    public Single<String> getBalance(String address) {
        return mApiService.balance(address).map(XLMResponse::getNativeBalance);
    }
}
