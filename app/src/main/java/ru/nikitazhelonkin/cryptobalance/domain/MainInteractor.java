package ru.nikitazhelonkin.cryptobalance.domain;


import android.text.TextUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nikitazhelonkin.cryptobalance.data.api.CryptoCompareApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.client.ApiClientProvider;
import ru.nikitazhelonkin.cryptobalance.data.api.response.Prices;
import ru.nikitazhelonkin.cryptobalance.data.entity.Coin;
import ru.nikitazhelonkin.cryptobalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.cryptobalance.data.entity.Wallet;
import ru.nikitazhelonkin.cryptobalance.data.repository.CoinRepository;
import ru.nikitazhelonkin.cryptobalance.data.repository.WalletRepository;

public class MainInteractor {

    private CoinRepository mCoinRepository;
    private WalletRepository mWalletRepository;

    private ApiClientProvider mApiClientProvider;

    private CryptoCompareApiService mPriceApiService;

    @Inject
    public MainInteractor(CoinRepository coinRepository,
                          WalletRepository walletRepository,
                          ApiClientProvider apiClientProvider,
                          CryptoCompareApiService cryptoCompareApiService) {
        mCoinRepository = coinRepository;
        mWalletRepository = walletRepository;
        mApiClientProvider = apiClientProvider;
        mPriceApiService = cryptoCompareApiService;
    }

    public Completable editWalletName(Wallet wallet, String name){
        wallet.setAlias(name);
        return mWalletRepository.update(wallet);
    }

    public Completable deleteWallet(Wallet wallet){
        return mWalletRepository.delete(wallet);
    }

    public Observable<Class<?>> observe() {
        return mWalletRepository.observe();
    }

    public Completable syncBalances(){
        return mWalletRepository.getWallets()
                 .toObservable()
                .flatMap(Observable::fromIterable)
                .doOnNext(wallet -> wallet.setBalance(Float.parseFloat(getBalance(wallet).blockingGet())))
                .doOnNext(wallet -> mWalletRepository.update(wallet))
                .toList()
                .toCompletable();

    }

    public Single<MainViewModel> loadData() {
        return Single.zip(getWallets(), getCoins(),
                getPrices(getCurrency()),(wallets, coins, prices) -> new MainViewModel(
                        getCurrency(), wallets, coins, prices));
    }

    private String getCurrency(){
        return "USD";
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

    private Single<Coin> getCoin(String ticker) {
        return mCoinRepository.getCoin(ticker);
    }

    private Single<String> getBalance(Wallet wallet) {
        return mApiClientProvider.provide(wallet.getCoinTicker()).getBalance(wallet.getAddress());
    }
}
