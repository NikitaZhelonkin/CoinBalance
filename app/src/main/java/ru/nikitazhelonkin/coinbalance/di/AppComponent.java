package ru.nikitazhelonkin.coinbalance.di;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.nikitazhelonkin.coinbalance.data.api.response.DogeApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.CryptoCompareApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.AdaApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.BCHChainApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.BTCApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ChainsoApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ChainzApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ETCApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ETHApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.EthplorerApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.GasTrackerApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.NEMApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.NeoScanApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.XLMApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.XRPApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ZChainApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BinanceApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BitfinexApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BittrexApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.KrakenApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.PoloniexApiService;
import ru.nikitazhelonkin.coinbalance.data.db.AppDatabase;
import ru.nikitazhelonkin.coinbalance.data.prefs.Prefs;
import ru.nikitazhelonkin.coinbalance.data.repository.ExchangeBalancesRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.ExchangeRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.WalletRepository;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    Context context();

    Prefs prefs();

    AppDatabase appDatabase();

    CryptoCompareApiService cryptoCompareApiService();

    BTCApiService btcApiService();

    ETHApiService ethApiService();

    EthplorerApiService ethplorerApiService();

    ChainzApiService chainzApiService();

    ChainsoApiService chainsoApiService();

    XRPApiService xrpApiService();

    BCHChainApiService bchChainApiService();

    ETCApiService etcApiService();

    GasTrackerApiService gastrackerApiService();

    DogeApiService dogeApiService();

    NEMApiService nemApiService();

    XLMApiService xlmApiService();

    AdaApiService adaApiService();

    NeoScanApiService neoScanApiService();

    ZChainApiService zChainApiService();

    BitfinexApiService bitfinexApiService();

    BittrexApiService bittrexApiService();

    BinanceApiService binanceApiService();

    KrakenApiService krakenApiService();

    PoloniexApiService poloniexApiService();

    WalletRepository walletRepository();
    ExchangeRepository exchangeRepository();
    ExchangeBalancesRepository exchangeBalancesRepository();

}
