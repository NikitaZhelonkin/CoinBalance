package ru.nikitazhelonkin.cryptobalance.presentation.add;


import java.util.List;

import ru.nikitazhelonkin.cryptobalance.data.entity.Coin;
import ru.nikitazhelonkin.cryptobalance.mvp.MvpView;

public interface AddWalletView extends MvpView {

    void setupCoins(List<Coin> coinList);

    void showMessage(int errorResId);

    void scanQRCode();

    void exit();
}
