package ru.nikitazhelonkin.cryptobalance.presentation.main;


import ru.nikitazhelonkin.cryptobalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.cryptobalance.data.entity.Wallet;
import ru.nikitazhelonkin.cryptobalance.mvp.MvpView;

public interface MainView extends MvpView {

    void setData(MainViewModel data);

    void setEmptyViewVisible(boolean visible);

    void showError(int errorResId);

    void showMessage(int message);

    void showLoader();

    void hideLoader();

    void showQRCodeView(Wallet wallet);

    void showEditNameView(Wallet wallet);

    void showDeleteView(Wallet wallet);

    void navigateToAddWalletView();

    void navigateToSettingsView();


}
