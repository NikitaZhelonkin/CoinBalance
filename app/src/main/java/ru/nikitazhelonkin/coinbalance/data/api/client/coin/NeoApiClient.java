package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.NeoScanApiService;

public class NeoApiClient implements ApiClient {

    private NeoScanApiService mApiService;

    public NeoApiClient(NeoScanApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Single<String> getBalance(String address) {
        return mApiService.balance(address).map(response -> response.getBalance("NEO"));
    }
}
