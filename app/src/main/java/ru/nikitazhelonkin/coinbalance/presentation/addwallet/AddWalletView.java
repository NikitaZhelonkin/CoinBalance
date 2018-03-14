package ru.nikitazhelonkin.coinbalance.presentation.addwallet;


import java.util.List;

import ru.nikitazhelonkin.coinbalance.data.entity.Coin;
import ru.nikitazhelonkin.coinbalance.mvp.MvpView;

public interface AddWalletView extends MvpView {

    void setupCoins(List<Coin> coinList);

    void showMessage(int errorResId);

    void scanQRCode();

    void exit();

    void setSubmitEnabled(boolean enabled);
}
