package ru.nikitazhelonkin.coinbalance.data.api.client;


import javax.inject.Inject;

import ru.nikitazhelonkin.coinbalance.data.api.BCHChainApiService;
import ru.nikitazhelonkin.coinbalance.data.api.BTCApiService;
import ru.nikitazhelonkin.coinbalance.data.api.ChainsoApiService;
import ru.nikitazhelonkin.coinbalance.data.api.ChainzApiService;
import ru.nikitazhelonkin.coinbalance.data.api.ETCApiService;
import ru.nikitazhelonkin.coinbalance.data.api.ETHApiService;
import ru.nikitazhelonkin.coinbalance.data.api.NEMApiService;
import ru.nikitazhelonkin.coinbalance.data.api.XLMApiService;
import ru.nikitazhelonkin.coinbalance.data.api.XRPApiService;
import ru.nikitazhelonkin.coinbalance.data.api.response.DogeApiService;

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
