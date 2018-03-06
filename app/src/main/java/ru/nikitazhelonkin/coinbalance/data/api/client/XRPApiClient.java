package ru.nikitazhelonkin.coinbalance.data.api.client;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.XRPApiService;

public class XRPApiClient implements ApiClient {

    private XRPApiService mXRPApiService;

    public XRPApiClient(XRPApiService xrpApiService) {
        mXRPApiService = xrpApiService;
    }


    @Override
    public Single<String> getBalance(String address) {
        return mXRPApiService.balance(address).map(xrpResponse -> xrpResponse.accountData.balance);
    }
}
