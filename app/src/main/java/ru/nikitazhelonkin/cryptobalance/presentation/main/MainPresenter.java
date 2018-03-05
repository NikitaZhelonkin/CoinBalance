package ru.nikitazhelonkin.cryptobalance.presentation.main;


import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import ru.nikitazhelonkin.cryptobalance.R;
import ru.nikitazhelonkin.cryptobalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.cryptobalance.data.entity.Wallet;
import ru.nikitazhelonkin.cryptobalance.data.system.ClipboardManager;
import ru.nikitazhelonkin.cryptobalance.data.system.SystemManager;
import ru.nikitazhelonkin.cryptobalance.domain.MainInteractor;
import ru.nikitazhelonkin.cryptobalance.mvp.MvpBasePresenter;
import ru.nikitazhelonkin.cryptobalance.utils.L;
import ru.nikitazhelonkin.cryptobalance.utils.rx.scheduler.RxSchedulerProvider;

public class MainPresenter extends MvpBasePresenter<MainView> {

    private MainInteractor mMainInteractor;
    private RxSchedulerProvider mRxSchedulerProvider;
    private ClipboardManager mClipboardManager;
    private SystemManager mSystemManager;

    private MainViewModel mData;

    @Inject
    public MainPresenter(MainInteractor mainInteractor,
                         RxSchedulerProvider rxSchedulerProvider,
                         ClipboardManager clipboardManager,
                         SystemManager systemManager) {
        mMainInteractor = mainInteractor;
        mRxSchedulerProvider = rxSchedulerProvider;
        mClipboardManager = clipboardManager;
        mSystemManager = systemManager;
    }

    @Override
    public void onAttach(MainView view, @Nullable Bundle savedInstanceState) {
        super.onAttach(view, savedInstanceState);
        syncBalances();
        loadWallets();
        observe();
        getView().setTotalBalance(mMainInteractor.getCurrency(), 0);
    }

    public void onSettingsClick() {
        getView().navigateToSettingsView();
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
                .subscribe();
    }

    public void deleteWallet(Wallet wallet) {
        mMainInteractor.deleteWallet(wallet)
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe();
    }

    private void observe() {
        Disposable disposable = mMainInteractor.observe()
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(aClass -> loadWallets());
        disposeOnDetach(disposable);

    }

    private void syncBalances() {
        getView().showLoader();
        Disposable disposable = mMainInteractor.syncBalances()
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(() -> getView().hideLoader(), this::onError);
        disposeOnDetach(disposable);
    }

    private void loadWallets() {
        Disposable disposable = mMainInteractor.loadData()
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(this::onResult, this::onError);
        disposeOnDetach(disposable);
    }

    private void onResult(MainViewModel data) {
        mData = data;
        getView().setData(data);
        getView().setTotalBalance(data.getCurrency(), data.getTotalBalance());
        getView().setEmptyViewVisible(data.getWalletCount() == 0);
        getView().setErrorViewVisible(false);
    }

    public void updateItemPositions() {
        if (mData == null)
            return;
        for (int i = 0; i < mData.getWalletCount(); i++) {
            mData.getWallet(i).setPosition(i);
        }
        mMainInteractor.updateWallets(mData.getWallets(), false)
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe();
    }

    private void onError(Throwable e) {
        L.e(e);
        getView().hideLoader();
        getView().setErrorViewVisible(mData == null);
        if (!mSystemManager.isConnected()) {
            getView().showError(R.string.error_connection);
        } else {
            getView().showError(R.string.error_unknown);
        }
    }

}
