package ru.nikitazhelonkin.cryptobalance.data.api.client;


import javax.inject.Inject;

import ru.nikitazhelonkin.cryptobalance.data.api.BTCApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ChainsoApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ChainzApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ETHApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.XRPApiService;

public class ApiClientProvider {


    private BTCApiService mBTCApiService;
    private ETHApiService mETHApiService;
    private ChainsoApiService mChainsoApiService;
    private ChainzApiService mChainzApiService;
    private XRPApiService mXRPApiService;

    @Inject
    public ApiClientProvider(BTCApiService btcApiService,
                             ETHApiService ethApiService,
                             ChainsoApiService chainsoApiService,
                             ChainzApiService chainzApiService,
                             XRPApiService xrpApiService) {
        mBTCApiService = btcApiService;
        mETHApiService = ethApiService;
        mChainsoApiService = chainsoApiService;
        mChainzApiService = chainzApiService;
        mXRPApiService = xrpApiService;
    }

    public ApiClient provide(String coinTicker) {
        if (coinTicker.equals("BTC")) {
            return new BTCApiClient(mBTCApiService);
        } else if (coinTicker.equals("ETH")) {
            return new ETHApiClient(mETHApiService);
        } else if (coinTicker.equals("LTC")) {
            return new LTCApiClient(mChainzApiService);
        } else if (coinTicker.equals("XRP")) {
            return new XRPApiClient(mXRPApiService);
        }
        throw new IllegalArgumentException("Coin " + coinTicker + " is unsupported");
    }
}
