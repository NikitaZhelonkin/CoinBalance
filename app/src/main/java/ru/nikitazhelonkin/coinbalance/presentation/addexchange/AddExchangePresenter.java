package ru.nikitazhelonkin.coinbalance.presentation.addexchange;


import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.exception.ServiceNotSupportedException;
import ru.nikitazhelonkin.coinbalance.data.system.SystemManager;
import ru.nikitazhelonkin.coinbalance.domain.AddExchangeInteractor;
import ru.nikitazhelonkin.coinbalance.mvp.MvpBasePresenter;
import ru.nikitazhelonkin.coinbalance.utils.L;
import ru.nikitazhelonkin.coinbalance.utils.rx.scheduler.RxSchedulerProvider;

public class AddExchangePresenter extends MvpBasePresenter<AddExchangeView> {

    private AddExchangeInteractor mAddExchangeInteractor;
    private RxSchedulerProvider mRxSchedulerProvider;
    private SystemManager mSystemManager;

    @Inject
    public AddExchangePresenter(AddExchangeInteractor addExchangeInteractor,
                              RxSchedulerProvider rxSchedulerProvider,
                              SystemManager systemManager) {
        mAddExchangeInteractor = addExchangeInteractor;
        mRxSchedulerProvider = rxSchedulerProvider;
        mSystemManager = systemManager;
    }

    @Override
    public void onAttach(AddExchangeView view, @Nullable Bundle savedInstanceState) {
        super.onAttach(view, savedInstanceState);
        loadServices();
    }

    private void loadServices() {
        Disposable disposable = mAddExchangeInteractor.getExchangeServices()
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(serviceList -> getView().setupServices(serviceList));
        disposeOnDetach(disposable);
    }

    public void onSubmitClick(Exchange exchange) {
        getView().showAgreement(exchange);
    }

    public void onAgreeClick(Exchange exchange){
        addExchange(exchange);
    }

    private void addExchange(Exchange exchange) {
        getView().setSubmitEnabled(false);
        Disposable disposable = mAddExchangeInteractor.addExchange(exchange)
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(this::onSuccess, this::onError);
        disposeOnDetach(disposable);
    }

    private void onSuccess() {
        getView().setSubmitEnabled(true);
        getView().showMessage(R.string.success_add_exchange);
        getView().exit();
    }

    private void onError(Throwable throwable) {
        getView().setSubmitEnabled(true);
        if (throwable instanceof ServiceNotSupportedException) {
            getView().showMessage(R.string.error_service_not_supported);
        } else if (throwable instanceof SQLiteConstraintException) {
            getView().showMessage(R.string.error_add_exchange_already_exist);
        } else if (!mSystemManager.isConnected()) {
            getView().showMessage(R.string.error_connection);
        } else {
            getView().showMessage(R.string.error_unknown);
        }
        L.e(throwable);
    }
}
