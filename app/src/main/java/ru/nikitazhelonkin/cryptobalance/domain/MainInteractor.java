package ru.nikitazhelonkin.cryptobalance.domain;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nikitazhelonkin.cryptobalance.data.api.client.ApiClientProvider;
import ru.nikitazhelonkin.cryptobalance.data.entity.Coin;
import ru.nikitazhelonkin.cryptobalance.data.entity.Wallet;
import ru.nikitazhelonkin.cryptobalance.data.entity.WalletViewModel;
import ru.nikitazhelonkin.cryptobalance.data.repository.CoinRepository;
import ru.nikitazhelonkin.cryptobalance.data.repository.WalletRepository;

public class MainInteractor {

    private CoinRepository mCoinRepository;
    private WalletRepository mWalletRepository;

    private ApiClientProvider mApiClientProvider;

    @Inject
    public MainInteractor(CoinRepository coinRepository,
                          WalletRepository walletRepository,
                          ApiClientProvider apiClientProvider) {
        mCoinRepository = coinRepository;
        mWalletRepository = walletRepository;
        mApiClientProvider = apiClientProvider;
    }

    public Flowable<Integer> observe() {
        return mWalletRepository.observe();
    }

    public Single<List<WalletViewModel>> getData() {
        return mWalletRepository.getWallets()
                .toObservable()
                .flatMap(Observable::fromIterable)
                .map(wallet -> new WalletViewModel(
                        wallet,
                        getCoin(wallet.getCoinTicker()).blockingGet(),
                        getBalance(wallet).blockingGet()))
                .toList();
    }

    private Single<Coin> getCoin(String ticker) {
        return mCoinRepository.getCoin(ticker);
    }

    private Single<String> getBalance(Wallet wallet) {
        return mApiClientProvider.provide(wallet.getCoinTicker()).getBalance(wallet.getAddress());
    }

}
