package ru.nikitazhelonkin.coinbalance.domain;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeService;
import ru.nikitazhelonkin.coinbalance.data.exception.ServiceNotSupportedException;
import ru.nikitazhelonkin.coinbalance.data.repository.ExchangeRepository;

public class AddExchangeInteractor {

    private ExchangeRepository mExchangeRepository;

    @Inject
    public AddExchangeInteractor(ExchangeRepository exchangeRepository) {
        mExchangeRepository = exchangeRepository;
    }

    public Completable addExchange(Exchange exchange) {
        return Single.fromCallable(() -> {
            ExchangeService service = ExchangeService.forName(exchange.getService().getTitle());
            if (service != null) {
                return service;
            }
            throw new ServiceNotSupportedException();
        }).flatMapCompletable(balance -> mExchangeRepository.insert(exchange, true));
    }

    public Single<List<ExchangeService>> getExchangeServices() {
        return Observable.fromArray(ExchangeService.values()).toList();
    }

}
