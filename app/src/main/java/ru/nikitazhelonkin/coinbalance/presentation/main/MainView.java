package ru.nikitazhelonkin.coinbalance.presentation.main;


import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.mvp.MvpView;

public interface MainView extends MvpView {

    void setData(MainViewModel data, boolean animate);

    void setMode(int mode, boolean animate);

    void setTotalBalance(String currency, float balance);

    void setProfitLoss(float pl);

    void setEmptyViewVisible(boolean visible);

    void setErrorViewVisible(boolean visible);

    void showError(int errorResId);

    void showMessage(int message);

    void showWalletError();

    void showExchangeError(String message);

    void showLoader();

    void hideLoader();

    void showRateDialog();

    void showAddBottomSheetDialog();

    void reportError();

    void navigateToMarket();

    void navigateToAddWalletView();

    void navigateToAddExchangeView();

    void navigateToSettingsView();

    void navigateToWalletDetail(Wallet wallet);

    void navigateToExchangeDetail(Exchange exchange);

}
