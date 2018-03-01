package ru.nikitazhelonkin.cryptobalance.presentation.main;


import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import ru.nikitazhelonkin.cryptobalance.data.entity.WalletViewModel;
import ru.nikitazhelonkin.cryptobalance.domain.MainInteractor;
import ru.nikitazhelonkin.cryptobalance.mvp.MvpBasePresenter;
import ru.nikitazhelonkin.cryptobalance.utils.L;
import ru.nikitazhelonkin.cryptobalance.utils.rx.scheduler.RxSchedulerProvider;

public class MainPresenter extends MvpBasePresenter<MainView> {

    private MainInteractor mMainInteractor;
    private RxSchedulerProvider mRxSchedulerProvider;

    private List<WalletViewModel> mData;

    @Inject
    public MainPresenter(MainInteractor mainInteractor, RxSchedulerProvider rxSchedulerProvider) {
        mMainInteractor = mainInteractor;
        mRxSchedulerProvider = rxSchedulerProvider;
    }

    @Override
    public void onAttach(MainView view, @Nullable Bundle savedInstanceState) {
        super.onAttach(view, savedInstanceState);
        observe();
        loadData();
    }

    public void onRefresh() {
        loadData();
    }

    private void observe() {
        Disposable disposable = mMainInteractor.observe()
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(o -> loadData());
        disposeOnDetach(disposable);
    }

    private void loadData() {
        if (mData == null) {
            getView().showLoader();
        }
        Disposable disposable = mMainInteractor.getData()
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(this::onResult, this::onError);
        disposeOnDetach(disposable);
    }

    public void onAddClick() {
        getView().navigateToAddWalletView();
    }

    private void onResult(List<WalletViewModel> data) {
        mData = data;
        getView().hideLoader();
        getView().setData(data);
    }

    private void onError(Throwable e) {
        getView().hideLoader();
        getView().showError(e.getMessage());
        L.e(e);
    }
}
