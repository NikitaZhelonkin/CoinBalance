package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.GasTrackerApiService;

public class GasTrackerApiClient implements ApiClient {

    private GasTrackerApiService mApiService;

    public GasTrackerApiClient(GasTrackerApiService apiService){
        mApiService = apiService;
    }

    @Override
    public Single<String> getBalance(String address) {
        return mApiService.balance(address).map(response->response.balance.ether);
    }
}
