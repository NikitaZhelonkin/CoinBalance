package ru.nikitazhelonkin.coinbalance.di;


import dagger.Component;
import ru.nikitazhelonkin.coinbalance.presentation.addexchange.AddExchangePresenter;
import ru.nikitazhelonkin.coinbalance.presentation.addwallet.AddWalletPresenter;
import ru.nikitazhelonkin.coinbalance.presentation.donation.DonatePresenter;
import ru.nikitazhelonkin.coinbalance.presentation.main.MainPresenter;
import ru.nikitazhelonkin.coinbalance.presentation.settings.SettingsPresenter;

@Presenter
@Component(dependencies = AppComponent.class)
public interface PresenterComponent {

    MainPresenter mainPresenter();

    AddWalletPresenter addWalletPresenter();

    AddExchangePresenter addExchangePresenter();

    SettingsPresenter settingsPresenter();

    DonatePresenter donatePresenter();


}
