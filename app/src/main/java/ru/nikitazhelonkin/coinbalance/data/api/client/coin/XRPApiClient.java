package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.XRPApiService;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletBalance;

public class XRPApiClient implements ApiClient {

    private XRPApiService mXRPApiService;

    public XRPApiClient(XRPApiService xrpApiService) {
        mXRPApiService = xrpApiService;
    }


    @Override
    public Single<WalletBalance> getBalance(String address) {
        return mXRPApiService.balance(address)
                .map(xrpResponse -> xrpResponse.accountData.balance)
                .map(WalletBalance::new);
    }
}
