package ru.nikitazhelonkin.cryptobalance.di;


import javax.inject.Singleton;

import dagger.Component;
import ru.nikitazhelonkin.cryptobalance.data.api.BTCApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ChainsoApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ETHApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.XRPApiService;
import ru.nikitazhelonkin.cryptobalance.data.db.AppDatabase;
import ru.nikitazhelonkin.cryptobalance.data.repository.CoinRepository;
import ru.nikitazhelonkin.cryptobalance.data.repository.WalletRepository;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    AppDatabase appDatabase();

    BTCApiService btcApiService();

    ETHApiService ethApiService();

    ChainsoApiService chainsoApiService();

    XRPApiService xrpApiService();

    WalletRepository walletRepository();

    CoinRepository coinRepository();

}
