package ru.nikitazhelonkin.cryptobalance.data.repository;


import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import ru.nikitazhelonkin.cryptobalance.data.db.AppDatabase;
import ru.nikitazhelonkin.cryptobalance.data.entity.Wallet;

public class WalletRepository {

    private static final Subject<Class<?>> ON_CHANGE = PublishSubject.create();

    private AppDatabase mAppDatabase;

    public WalletRepository(AppDatabase database) {
        mAppDatabase = database;
    }

    public Observable<Class<?>> observe() {
        return ON_CHANGE.filter(aClass -> aClass.equals(getClass()));
    }

    public Single<List<Wallet>> getWallets() {
        return mAppDatabase.userDao().getAll()
                .toObservable()
                .flatMap(Observable::fromIterable)
                .toSortedList(Wallet::compareTo);
    }

    public Completable insert(Wallet wallet, boolean notify) {
        return Completable.fromAction(() -> mAppDatabase.userDao().insert(wallet))
                .doOnComplete(() -> {
                    if (notify) notifyChange();
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
                    if (notify) notifyChange();
                });
    }


    public void notifyChange() {
        ON_CHANGE.onNext(getClass());
    }

}
