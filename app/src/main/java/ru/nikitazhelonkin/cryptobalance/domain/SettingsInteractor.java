package ru.nikitazhelonkin.cryptobalance.domain;


import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.nikitazhelonkin.cryptobalance.data.ExportManager;
import ru.nikitazhelonkin.cryptobalance.data.entity.ImportExportData;
import ru.nikitazhelonkin.cryptobalance.data.entity.Wallet;
import ru.nikitazhelonkin.cryptobalance.data.repository.WalletRepository;

public class SettingsInteractor {

    private WalletRepository mWalletRepository;

    private ExportManager mExportManager;

    @Inject
    public SettingsInteractor(WalletRepository walletRepository, ExportManager exportManager) {
        mWalletRepository = walletRepository;
        mExportManager = exportManager;
    }

    public Single<String> exportConfig() {
        return mWalletRepository.getWallets()
                .map(wallets -> {
                    File file = mExportManager.exportData(ImportExportData.fromWallets(wallets));
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
                for (ImportExportData.WalletData walletData : data.getData()) {
                    Wallet wallet = new Wallet(walletData.coin, walletData.address, walletData.alias);
                    try {
                        mWalletRepository.insert(wallet, false).blockingAwait();
                    } catch (SQLiteConstraintException e) {
                        //ignore duplicate error
                    }
                }
                mWalletRepository.notifyInsert();
            } else {
                throw new RuntimeException("Error import");
            }
        });
    }
}
