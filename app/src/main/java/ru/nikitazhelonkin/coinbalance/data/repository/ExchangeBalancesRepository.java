package ru.nikitazhelonkin.coinbalance.data.repository;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.db.AppDatabase;
import ru.nikitazhelonkin.coinbalance.data.db.ExchangeBalancesDao;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeBalance;

@Singleton
public class ExchangeBalancesRepository extends ObservableRepository {

    private ExchangeBalancesDao mDao;

    @Inject
    public ExchangeBalancesRepository(AppDatabase appDatabase) {
        mDao = appDatabase.exchangeBalancesDao();
    }

    public Single<List<ExchangeBalance>> getBalances() {
        return mDao.getBalances();
    }

    public Single<List<ExchangeBalance>> getBalances(int exchangeId) {
        return mDao.getBalances(exchangeId);
    }

    public Completable insert(List<ExchangeBalance> exchangeBalances) {
        return Completable.fromAction(() -> mDao.insert(exchangeBalances));
    }

    public Completable delete(int exchangeId) {
        return Completable.fromAction(() -> mDao.delete(exchangeId));
    }
}
