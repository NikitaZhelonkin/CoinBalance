package ru.nikitazhelonkin.coinbalance.presentation.main;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.yandex.metrica.YandexMetrica;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import ru.nikitazhelonkin.coinbalance.Const;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.data.prefs.Prefs;
import ru.nikitazhelonkin.coinbalance.data.repository.ObservableRepository;
import ru.nikitazhelonkin.coinbalance.data.system.ClipboardManager;
import ru.nikitazhelonkin.coinbalance.data.system.SystemManager;
import ru.nikitazhelonkin.coinbalance.domain.MainInteractor;
import ru.nikitazhelonkin.coinbalance.mvp.MvpBasePresenter;
import ru.nikitazhelonkin.coinbalance.utils.L;
import ru.nikitazhelonkin.coinbalance.utils.rx.scheduler.RxSchedulerProvider;

public class MainPresenter extends MvpBasePresenter<MainView> {

    private MainInteractor mMainInteractor;
    private RxSchedulerProvider mRxSchedulerProvider;
    private ClipboardManager mClipboardManager;
    private SystemManager mSystemManager;
    private Prefs mPrefs;

    private MainViewModel mData;

    private Handler mHandler;

    public static final int MODE_MAIN = 0;
    public static final int MODE_CHART = 1;
    private int mMode = -1;

    @Inject
    public MainPresenter(MainInteractor mainInteractor,
                         RxSchedulerProvider rxSchedulerProvider,
                         ClipboardManager clipboardManager,
                         SystemManager systemManager,
                         Prefs prefs) {
        mMainInteractor = mainInteractor;
        mRxSchedulerProvider = rxSchedulerProvider;
        mClipboardManager = clipboardManager;
        mSystemManager = systemManager;
        mPrefs = prefs;
        mHandler = new Handler();
    }

    @Override
    public void onAttach(MainView view, @Nullable Bundle savedInstanceState) {
        super.onAttach(view, savedInstanceState);
        syncBalances();
        loadWallets();
        observe();
        setMode(MODE_MAIN, false);
        getView().setTotalBalance(mMainInteractor.getCurrency(), 0);
        getView().setProfitLoss(0);

        if (savedInstanceState == null) {
            int appOpenCount = mPrefs.getInt(Const.PREFS_APP_OPEN_COUNT, 0) + 1;
            boolean appRated = mPrefs.getBoolean(Const.PREFS_APP_RATED, false);
            mPrefs.putInt(Const.PREFS_APP_OPEN_COUNT, appOpenCount);
            if (!appRated && appOpenCount % Const.APP_OPEN_COUNT_TO_RATE == 0) {
                getView().showRateDialog();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        stopUpdates();
    }

    public void onSettingsClick() {
        getView().navigateToSettingsView();
    }

    public void onModeClick() {
        setMode(mMode == MODE_CHART ? MODE_MAIN : MODE_CHART, true);
    }

    public void onRefresh() {
        syncBalances();
    }

    public void onAddWalletClick() {
        getView().navigateToAddWalletView();
    }

    public void onAddExchangeClick() {
        getView().navigateToAddExchangeView();
    }

    public void onWalletItemClick(Wallet wallet) {
        if (wallet.getStatus() == Wallet.STATUS_ERROR) {
            getView().showError(R.string.wallet_status_error);
        } else if (wallet.getStatus() == Wallet.STATUS_NONE) {
            getView().showMessage(R.string.wallet_status_none);
        }
    }

    public void onExchangeItemClick(Exchange exchange) {
        if (exchange.getStatus() == Exchange.STATUS_ERROR) {
            getView().showError(R.string.exchange_status_error);
        } else if (exchange.getStatus() == Exchange.STATUS_ERROR_NO_PERMISSION) {
            getView().showError(R.string.exchange_status_error_no_permission);
        } else if (exchange.getStatus() == Exchange.STATUS_NONE) {
            getView().showMessage(R.string.exchange_status_none);
        }
    }

    public void onWalletMenuItemClick(Wallet wallet, int itemId) {
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

    public void onExchangeMenuItemClick(Exchange exchange, int itemId) {
        switch (itemId) {
            case R.id.action_delete:
                getView().showDeleteView(exchange);
                break;
            case R.id.action_edit:
                getView().showEditTitleView(exchange);
                break;
        }
    }

    public void onRateClick() {
        mPrefs.putBoolean(Const.PREFS_APP_RATED, true);
        getView().navigateToMarket();
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

    public void editExchangeTitle(Exchange exchange, String title) {
        mMainInteractor.editExchangeTitle(exchange, title)
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe();
    }

    public void deleteExchange(Exchange exchange) {
        mMainInteractor.deleteExchange(exchange)
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe();
    }

    private void observe() {
        Disposable disposable = mMainInteractor.observeSettings()
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(aClass -> loadWallets());
        disposeOnDetach(disposable);

        disposable = mMainInteractor.observeData()
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(event -> {
                    loadWallets();
                    if (event.getEventType() == ObservableRepository.Event.INSERT) {
                        syncBalances();
                    }
                });
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
        getView().setProfitLoss(data.calculateChange24Hours());
        getView().setEmptyViewVisible(data.getItems().size() == 0);
        getView().setErrorViewVisible(false);

        postUpdate();
    }

    public void onStartDragging() {
        stopUpdates();
    }

    public void onStopDragging() {
        postUpdate();
        updateItemPositions();
    }

    private void updateItemPositions() {
        if (mData == null)
            return;
        for (int i = 0; i < mData.getItems().size(); i++) {
            mData.getItem(i).setPosition(i);
        }
        Completable.mergeArray(
                mMainInteractor.updateWallets(mData.getWallets(), false),
                mMainInteractor.updateExchanges(mData.getExchanges(), false)
        ).compose(mRxSchedulerProvider.ioToMainTransformer()).subscribe();
    }

    private void onError(Throwable e) {
        L.e(e);
        YandexMetrica.reportError("MainPresenter.onError", e);
        getView().hideLoader();
        getView().setErrorViewVisible(mData == null);
        if (!mSystemManager.isConnected()) {
            getView().showError(R.string.error_connection);
        } else {
            getView().showError(R.string.error_unknown);
        }
    }

    private void setMode(int mode, boolean animate) {
        if (mMode != mode) {
            mMode = mode;
            getView().setMode(mode, animate);
        }
    }

    private void postUpdate() {
       stopUpdates();
        mHandler.postDelayed(this::loadWallets, TimeUnit.SECONDS.toMillis(10));
    }

    private void stopUpdates() {
        mHandler.removeCallbacksAndMessages(null);
    }

}
