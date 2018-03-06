package ru.nikitazhelonkin.coinbalance.utils.rx.scheduler;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nikita on 17.10.17.
 */

public class RxSchedulerProvider {

    @Inject
    public RxSchedulerProvider() {

    }

    public final <T> SchedulerTransformer<T> ioToMainTransformer() {
        return new SchedulerTransformer<>(io(), main());
    }

    public Scheduler main() {
        return AndroidSchedulers.mainThread();
    }

    public Scheduler io() {
        return Schedulers.io();
    }
}
