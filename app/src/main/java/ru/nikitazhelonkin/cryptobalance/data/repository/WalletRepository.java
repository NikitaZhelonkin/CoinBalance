package ru.nikitazhelonkin.cryptobalance.data.repository;


import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.nikitazhelonkin.cryptobalance.data.db.AppDatabase;
import ru.nikitazhelonkin.cryptobalance.data.entity.Wallet;

public class WalletRepository {

    private AppDatabase mAppDatabase;

    public WalletRepository(AppDatabase database) {
        mAppDatabase = database;
    }

    public Flowable<Integer> observe() {
        return mAppDatabase.userDao().observe();
    }

    public Single<List<Wallet>> getWallets() {
        return mAppDatabase.userDao().getAll();
    }

    public Completable insert(Wallet wallet) {
        return Completable.fromAction(() -> mAppDatabase.userDao().insert(wallet));
    }

    public Completable update(Wallet wallet) {
        return Completable.fromAction(() -> mAppDatabase.userDao().update(wallet));
    }

    public Completable delete(Wallet wallet) {
        return Completable.fromAction(() -> mAppDatabase.userDao().delete(wallet));
    }

}
