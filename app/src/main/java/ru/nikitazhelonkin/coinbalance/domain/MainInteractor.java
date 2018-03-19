package ru.nikitazhelonkin.coinbalance.domain;


import android.text.TextUtils;

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
import ru.nikitazhelonkin.coinbalance.data.api.service.CryptoCompareApiService;
import ru.nikitazhelonkin.coinbalance.data.entity.Coin;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeBalance;
import ru.nikitazhelonkin.coinbalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.data.exception.NoPermissionException;
import ru.nikitazhelonkin.coinbalance.data.repository.ExchangeBalancesRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.ExchangeRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.ObservableRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.WalletRepository;
import ru.nikitazhelonkin.coinbalance.utils.L;

public class MainInteractor {

    private WalletRepository mWalletRepository;
    private ExchangeRepository mExchangeRepository;
    private ExchangeBalancesRepository mExchangeBalancesRepository;

    private ApiClientProvider mApiClientProvider;
    private ExchangeApiClientProvider mExchangeApiClientProvider;

    private CryptoCompareApiService mPriceApiService;

    private AppSettings mAppSettings;

    @Inject
    public MainInteractor(WalletRepository walletRepository,
                          ExchangeRepository exchangeRepository,
                          ExchangeBalancesRepository exchangeBalancesRepository,
                          ApiClientProvider apiClientProvider,
                          ExchangeApiClientProvider exchangeApiClientProvider,
                          CryptoCompareApiService cryptoCompareApiService,
                          AppSettings settings) {
        mWalletRepository = walletRepository;
        mExchangeRepository = exchangeRepository;
        mExchangeBalancesRepository = exchangeBalancesRepository;
        mApiClientProvider = apiClientProvider;
        mExchangeApiClientProvider = exchangeApiClientProvider;
        mPriceApiService = cryptoCompareApiService;
        mAppSettings = settings;
    }

    public Completable editWalletName(Wallet wallet, String name) {
        wallet.setAlias(name);
        return mWalletRepository.update(wallet, true);
    }

    public Completable deleteWallet(Wallet wallet) {
        return mWalletRepository.delete(wallet, true);
    }

    public Completable editExchangeTitle(Exchange exchange, String title) {
        exchange.setTitle(title);
        return mExchangeRepository.update(exchange, true);
    }

    public Completable deleteExchange(Exchange exchange) {
        return mExchangeRepository.delete(exchange, true);
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
                        wallet.setBalance(Float.parseFloat(getBalance(wallet).blockingGet()));
                        wallet.setStatus(Wallet.STATUS_OK);
                    } catch (Throwable e) {
                        if (e.getCause() instanceof InterruptedIOException) {
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

    private Completable syncExchanges() {
        return mExchangeRepository.getExchanges()
                .flatMapObservable(Observable::fromIterable)
                .doOnNext(exchange -> {
                    try {
                        List<ExchangeBalance> balances = getBalances(exchange).blockingGet();
                        mExchangeBalancesRepository.delete(exchange.getId()).blockingAwait();
                        mExchangeBalancesRepository.insert(balances).blockingAwait();
                        exchange.setStatus(Exchange.STATUS_OK);
                    } catch (Throwable e) {
                        if (e.getCause() instanceof InterruptedIOException) {
                            //ignore interruption
                            return;
                        }
                        L.e("Error", e);
                        YandexMetrica.reportError("syncExchange.error", e);
                        if (e instanceof NoPermissionException) {
                            exchange.setStatus(Exchange.STATUS_ERROR_NO_PERMISSION);
                        } else {
                            exchange.setStatus(Exchange.STATUS_ERROR);
                        }
                    }
                    mExchangeRepository.update(exchange, false).blockingAwait();
                })
                .toList()
                .toCompletable();

    }


    public Single<MainViewModel> loadData() {
        return Single.zip(getWallets(),
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


    private Single<List<Exchange>> getExchanges() {
        return mExchangeRepository.getExchanges();
    }

    private Single<List<ExchangeBalance>> getExchangeBalances() {
        return mExchangeBalancesRepository.getBalances();
    }

    private Single<Prices> getPrices(String currency) {
        return getCoinForPrices()
                .map(strings -> TextUtils.join(",", strings))
                .flatMap(s -> mPriceApiService.getPrices(s, currency))
                .doOnSuccess(prices -> prices.setCurrency(currency));
    }

    private Single<List<String>> getCoinForPrices() {
        Observable<String> privateCoins = Observable.fromArray(Coin.values()).toList()
                .flatMapObservable(Observable::fromIterable)
                .map(Coin::getTicker);
        Observable<String> exchangeCoins = getExchangeBalances()
                .flatMapObservable(Observable::fromIterable)
                .map(ExchangeBalance::getCoinTicker);
        return Observable.merge(privateCoins, exchangeCoins)
                .distinct()
                .toList();
    }

    private Single<String> getBalance(Wallet wallet) {
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
