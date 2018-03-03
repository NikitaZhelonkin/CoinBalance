package ru.nikitazhelonkin.cryptobalance.presentation.add;


import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import ru.nikitazhelonkin.cryptobalance.R;
import ru.nikitazhelonkin.cryptobalance.data.entity.Wallet;
import ru.nikitazhelonkin.cryptobalance.data.exception.CoinNotSupportedException;
import ru.nikitazhelonkin.cryptobalance.data.system.SystemManager;
import ru.nikitazhelonkin.cryptobalance.domain.AddWalletInteractor;
import ru.nikitazhelonkin.cryptobalance.mvp.MvpBasePresenter;
import ru.nikitazhelonkin.cryptobalance.utils.L;
import ru.nikitazhelonkin.cryptobalance.utils.rx.scheduler.RxSchedulerProvider;

public class AddWalletPresenter extends MvpBasePresenter<AddWalletView> {

    private AddWalletInteractor mAddWalletInteractor;
    private RxSchedulerProvider mRxSchedulerProvider;
    private SystemManager mSystemManager;

    @Inject
    public AddWalletPresenter(AddWalletInteractor addWalletInteractor,
                              RxSchedulerProvider rxSchedulerProvider,
                              SystemManager systemManager) {
        mAddWalletInteractor = addWalletInteractor;
        mRxSchedulerProvider = rxSchedulerProvider;
        mSystemManager = systemManager;
    }

    @Override
    public void onAttach(AddWalletView view, @Nullable Bundle savedInstanceState) {
        super.onAttach(view, savedInstanceState);
        loadCoins();
    }

    private void loadCoins() {
        Disposable disposable = mAddWalletInteractor.getCoins()
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(coins -> getView().setupCoins(coins));
        disposeOnDetach(disposable);
    }

    public void onSubmitClick(Wallet wallet) {
        addWallet(wallet);
    }

    private void addWallet(Wallet wallet) {
        mAddWalletInteractor.addWallet(wallet)
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(this::onSuccess, this::onError);
    }

    private void onSuccess() {
        getView().showMessage(R.string.success_add_wallet);
        getView().exit();
    }

    public void onQRCodeClick() {
        getView().scanQRCode();
    }

    private void onError(Throwable throwable) {
        if (throwable instanceof CoinNotSupportedException) {
            getView().showMessage(R.string.error_coin_not_supported);
        } else if (throwable instanceof SQLiteConstraintException) {
            getView().showMessage(R.string.error_add_wallet_already_exist);
        } else if (!mSystemManager.isConnected()) {
            getView().showMessage(R.string.error_connection);
        } else {
            getView().showMessage(R.string.error_unknown);
        }
        L.e(throwable);
    }


}
