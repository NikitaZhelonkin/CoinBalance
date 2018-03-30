package ru.nikitazhelonkin.coinbalance.mvp;


import android.os.Bundle;
import android.support.annotation.Nullable;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private V mView;

    private CompositeDisposable mCompositeDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //for inheritance
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //for inheritance
    }

    @Override
    public void onAttach(V view, @Nullable Bundle savedInstanceState) {
        mCompositeDisposable = new CompositeDisposable();
        mView = view;
    }

    @Override
    public void onDetach() {
        dispose();
        mView = null;
    }

    @Override
    public void onDestroy() {
        //for inheritance
    }

    protected V getView() {
        return mView;
    }

    protected void disposeOnDetach(Disposable disposable) {
        if (mCompositeDisposable == null) {
            return;
        }
        if (mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    private void dispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

}
