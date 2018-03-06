package ru.nikitazhelonkin.cryptobalance.domain;


import android.text.TextUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nikitazhelonkin.cryptobalance.data.AppSettings;
import ru.nikitazhelonkin.cryptobalance.data.api.CryptoCompareApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.client.ApiClientProvider;
import ru.nikitazhelonkin.cryptobalance.data.api.response.Prices;
import ru.nikitazhelonkin.cryptobalance.data.entity.Coin;
import ru.nikitazhelonkin.cryptobalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.cryptobalance.data.entity.Wallet;
import ru.nikitazhelonkin.cryptobalance.data.repository.CoinRepository;
import ru.nikitazhelonkin.cryptobalance.data.repository.ObservableRepository;
import ru.nikitazhelonkin.cryptobalance.data.repository.WalletRepository;
import ru.nikitazhelonkin.cryptobalance.utils.L;

public class MainInteractor {

    private CoinRepository mCoinRepository;
    private WalletRepository mWalletRepository;

    private ApiClientProvider mApiClientProvider;

    private CryptoCompareApiService mPriceApiService;

    private AppSettings mAppSettings;

    @Inject
    public MainInteractor(CoinRepository coinRepository,
                          WalletRepository walletRepository,
                          ApiClientProvider apiClientProvider,
                          CryptoCompareApiService cryptoCompareApiService,
                          AppSettings settings) {
        mCoinRepository = coinRepository;
        mWalletRepository = walletRepository;
        mApiClientProvider = apiClientProvider;
        mPriceApiService = cryptoCompareApiService;
        mAppSettings = settings;
    }

    public Completable editWalletName(Wallet wallet, String name){
        wallet.setAlias(name);
        return mWalletRepository.update(wallet, true);
    }

    public Completable deleteWallet(Wallet wallet){
        return mWalletRepository.delete(wallet, true);
    }

    public Observable<ObservableRepository.Event> observeWallet() {
        return mWalletRepository.observe();
    }

    public Observable<Class<?>> observeSettings(){
        return mAppSettings.observe();
    }

    public Completable syncBalances(){
        return mWalletRepository.getWallets()
                 .toObservable()
                .flatMap(Observable::fromIterable)
                .doOnNext(wallet -> {
                    try{
                        wallet.setBalance(Float.parseFloat(getBalance(wallet).blockingGet()));
                        wallet.setStatus(Wallet.STATUS_OK);
                    }catch (Throwable e){
                        L.e(e);
                        wallet.setStatus(Wallet.STATUS_ERROR);
                    }
                    mWalletRepository.update(wallet, false).blockingAwait();
                })
                .toList()
                .toCompletable()
                .doOnComplete(() -> mWalletRepository.notifyChange());

    }

    public Single<MainViewModel> loadData() {
        return Single.zip(getWallets(), getCoins(),
                getPrices(getCurrency()),(wallets, coins, prices) -> new MainViewModel(
                        getCurrency(), wallets, coins, prices));
    }

    public String getCurrency(){
        return mAppSettings.getCurrency();
    }

    public Completable updateWallets(List<Wallet> wallets, boolean notify){
        return  mWalletRepository.update(wallets, notify);
    }

    private Single<List<Wallet>> getWallets() {
        return mWalletRepository.getWallets();
    }

    private Single<List<Coin>> getCoins() {
        return mCoinRepository.getCoins();
    }

    private Single<Prices> getPrices(String currency){
        return mCoinRepository.getCoins()
                .toObservable()
                .flatMap(Observable::fromIterable)
                .map(Coin::getTicker)
                .toList().map(strings -> TextUtils.join(",", strings))
                .flatMap(s -> mPriceApiService.getPrices(s, currency));
    }

    private Single<String> getBalance(Wallet wallet) {
        return mApiClientProvider.provide(wallet.getCoinTicker()).getBalance(wallet.getAddress());
    }
}
