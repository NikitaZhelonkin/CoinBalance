package ru.nikitazhelonkin.cryptobalance.mvp;


import android.os.Bundle;
import android.support.annotation.Nullable;

public interface MvpPresenter<V extends MvpView> {

    void onCreate(@Nullable Bundle savedInstanceState);

    void onSaveInstanceState(Bundle outState);

    void onAttach(V view, @Nullable Bundle savedInstanceState);

    void onDetach();

    void onDestroy();
}
