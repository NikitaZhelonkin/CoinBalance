package ru.nikitazhelonkin.coinbalance.presentation.walletdetail;


import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletDetailViewModel;
import ru.nikitazhelonkin.coinbalance.data.repository.WalletRepository;
import ru.nikitazhelonkin.coinbalance.data.system.ClipboardManager;
import ru.nikitazhelonkin.coinbalance.di.WalletDetailModule;
import ru.nikitazhelonkin.coinbalance.domain.WalletDetailInteractor;
import ru.nikitazhelonkin.coinbalance.mvp.MvpBasePresenter;
import ru.nikitazhelonkin.coinbalance.utils.L;
import ru.nikitazhelonkin.coinbalance.utils.rx.scheduler.RxSchedulerProvider;

public class WalletDetailPresenter extends MvpBasePresenter<WalletDetailView> {

    private int mWalletId;
    private WalletDetailViewModel mWalletModel;
    private WalletDetailInteractor mWalletDetailInteractor;
    private RxSchedulerProvider mSchedulerProvider;
    private ClipboardManager mClipboardManager;


    @Inject
    public WalletDetailPresenter(int walletId,
                                 WalletDetailInteractor walletDetailInteractor,
                                 RxSchedulerProvider schedulerProvider,
                                 ClipboardManager clipboardManager) {
        mWalletId = walletId;
        mWalletDetailInteractor = walletDetailInteractor;
        mSchedulerProvider = schedulerProvider;
        mClipboardManager = clipboardManager;
    }

    @Override
    public void onAttach(WalletDetailView view, @Nullable Bundle savedInstanceState) {
        super.onAttach(view, savedInstanceState);
        loadWallet(mWalletId);
    }

    public void onWalletAddressClick() {
        mClipboardManager.copyToClipboard(mWalletModel.getWallet().getAddress());
        getView().showMessage(R.string.address_copied);
    }

    public void onEditClick(){
        getView().showEditNameView(mWalletModel.getWallet());
    }

    public void onDeleteClick(){
        getView().showDeleteView(mWalletModel.getWallet());
    }

    public void editWalletName(Wallet wallet, String name) {
        mWalletDetailInteractor.editWalletName(wallet, name)
                .compose(mSchedulerProvider.ioToMainTransformer())
                .subscribe(() -> loadWallet(mWalletId));
    }

    public void deleteWallet(Wallet wallet) {
        mWalletDetailInteractor.deleteWallet(wallet)
                .compose(mSchedulerProvider.ioToMainTransformer())
                .subscribe(() -> getView().exit());
    }

    private void loadWallet(int walletId) {
        Disposable disposable = mWalletDetailInteractor.getWallet(walletId)
                .compose(mSchedulerProvider.ioToMainTransformer())
                .subscribe(this::onSuccess, this::onError);
        disposeOnDetach(disposable);
    }

    private void onSuccess(WalletDetailViewModel wallet) {
        mWalletModel = wallet;
        getView().showWallet(wallet);
    }

    private void onError(Throwable e) {
        L.e(e);
    }
}
