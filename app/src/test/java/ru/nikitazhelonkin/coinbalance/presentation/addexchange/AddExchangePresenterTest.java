package ru.nikitazhelonkin.coinbalance.presentation.addexchange;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeService;
import ru.nikitazhelonkin.coinbalance.data.system.SystemManager;
import ru.nikitazhelonkin.coinbalance.domain.AddExchangeInteractor;
import ru.nikitazhelonkin.coinbalance.utils.rx.scheduler.RxSchedulerProvider;

import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class AddExchangePresenterTest {

    @Mock
    AddExchangeInteractor addExchangeInteractor;
    @Mock
    SystemManager systemManager;
    @Mock
    AddExchangeView addExchangeView;

    private AddExchangePresenter addExchangePresenter;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        when(addExchangeInteractor.getExchangeServices()).thenReturn(Single.just(new ArrayList<>()));

        addExchangePresenter = new AddExchangePresenter(addExchangeInteractor, new TestRxSchedulerProvider(), systemManager);
        addExchangePresenter.onAttach(addExchangeView, null);

    }


    @Test
    public void addExchange_showError(){
        Exchange exchange = new Exchange(ExchangeService.BITFINEX, "apiKey", "apiSecret", "title");

        when(addExchangeInteractor.addExchange(any())).thenReturn(Completable.error(new IOException("Test exception")));

        addExchangePresenter.onAgreeClick(exchange);

        verify(addExchangeView).showMessage(anyInt());

    }

    public class TestRxSchedulerProvider extends RxSchedulerProvider {

        @Override
        public Scheduler io() {
            return Schedulers.trampoline();
        }

        @Override
        public Scheduler main() {
            return Schedulers.trampoline();
        }
    }


}