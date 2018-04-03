package ru.nikitazhelonkin.coinbalance.domain;


import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.AppSettings;
import ru.nikitazhelonkin.coinbalance.data.api.response.Prices;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeDetailViewModel;
import ru.nikitazhelonkin.coinbalance.data.repository.ExchangeBalancesRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.ExchangeRepository;

public class ExchangeDetailInteractor {

    private ExchangeRepository mExchangeRepository;
    private ExchangeBalancesRepository mExchangeBalancesRepository;
    private PriceInteractor mPriceInteractor;
    private AppSettings mAppSettings;

    @Inject
    public ExchangeDetailInteractor(ExchangeRepository exchangeRepository,
                                    ExchangeBalancesRepository balancesRepository,
                                    PriceInteractor priceInteractor,
                                    AppSettings appSettings) {
        mExchangeRepository = exchangeRepository;
        mExchangeBalancesRepository = balancesRepository;
        mPriceInteractor = priceInteractor;
        mAppSettings = appSettings;
    }

    public Single<ExchangeDetailViewModel> getData(int exchangeId) {
        return Single.zip(mExchangeRepository.getById(exchangeId),
                mExchangeBalancesRepository.getBalances(exchangeId)
                ,getPrices(getCurrency()),
                ExchangeDetailViewModel::new);
    }

    public Completable editExchangeTitle(Exchange exchange, String title) {
        exchange.setTitle(title);
        return mExchangeRepository.update(exchange, true);
    }

    public Completable deleteExchange(Exchange exchange) {
        return mExchangeRepository.delete(exchange, true);
    }

    public String getCurrency() {
        return mAppSettings.getCurrency();
    }

    private Single<Prices> getPrices(String currency) {
        return mPriceInteractor.getPrices(currency, true);
    }
}
