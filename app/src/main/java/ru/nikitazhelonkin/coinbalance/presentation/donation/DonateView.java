package ru.nikitazhelonkin.coinbalance.presentation.donation;


import android.content.Context;

import ru.nikitazhelonkin.coinbalance.mvp.MvpView;

public interface DonateView extends MvpView {

    Context getContext();

    void showMessage(int messageResId);

    void exit();
}
