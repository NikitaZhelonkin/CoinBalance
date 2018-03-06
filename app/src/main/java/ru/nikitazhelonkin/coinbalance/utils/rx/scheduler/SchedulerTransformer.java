package ru.nikitazhelonkin.coinbalance.utils.rx.scheduler;

import org.reactivestreams.Publisher;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import ru.nikitazhelonkin.coinbalance.utils.rx.BaseTransformer;

/**
 * Created by nikita on 11.10.17.
 */

public class SchedulerTransformer<T> implements BaseTransformer<T> {

    private Scheduler mSubscribeScheduler;
    private Scheduler mObserveScheduler;

    public SchedulerTransformer(Scheduler subscribeScheduler, Scheduler observeScheduler) {
        mSubscribeScheduler = subscribeScheduler;
        mObserveScheduler = observeScheduler;
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return upstream
                .subscribeOn(mSubscribeScheduler)
                .observeOn(mObserveScheduler);
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream
                .subscribeOn(mSubscribeScheduler)
                .observeOn(mObserveScheduler);
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return upstream
                .subscribeOn(mSubscribeScheduler)
                .observeOn(mObserveScheduler);
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .subscribeOn(mSubscribeScheduler)
                .observeOn(mObserveScheduler);
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return upstream
                .subscribeOn(mSubscribeScheduler)
                .observeOn(mObserveScheduler);
    }
}
