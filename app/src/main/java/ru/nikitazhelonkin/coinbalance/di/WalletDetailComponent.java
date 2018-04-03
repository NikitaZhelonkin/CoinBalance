package ru.nikitazhelonkin.coinbalance.di;


import dagger.Component;
import ru.nikitazhelonkin.coinbalance.presentation.walletdetail.WalletDetailPresenter;

@Presenter
@Component(dependencies = AppComponent.class, modules = WalletDetailModule.class)
public interface WalletDetailComponent {

    WalletDetailPresenter presenter();
}
