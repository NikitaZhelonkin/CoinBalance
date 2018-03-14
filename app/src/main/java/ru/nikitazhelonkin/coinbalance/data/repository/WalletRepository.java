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
        return mAppDatabase.userDao().getAll();
    }

    public Completable insert(Wallet wallet, boolean notify) {
        return Completable.fromAction(() -> mAppDatabase.userDao().insert(wallet))
                .doOnComplete(() -> {
                    if (notify) notifyInsert();
                });
    }

    public Completable update(List<Wallet> wallets, boolean notify) {
        return Observable.fromIterable(wallets)
                .flatMapCompletable(wallet -> Completable.fromAction(() -> mAppDatabase.userDao().update(wallet)))
                .doOnComplete(() -> {
                    if (notify) notifyChange();
                });
    }

    public Completable update(Wallet wallet, boolean notify) {
        return Completable.fromAction(() -> mAppDatabase.userDao().update(wallet))
                .doOnComplete(() -> {
                    if (notify) notifyChange();
                });
    }

    public Completable delete(Wallet wallet, boolean notify) {
        return Completable.fromAction(() -> mAppDatabase.userDao().delete(wallet))
                .doOnComplete(() -> {
                    if (notify) notifyDelete();
                });
    }

}
