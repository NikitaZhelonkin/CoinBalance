package ru.nikitazhelonkin.cryptobalance.presentation.settings;


import ru.nikitazhelonkin.cryptobalance.mvp.MvpView;

public interface SettingsView extends MvpView {

    void showMessage(String message);
}
