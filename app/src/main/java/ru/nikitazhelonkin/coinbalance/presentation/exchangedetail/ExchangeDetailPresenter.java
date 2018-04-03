package ru.nikitazhelonkin.coinbalance.presentation.exchangedetail;


import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeDetailViewModel;
import ru.nikitazhelonkin.coinbalance.domain.ExchangeDetailInteractor;
import ru.nikitazhelonkin.coinbalance.mvp.MvpBasePresenter;
import ru.nikitazhelonkin.coinbalance.utils.L;
import ru.nikitazhelonkin.coinbalance.utils.rx.scheduler.RxSchedulerProvider;

public class ExchangeDetailPresenter extends MvpBasePresenter<ExchangeDetailView> {

    private RxSchedulerProvider mSchedulerProvider;
    private ExchangeDetailInteractor mInteractor;
    private int mExchangeId;
    private ExchangeDetailViewModel mModel;

    @Inject
    public ExchangeDetailPresenter(int exchangeId, ExchangeDetailInteractor interactor, RxSchedulerProvider schedulerProvider) {
        mExchangeId = exchangeId;
        mInteractor = interactor;
        mSchedulerProvider = schedulerProvider;
    }

    @Override
    public void onAttach(ExchangeDetailView view, @Nullable Bundle savedInstanceState) {
        super.onAttach(view, savedInstanceState);
        loadData();
    }

    public void editExchangeTitle(Exchange exchange, String title) {
        mInteractor.editExchangeTitle(exchange, title)
                .compose(mSchedulerProvider.ioToMainTransformer())
                .subscribe(this::loadData);
    }

    public void deleteExchange(Exchange exchange) {
        mInteractor.deleteExchange(exchange)
                .compose(mSchedulerProvider.ioToMainTransformer())
                .subscribe(() -> getView().exit());
    }

    public void onEditClick() {
        getView().showEditNameView(mModel.getExchange());
    }

    public void onDeleteClick() {
        getView().showDeleteView(mModel.getExchange());
    }

    private void loadData() {
        Disposable disposable = mInteractor.getData(mExchangeId)
                .compose(mSchedulerProvider.ioToMainTransformer())
                .subscribe(this::onSuccess, this::onError);
        disposeOnDetach(disposable);
    }

    private void onSuccess(ExchangeDetailViewModel model) {
        mModel = model;
        getView().showExchange(model);
    }

    private void onError(Throwable e) {
        L.e(e);
    }
}
