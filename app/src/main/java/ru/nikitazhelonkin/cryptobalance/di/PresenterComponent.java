package ru.nikitazhelonkin.cryptobalance.di;


import dagger.Component;
import ru.nikitazhelonkin.cryptobalance.presentation.add.AddWalletPresenter;
import ru.nikitazhelonkin.cryptobalance.presentation.main.MainPresenter;

@Presenter
@Component(dependencies = AppComponent.class)
public interface PresenterComponent {

    MainPresenter mainPresenter();

    AddWalletPresenter addWalletPresenter();


}
