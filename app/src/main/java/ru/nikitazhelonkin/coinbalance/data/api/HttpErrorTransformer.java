package ru.nikitazhelonkin.coinbalance.data.api;


import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import retrofit2.HttpException;
import retrofit2.Response;
import ru.nikitazhelonkin.coinbalance.data.exception.ApiError;

public class HttpErrorTransformer<T> implements SingleTransformer<T, T> {


    public HttpErrorTransformer() {
    }

    @Override
    public SingleSource<T> apply(Single<T> single) {
        return single.onErrorResumeNext(throwable -> {
            if (throwable instanceof HttpException) {
                Response<?> response = ((HttpException) throwable).response();
                if (response != null) {
                    return Single.error(new ApiError(response.errorBody().string()));
                }
            }
            return Single.error(throwable);
        });
    }
}