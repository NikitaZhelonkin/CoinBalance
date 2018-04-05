package ru.nikitazhelonkin.coinbalance.data.repository;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.db.AppDatabase;
import ru.nikitazhelonkin.coinbalance.data.db.ExchangeDao;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;

@Singleton
public class ExchangeRepository extends ObservableRepository {

    private ExchangeDao mDao;

    @Inject
    public ExchangeRepository(AppDatabase appDatabase) {
        mDao = appDatabase.exchangeDao();
    }

    public Single<List<Exchange>> getExchanges() {
        return mDao.getAll();
    }

    public Single<Exchange> getById(int exchangeId){
        return mDao.getById(exchangeId);
    }

    public Completable insert(Exchange exchange, boolean notify) {
        return Completable.fromAction(() -> mDao.insert(exchange))
                .doOnComplete(() -> {
                    if (notify) notifyInsert();
                });
    }

    public Completable update(Exchange exchange, boolean notify) {
        return Completable.fromAction(() -> mDao.update(exchange))
                .doOnComplete(() -> {
                    if (notify) notifyChange();
                });
    }

    public Completable update(List<Exchange> exchanges, boolean notify) {
        return Observable.fromIterable(exchanges)
                .flatMapCompletable(e -> Completable.fromAction(() -> mDao.update(e)))
                .doOnComplete(() -> {
                    if (notify) notifyChange();
                });
    }

    public Completable delete(Exchange exchange, boolean notify) {
        return Completable.fromAction(() -> mDao.delete(exchange))
                .doOnComplete(() -> {
                    if (notify) notifyDelete();
                });
    }
}
