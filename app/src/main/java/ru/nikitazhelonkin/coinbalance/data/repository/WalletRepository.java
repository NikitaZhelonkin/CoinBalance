package ru.nikitazhelonkin.coinbalance.data.repository;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.db.AppDatabase;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;

@Singleton
public class WalletRepository extends ObservableRepository {

    private AppDatabase mAppDatabase;

    @Inject
    public WalletRepository(AppDatabase database) {
        mAppDatabase = database;
    }

    public Single<List<Wallet>> getWallets() {
        return mAppDatabase.walletDao().getAll();
    }

    public Single<Wallet> getById(int walletId) {
        return mAppDatabase.walletDao().getById(walletId);
    }

    public Completable insert(Wallet wallet, boolean notify) {
        return Completable.fromAction(() -> mAppDatabase.walletDao().insert(wallet))
                .doOnComplete(() -> {
                    if (notify) notifyInsert();
                });
    }

    public Completable update(List<Wallet> wallets, boolean notify) {
        return Observable.fromIterable(wallets)
                .flatMapCompletable(wallet -> Completable.fromAction(() -> mAppDatabase.walletDao().update(wallet)))
                .doOnComplete(() -> {
                    if (notify) notifyChange();
                });
    }

    public Completable update(Wallet wallet, boolean notify) {
        return Completable.fromAction(() -> mAppDatabase.walletDao().update(wallet))
                .doOnComplete(() -> {
                    if (notify) notifyChange();
                });
    }

    public Completable delete(Wallet wallet, boolean notify) {
        return Completable.fromAction(() -> mAppDatabase.walletDao().delete(wallet))
                .doOnComplete(() -> {
                    if (notify) notifyDelete();
                });
    }

}
