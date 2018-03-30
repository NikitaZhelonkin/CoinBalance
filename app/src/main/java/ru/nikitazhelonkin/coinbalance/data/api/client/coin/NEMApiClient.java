package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.NEMApiService;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletBalance;

public class NEMApiClient implements ApiClient {

    public NEMApiService mApiService;

    public NEMApiClient(NEMApiService apiService){
        mApiService = apiService;
    }

    @Override
    public Single<WalletBalance> getBalance(String address) {
        return mApiService.balance(address)
                .map(nemResponse -> convert(nemResponse.account.balance))
                .map(WalletBalance::new);
    }

    private String convert(String s) {
        return String.valueOf(Double.parseDouble(s) / (1000000d));
    }
}
