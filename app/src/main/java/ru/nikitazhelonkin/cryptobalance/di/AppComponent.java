package ru.nikitazhelonkin.cryptobalance.di;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.nikitazhelonkin.cryptobalance.data.api.BCHChainApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.BTCApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ChainsoApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ChainzApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.CryptoCompareApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ETCApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ETHApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.NEMApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.XLMApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.XRPApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.response.DogeApiService;
import ru.nikitazhelonkin.cryptobalance.data.db.AppDatabase;
import ru.nikitazhelonkin.cryptobalance.data.prefs.Prefs;
import ru.nikitazhelonkin.cryptobalance.data.repository.CoinRepository;
import ru.nikitazhelonkin.cryptobalance.data.repository.WalletRepository;

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
