package ru.nikitazhelonkin.coinbalance.data.api.client;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.ETHApiService;

public class ETHApiClient implements ApiClient {

    private ETHApiService mETHApiService;

    public ETHApiClient(ETHApiService ethApiService) {
        mETHApiService = ethApiService;
    }

    @Override
    public Single<String> getBalance(String address) {
        return mETHApiService.balance(address).map(ethResponse -> convert(ethResponse.result));
    }

    private String convert(String s) {
        return String.valueOf(Double.parseDouble(s) / (1000000000000000000d));
    }
}
