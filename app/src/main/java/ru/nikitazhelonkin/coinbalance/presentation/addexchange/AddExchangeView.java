package ru.nikitazhelonkin.coinbalance.presentation.addexchange;


import java.util.List;

import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeService;
import ru.nikitazhelonkin.coinbalance.mvp.MvpView;

public interface AddExchangeView extends MvpView {

    void setupServices(List<ExchangeService> serviceList);

    void showMessage(int errorResId);

    void exit();

    void setSubmitEnabled(boolean enabled);
}
