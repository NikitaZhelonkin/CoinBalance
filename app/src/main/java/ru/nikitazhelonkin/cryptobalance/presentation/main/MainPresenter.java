package ru.nikitazhelonkin.cryptobalance.presentation.main;


import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import ru.nikitazhelonkin.cryptobalance.R;
import ru.nikitazhelonkin.cryptobalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.cryptobalance.data.entity.Wallet;
import ru.nikitazhelonkin.cryptobalance.data.system.ClipboardManager;
import ru.nikitazhelonkin.cryptobalance.domain.MainInteractor;
import ru.nikitazhelonkin.cryptobalance.mvp.MvpBasePresenter;
import ru.nikitazhelonkin.cryptobalance.utils.L;
import ru.nikitazhelonkin.cryptobalance.utils.rx.scheduler.RxSchedulerProvider;

public class MainPresenter extends MvpBasePresenter<MainView> {

    private MainInteractor mMainInteractor;
    private RxSchedulerProvider mRxSchedulerProvider;
    private ClipboardManager mClipboardManager;

    @Inject
    public MainPresenter(MainInteractor mainInteractor,
                         RxSchedulerProvider rxSchedulerProvider,
                         ClipboardManager clipboardManager) {
        mMainInteractor = mainInteractor;
        mRxSchedulerProvider = rxSchedulerProvider;
        mClipboardManager = clipboardManager;
    }

    @Override
    public void onAttach(MainView view, @Nullable Bundle savedInstanceState) {
        super.onAttach(view, savedInstanceState);
        syncBalances();
        loadWallets();
    }

    public void onRefresh() {
        syncBalances();
    }

    public void onAddClick() {
        getView().navigateToAddWalletView();
    }

    public void onMenuItemClick(Wallet wallet, int itemId) {
        switch (itemId) {
            case R.id.action_copy:
                mClipboardManager.copyToClipboard(wallet.getAddress());
                getView().showMessage(R.string.address_copied);
                break;
            case R.id.action_code:
                getView().showQRCodeView(wallet);
                break;
            case R.id.action_edit:
                getView().showEditNameView(wallet);
                break;
            case R.id.action_delete:
                getView().showDeleteView(wallet);
                break;
        }
    }

    public void editWalletName(Wallet wallet, String name) {
        mMainInteractor.editWalletName(wallet, name)
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(this::loadWallets);
    }

    public void deleteWallet(Wallet wallet) {
        mMainInteractor.deleteWallet(wallet)
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(this::loadWallets);
    }

    private void syncBalances() {
        getView().showLoader();
        Disposable disposable = mMainInteractor.syncBalances()
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(() -> {
                    getView().hideLoader();
                    loadWallets();
                }, throwable -> {
                    getView().hideLoader();
                    getView().showError(R.string.error_unknown);
                    L.e(throwable);
                });
        disposeOnDetach(disposable);
    }

    private void loadWallets() {
        Disposable disposable = mMainInteractor.loadData()
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(this::onResult, this::onError);
        disposeOnDetach(disposable);
    }

    private void onResult(MainViewModel data) {
        getView().setData(data);
    }

    private void onError(Throwable e) {
        getView().showError(R.string.error_unknown);
        L.e(e);
    }

}
