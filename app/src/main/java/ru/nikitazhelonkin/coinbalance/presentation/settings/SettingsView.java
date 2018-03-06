package ru.nikitazhelonkin.coinbalance.presentation.settings;


import ru.nikitazhelonkin.coinbalance.mvp.MvpView;

public interface SettingsView extends MvpView {

    void showMessage(String message);
}
