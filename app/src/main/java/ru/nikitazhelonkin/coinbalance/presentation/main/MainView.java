package ru.nikitazhelonkin.coinbalance.presentation.main;


import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.mvp.MvpView;

public interface MainView extends MvpView {

    void setData(MainViewModel data);

    void setMode(int mode, boolean animate);

    void setTotalBalance(String currency, float balance);

    void setEmptyViewVisible(boolean visible);

    void setErrorViewVisible(boolean visible);

    void showError(int errorResId);

    void showMessage(int message);

    void showLoader();

    void hideLoader();

    void showQRCodeView(Wallet wallet);

    void showEditNameView(Wallet wallet);

    void showDeleteView(Wallet wallet);

    void showDeleteView(Exchange exchange);

    void showEditTitleView(Exchange exchange);

    void navigateToAddWalletView();

    void navigateToAddExchangeView();

    void navigateToSettingsView();


}
