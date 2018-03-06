package ru.nikitazhelonkin.coinbalance.di;


import dagger.Component;
import ru.nikitazhelonkin.coinbalance.presentation.add.AddWalletPresenter;
import ru.nikitazhelonkin.coinbalance.presentation.main.MainPresenter;
import ru.nikitazhelonkin.coinbalance.presentation.settings.SettingsPresenter;

@Presenter
@Component(dependencies = AppComponent.class)
public interface PresenterComponent {

    MainPresenter mainPresenter();

    AddWalletPresenter addWalletPresenter();

    SettingsPresenter settingsPresenter();


}
