package ru.nikitazhelonkin.cryptobalance.presentation.main;


import java.util.List;

import ru.nikitazhelonkin.cryptobalance.data.entity.WalletViewModel;
import ru.nikitazhelonkin.cryptobalance.mvp.MvpView;

public interface MainView extends MvpView {

    void setData(List<WalletViewModel> data);


    void showError(String error);

    void showLoader();

    void hideLoader();

    void navigateToAddWalletView();

}
