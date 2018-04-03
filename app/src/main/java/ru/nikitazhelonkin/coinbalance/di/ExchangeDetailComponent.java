package ru.nikitazhelonkin.coinbalance.di;


import dagger.Component;
import ru.nikitazhelonkin.coinbalance.presentation.exchangedetail.ExchangeDetailPresenter;

@Presenter
@Component(dependencies = AppComponent.class, modules = ExchangeDetailModule.class)
public interface ExchangeDetailComponent {

    ExchangeDetailPresenter presenter();
}
