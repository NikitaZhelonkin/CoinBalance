package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import javax.inject.Inject;

import ru.nikitazhelonkin.coinbalance.data.api.response.DogeApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.BCHChainApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.BTCApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ChainsoApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ChainzApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ETCApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ETHApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.EthplorerApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.NEMApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.XLMApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.XRPApiService;

public class ApiClientProvider {


    private BTCApiService mBTCApiService;
    private ETHApiService mETHApiService;
    private EthplorerApiService mEthplorerApiService;
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
                             EthplorerApiService ethplorerApiService,
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
        mEthplorerApiService = ethplorerApiService;
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
                return new ETHApiClient(mEthplorerApiService);
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
