package ru.nikitazhelonkin.cryptobalance.mvp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class MvpActivity<P extends MvpPresenter<V>, V extends MvpView> extends AppCompatActivity
        implements MvpView {

    private P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        createPresenter(savedInstanceState);
        attachView(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        detachView();
        destroyPresenter();
        super.onDestroy();
    }

    private void createPresenter(@Nullable Bundle savedInstanceState) {
        mPresenter = onCreatePresenter();
        mPresenter.onCreate(savedInstanceState);
    }

    private void destroyPresenter() {
        mPresenter.onDestroy();
        mPresenter = null;
    }

    private void attachView(@Nullable Bundle savedInstanceState) {
        mPresenter.onAttach(getPresenterView(), savedInstanceState);
    }

    private void detachView() {
        mPresenter.onDetach();
    }

    public P getPresenter() {
        return mPresenter;
    }

    @SuppressWarnings("unchecked")
    protected V getPresenterView() {
        return (V) this;
    }

    protected abstract P onCreatePresenter();
}