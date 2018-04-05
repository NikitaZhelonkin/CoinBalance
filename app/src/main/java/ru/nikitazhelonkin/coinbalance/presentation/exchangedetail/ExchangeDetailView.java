package ru.nikitazhelonkin.coinbalance.presentation.exchangedetail;


import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeDetailViewModel;
import ru.nikitazhelonkin.coinbalance.mvp.MvpView;

public interface ExchangeDetailView extends MvpView {

    void showExchange(ExchangeDetailViewModel model);

    void showMessage(int messageResId);

    void showEditNameView(Exchange exchange);

    void showDeleteView(Exchange exchange);

    void exit();
}
