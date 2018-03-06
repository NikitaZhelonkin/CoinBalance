package ru.nikitazhelonkin.cryptobalance.data.api.client;


import javax.inject.Inject;

import ru.nikitazhelonkin.cryptobalance.data.api.BCHChainApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.BTCApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ChainsoApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ChainzApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ETCApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ETHApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.NEMApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.XLMApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.XRPApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.response.DogeApiService;

public class ApiClientProvider {


    private BTCApiService mBTCApiService;
    private ETHApiService mETHApiService;
    private ChainsoApiService mChainsoApiService;
    private ChainzApiService mChainzApiService;
    private XRPApiService mXRPApiService;
    private BCHChainApiService mBCHChainApiService;
    private ETCApiService mETCApiService;
    private DogeApiService mDogeApiService;
    private NEMApiService mNEMApiService;
    private XLMApiService mXLMApiService;

    @Inject
    public ApiClientProvider(BTCApiService btcApiService,
                             ETHApiService ethApiService,
                             ChainsoApiService chainsoApiService,
                             ChainzApiService chainzApiService,
                             XRPApiService xrpApiService,
                             BCHChainApiService bchChainApiService,
                             ETCApiService etcApiService,
                             DogeApiService dogeApiService,
                             NEMApiService nemApiService,
                             XLMApiService xlmApiService) {
        mBTCApiService = btcApiService;
        mETHApiService = ethApiService;
        mChainsoApiService = chainsoApiService;
        mChainzApiService = chainzApiService;
        mXRPApiService = xrpApiService;
        mBCHChainApiService = bchChainApiService;
        mETCApiService = etcApiService;
        mDogeApiService = dogeApiService;
        mNEMApiService = nemApiService;
        mXLMApiService = xlmApiService;
    }

    public ApiClient provide(String coinTicker) {
        switch (coinTicker) {
            case "BTC":
                return new BTCApiClient(mBTCApiService);
            case "ETH":
                return new ETHApiClient(mETHApiService);
            case "LTC":
                return new LTCApiClient(mChainzApiService);
            case "XRP":
                return new XRPApiClient(mXRPApiService);
            case "DASH":
                return new DashApiClient(mChainzApiService);
            case "BCH":
                return new BCHApiClient(mBCHChainApiService);
            case "ETC":
                return new ETCApiClient(mETCApiService);
            case "DOGE":
                return new DogeApiClient(mDogeApiService);
            case "XEM":
                return new NEMApiClient(mNEMApiService);
            case "XLM":
                return new XLMApiClient(mXLMApiService);
        }
        throw new IllegalArgumentException("Coin " + coinTicker + " is unsupported");
    }
}
