package ru.nikitazhelonkin.coinbalance.mvp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;

public abstract class MvpDialogFragment<P extends MvpPresenter<V>, V extends MvpView> extends AppCompatDialogFragment
        implements MvpView {

    private P mPresenter;

    private boolean mOnCreate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnCreate = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mOnCreate) {
            createPresenter(savedInstanceState);
            mOnCreate = false;
        }
        attachView(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null) {
            mPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyPresenter();
    }

    private void createPresenter(@Nullable Bundle savedInstanceState){
        mPresenter = onCreatePresenter();
        mPresenter.onCreate(savedInstanceState);
    }

    private void destroyPresenter() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }

    private void attachView(@Nullable Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.onAttach(getPresenterView(), savedInstanceState);
        }
    }

    private void detachView() {
        if (mPresenter != null) {
            mPresenter.onDetach();
        }
    }

    public P getPresenter() {
        return mPresenter;
    }

    @SuppressWarnings("unchecked")
    protected V getPresenterView() {
        return (V) this;
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    protected abstract P onCreatePresenter();
}
