package ru.nikitazhelonkin.coinbalance.data.api.client.exchange;


import javax.inject.Inject;

import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BinanceApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BitfinexApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BittrexApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.CoinbaseApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.HitBTCApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.KrakenApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.PoloniexApiService;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeService;

public class ExchangeApiClientProvider {

    private BitfinexApiService mBitfinexApiService;
    private BittrexApiService mBittrexApiService;
    private BinanceApiService mBinanceApiService;
    private KrakenApiService mKrakenApiService;
    protected PoloniexApiService mPoloniexApiService;
    private HitBTCApiService mHitBTCApiService;
    private CoinbaseApiService mCoinbaseApiService;

    @Inject
    public ExchangeApiClientProvider(BitfinexApiService bitfinexApiService,
                                     BittrexApiService bittrexApiService,
                                     BinanceApiService binanceApiService,
                                     KrakenApiService krakenApiService,
                                     PoloniexApiService poloniexApiService,
                                     HitBTCApiService hitBTCApiService,
                                     CoinbaseApiService coinbaseApiService) {
        mBitfinexApiService = bitfinexApiService;
        mBittrexApiService = bittrexApiService;
        mBinanceApiService = binanceApiService;
        mKrakenApiService = krakenApiService;
        mPoloniexApiService = poloniexApiService;
        mHitBTCApiService = hitBTCApiService;
        mCoinbaseApiService = coinbaseApiService;
    }

    public ExchangeApiClient provide(ExchangeService service) {
        switch (service) {
            case BITFINEX:
                return new BitfinexApiClient(mBitfinexApiService);
            case BITTREX:
                return new BittrexApiClient(mBittrexApiService);
            case BINANCE:
                return new BinanceApiClient(mBinanceApiService);
            case KRAKEN:
                return new KrakenApiClient(mKrakenApiService);
            case POLONIEX:
                return new PoloniexApiClient(mPoloniexApiService);
            case HITBTC:
                return new HitBTCApiClient(mHitBTCApiService);
            case COINBASE:
                return new CoinbaseApiClient(mCoinbaseApiService);
        }
        throw new IllegalArgumentException("Exchange " + service.getName() + " not unsupported");
    }
}
