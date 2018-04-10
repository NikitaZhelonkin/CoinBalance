package ru.nikitazhelonkin.coinbalance.domain;


import com.yandex.metrica.YandexMetrica;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.AppSettings;
import ru.nikitazhelonkin.coinbalance.data.api.client.coin.ApiClientProvider;
import ru.nikitazhelonkin.coinbalance.data.api.client.exchange.ExchangeApiClientProvider;
import ru.nikitazhelonkin.coinbalance.data.api.response.Prices;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeBalance;
import ru.nikitazhelonkin.coinbalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.coinbalance.data.entity.Token;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletBalance;
import ru.nikitazhelonkin.coinbalance.data.exception.ApiError;
import ru.nikitazhelonkin.coinbalance.data.repository.ExchangeBalancesRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.ExchangeRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.ObservableRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.TokenRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.WalletRepository;
import ru.nikitazhelonkin.coinbalance.utils.L;

public class MainInteractor {

    private WalletRepository mWalletRepository;
    private TokenRepository mTokenRepository;
    private ExchangeRepository mExchangeRepository;
    private ExchangeBalancesRepository mExchangeBalancesRepository;

    private ApiClientProvider mApiClientProvider;
    private ExchangeApiClientProvider mExchangeApiClientProvider;

    private PriceInteractor mPriceInteractor;

    private AppSettings mAppSettings;

    @Inject
    public MainInteractor(WalletRepository walletRepository,
                          TokenRepository tokenRepository,
                          ExchangeRepository exchangeRepository,
                          ExchangeBalancesRepository exchangeBalancesRepository,
                          ApiClientProvider apiClientProvider,
                          ExchangeApiClientProvider exchangeApiClientProvider,
                          PriceInteractor priceInteractor,
                          AppSettings settings) {
        mWalletRepository = walletRepository;
        mTokenRepository = tokenRepository;
        mExchangeRepository = exchangeRepository;
        mExchangeBalancesRepository = exchangeBalancesRepository;
        mApiClientProvider = apiClientProvider;
        mExchangeApiClientProvider = exchangeApiClientProvider;
        mPriceInteractor = priceInteractor;
        mAppSettings = settings;
    }

    public Observable<ObservableRepository.Event> observeData() {
        return Observable.merge(mWalletRepository.observe(), mExchangeRepository.observe())
                .debounce(100, TimeUnit.MILLISECONDS);
    }

    public Observable<Class<?>> observeSettings() {
        return mAppSettings.observe();
    }

    public Completable syncBalances() {
        return Completable.mergeArray(syncWallets(), syncExchanges())
                .doOnComplete(() -> mWalletRepository.notifyChange())
                .doOnComplete(() -> mExchangeRepository.notifyChange());

    }

    private Completable syncWallets() {
        return mWalletRepository.getWallets()
                .flatMapObservable(Observable::fromIterable)
                .doOnNext(wallet -> {
                    try {
                        WalletBalance walletBalance = getBalance(wallet).blockingGet();
                        wallet.setBalance(Float.parseFloat(walletBalance.getBalance()));
                        wallet.setStatus(Wallet.STATUS_OK);
                        addTokens(wallet, walletBalance.getTokenList());
                    } catch (Throwable e) {
                        if (e.getCause().getClass() == InterruptedIOException.class) {
                            //ignore interruption
                            return;
                        }
                        L.e("Error", e);
                        YandexMetrica.reportError("syncWallet.error", e);
                        wallet.setStatus(Wallet.STATUS_ERROR);
                    }
                    mWalletRepository.update(wallet, false).blockingAwait();
                })
                .toList()
                .toCompletable();

    }

    private void addTokens(Wallet wallet, List<Token> tokenList) {
        mTokenRepository.delete(wallet.getAddress()).blockingAwait();
        if (tokenList != null) mTokenRepository.insert(tokenList).blockingAwait();
    }

    private Completable syncExchanges() {
        return mExchangeRepository.getExchanges()
                .flatMapObservable(Observable::fromIterable)
                .doOnNext(exchange -> {
                    try {
                        List<ExchangeBalance> balances = getBalances(exchange).blockingGet();
                        mExchangeBalancesRepository.delete(exchange.getId()).blockingAwait();
                        mExchangeBalancesRepository.insert(balances).blockingAwait();
                        exchange.setStatus(Exchange.STATUS_OK);
                        exchange.setErrorMessage(null);
                    } catch (Throwable e) {
                        if (e.getCause() instanceof InterruptedIOException) {
                            //ignore interruption
                            return;
                        }
                        String message = e.getMessage();
                        if (e.getCause() instanceof ApiError) {
                            message = e.getMessage();
                        }
                        L.e("Error:", message);
                        YandexMetrica.reportError("syncExchange.error", e);
                        exchange.setStatus(Exchange.STATUS_ERROR);
                        exchange.setErrorMessage(message);
                    }
                    mExchangeRepository.update(exchange, false).blockingAwait();
                })
                .toList()
                .toCompletable();

    }


    public Single<MainViewModel> loadData() {
        return Single.zip(getWallets(),
                getTokens(),
                getExchanges(),
                getExchangeBalances(),
                getPrices(getCurrency()),
                MainViewModel::new);
    }

    public String getCurrency() {
        return mAppSettings.getCurrency();
    }

    public Completable updateWallets(List<Wallet> wallets, boolean notify) {
        return mWalletRepository.update(wallets, notify);
    }

    public Completable updateExchanges(List<Exchange> exchanges, boolean notify) {
        return mExchangeRepository.update(exchanges, notify);
    }


    private Single<List<Wallet>> getWallets() {
        return mWalletRepository.getWallets();
    }

    private Single<List<Token>> getTokens() {
        return mTokenRepository.getTokens();
    }

    private Single<List<Exchange>> getExchanges() {
        return mExchangeRepository.getExchanges();
    }

    private Single<List<ExchangeBalance>> getExchangeBalances() {
        return mExchangeBalancesRepository.getBalances();
    }

    private Single<Prices> getPrices(String currency) {
        return mPriceInteractor.getPrices(currency, false);
    }

    private Single<WalletBalance> getBalance(Wallet wallet) {
        return mApiClientProvider.provide(wallet.getCoinTicker()).getBalance(wallet.getAddress());
    }

    private Single<List<ExchangeBalance>> getBalances(Exchange exchange) {
        return mExchangeApiClientProvider.provide(exchange.getService())
                .getBalances(exchange.getApiKey(), exchange.getApiSecret())
                .map(map -> {
                    List<ExchangeBalance> list = new ArrayList<>(map.size());
                    for (String key : map.keySet()) {
                        list.add(new ExchangeBalance(exchange.getId(), key, map.get(key)));
                    }
                    return list;
                });
    }
}
