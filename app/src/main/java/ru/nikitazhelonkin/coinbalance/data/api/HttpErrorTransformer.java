package ru.nikitazhelonkin.coinbalance.data.api;


import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import retrofit2.HttpException;
import ru.nikitazhelonkin.coinbalance.data.exception.NoPermissionException;

public class HttpErrorTransformer<T> implements SingleTransformer<T, T> {


    public HttpErrorTransformer() {
    }

    @Override
    public SingleSource<T> apply(Single<T> single) {
        return single.onErrorResumeNext(throwable -> {
            if (throwable instanceof HttpException)
                if (((HttpException) throwable).code() == 403)
                    return Single.error(new NoPermissionException());
            return Single.error(throwable);
        });
    }
}