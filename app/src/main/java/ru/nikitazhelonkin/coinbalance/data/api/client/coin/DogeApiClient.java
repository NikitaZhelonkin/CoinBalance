package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import okhttp3.ResponseBody;
import ru.nikitazhelonkin.coinbalance.data.api.response.DogeApiService;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletBalance;

public class DogeApiClient implements ApiClient {

    private DogeApiService mApiService;

    public DogeApiClient(DogeApiService apiService){
        mApiService = apiService;
    }
    @Override
    public Single<WalletBalance> getBalance(String address) {
        return mApiService.balance(address)
                .map(ResponseBody::string)
                .map(WalletBalance::new);
    }
}
