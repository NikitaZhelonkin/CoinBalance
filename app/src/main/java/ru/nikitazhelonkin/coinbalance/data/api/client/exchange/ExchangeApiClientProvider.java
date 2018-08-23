package ru.nikitazhelonkin.coinbalance.data.api.client.exchange;


import javax.inject.Inject;

import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BinanceApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BitfinexApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BittrexApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.CoinbaseApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.GeminiApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.HitBTCApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.KrakenApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.PoloniexApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.WexApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.YoBitApiService;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeService;

public class ExchangeApiClientProvider {

    private BitfinexApiService mBitfinexApiService;
    private BittrexApiService mBittrexApiService;
    private BinanceApiService mBinanceApiService;
    private KrakenApiService mKrakenApiService;
    protected PoloniexApiService mPoloniexApiService;
    private HitBTCApiService mHitBTCApiService;
    private CoinbaseApiService mCoinbaseApiService;
    private GeminiApiService mGeminiApiService;
    private YoBitApiService mYoBitApiService;
    private WexApiService mWexApiService;

    @Inject
    public ExchangeApiClientProvider(BitfinexApiService bitfinexApiService,
                                     BittrexApiService bittrexApiService,
                                     BinanceApiService binanceApiService,
                                     KrakenApiService krakenApiService,
                                     PoloniexApiService poloniexApiService,
                                     HitBTCApiService hitBTCApiService,
                                     CoinbaseApiService coinbaseApiService,
                                     GeminiApiService geminiApiService,
                                     YoBitApiService yoBitApiService,
                                     WexApiService wexApiService) {
        mBitfinexApiService = bitfinexApiService;
        mBittrexApiService = bittrexApiService;
        mBinanceApiService = binanceApiService;
        mKrakenApiService = krakenApiService;
        mPoloniexApiService = poloniexApiService;
        mHitBTCApiService = hitBTCApiService;
        mCoinbaseApiService = coinbaseApiService;
        mGeminiApiService = geminiApiService;
        mYoBitApiService = yoBitApiService;
        mWexApiService = wexApiService;
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
            case GEMINI:
                return new GeminiApiClient(mGeminiApiService);
            case YOBIT:
                return new YoBitApiClient(mYoBitApiService);
            case WEX:
                return new WexApiClient(mWexApiService);
        }
        throw new IllegalArgumentException("Exchange " + service.getTitle() + " not unsupported");
    }
}
