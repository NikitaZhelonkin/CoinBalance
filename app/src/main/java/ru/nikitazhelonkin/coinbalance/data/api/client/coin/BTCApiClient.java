package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.BTCApiService;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletBalance;

public class BTCApiClient implements ApiClient {


    private BTCApiService mBTCApiService;

    public BTCApiClient(BTCApiService apiService) {
        mBTCApiService = apiService;
    }

    @Override
    public Single<WalletBalance> getBalance(String address) {
        return mBTCApiService.balance(address)
                .map(responseBody -> convert(responseBody.string()))
                .map(WalletBalance::new);
    }

    private String convert(String s) {
        return String.valueOf(Double.parseDouble(s) / (100000000d));
    }
}
