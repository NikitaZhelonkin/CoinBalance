package ru.nikitazhelonkin.coinbalance.domain;


import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.ExportManager;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeService;
import ru.nikitazhelonkin.coinbalance.data.entity.ImportExportData;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.data.repository.ExchangeRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.WalletRepository;

public class SettingsInteractor {

    private WalletRepository mWalletRepository;
    private ExchangeRepository mExchangeRepository;

    private ExportManager mExportManager;

    @Inject
    public SettingsInteractor(WalletRepository walletRepository,
                              ExchangeRepository exchangeRepository,
                              ExportManager exportManager) {
        mWalletRepository = walletRepository;
        mExchangeRepository = exchangeRepository;
        mExportManager = exportManager;
    }

    public Single<String> exportConfig() {
        return Single.zip(mWalletRepository.getWallets(), mExchangeRepository.getExchanges(), (wallets, exchanges) -> {
            File file = mExportManager.exportData(ImportExportData.from(wallets, exchanges));
            if (file != null) {
                return file.getPath();
            } else {
                throw new RuntimeException("Error export");
            }
        });
    }

    public Completable importConfig(Uri uri) {
        return Completable.fromAction(() -> {
            ImportExportData data = mExportManager.importData(uri);
            if (data != null) {
                //import wallets
                for (ImportExportData.WalletData walletData : data.getWallets()) {
                    Wallet wallet = new Wallet(walletData.coin, walletData.address, walletData.alias);
                    try {
                        mWalletRepository.insert(wallet, false).blockingAwait();
                    } catch (SQLiteConstraintException e) {
                        //ignore duplicate error
                    }
                }

                //import exchanges
                for (ImportExportData.ExchangeData exchangeData : data.getExchanges()) {
                    ExchangeService service = ExchangeService.forName(exchangeData.serviceName);
                    if (service == null)
                        continue;
                    Exchange exchange = new Exchange(service, exchangeData.apiKey, exchangeData.apiSecret, exchangeData.title);
                    try {
                        mExchangeRepository.insert(exchange, false).blockingAwait();
                    } catch (SQLiteConstraintException e) {
                        //ignore duplicate error
                    }
                }
                mExchangeRepository.notifyInsert();
                mWalletRepository.notifyInsert();
            } else {
                throw new RuntimeException("Error import");
            }
        });
    }
}
