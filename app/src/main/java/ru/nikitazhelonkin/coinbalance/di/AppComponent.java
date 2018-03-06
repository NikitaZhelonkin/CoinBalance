package ru.nikitazhelonkin.coinbalance.di;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.nikitazhelonkin.coinbalance.data.api.BCHChainApiService;
import ru.nikitazhelonkin.coinbalance.data.api.BTCApiService;
import ru.nikitazhelonkin.coinbalance.data.api.ChainsoApiService;
import ru.nikitazhelonkin.coinbalance.data.api.ChainzApiService;
import ru.nikitazhelonkin.coinbalance.data.api.CryptoCompareApiService;
import ru.nikitazhelonkin.coinbalance.data.api.ETCApiService;
import ru.nikitazhelonkin.coinbalance.data.api.ETHApiService;
import ru.nikitazhelonkin.coinbalance.data.api.NEMApiService;
import ru.nikitazhelonkin.coinbalance.data.api.XLMApiService;
import ru.nikitazhelonkin.coinbalance.data.api.XRPApiService;
import ru.nikitazhelonkin.coinbalance.data.api.response.DogeApiService;
import ru.nikitazhelonkin.coinbalance.data.db.AppDatabase;
import ru.nikitazhelonkin.coinbalance.data.prefs.Prefs;
import ru.nikitazhelonkin.coinbalance.data.repository.CoinRepository;
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

    ChainzApiService chainzApiService();

    ChainsoApiService chainsoApiService();

    XRPApiService xrpApiService();

    BCHChainApiService bchChainApiService();

    ETCApiService etcApiService();

    DogeApiService dogeApiService();

    NEMApiService nemApiService();

    XLMApiService xlmApiService();

    WalletRepository walletRepository();

    CoinRepository coinRepository();

}
