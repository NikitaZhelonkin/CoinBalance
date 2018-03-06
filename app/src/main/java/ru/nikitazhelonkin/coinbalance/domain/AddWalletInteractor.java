package ru.nikitazhelonkin.coinbalance.domain;


import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.client.ApiClientProvider;
import ru.nikitazhelonkin.coinbalance.data.entity.Coin;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.data.exception.CoinNotSupportedException;
import ru.nikitazhelonkin.coinbalance.data.exception.InvalidAddressException;
import ru.nikitazhelonkin.coinbalance.data.repository.CoinRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.WalletRepository;
import ru.nikitazhelonkin.coinbalance.data.validator.AddressValidator;
import ru.nikitazhelonkin.coinbalance.data.validator.AddressValidatorFactory;

public class AddWalletInteractor {

    private WalletRepository mWalletRepository;
    private CoinRepository mCoinRepository;
    private ApiClientProvider mApiClientProvider;

    @Inject
    public AddWalletInteractor(WalletRepository walletRepository,
                               CoinRepository coinRepository,
                               ApiClientProvider apiClientProvider) {
        mWalletRepository = walletRepository;
        mCoinRepository = coinRepository;
        mApiClientProvider = apiClientProvider;

    }

    public Completable addWallet(Wallet wallet) {
        return mCoinRepository.getCoin(wallet.getCoinTicker())
                .onErrorResumeNext(throwable -> Single.error(mapThrowable(throwable)))
                .doOnSuccess(coin -> {
                    AddressValidator validator = AddressValidatorFactory.forCoin(coin.getTicker());
                    if(validator!=null && !validator.isValid(wallet.getAddress()))
                        throw new InvalidAddressException();
                })
                .flatMapCompletable(balance -> mWalletRepository.insert(wallet, true));

    }

    public Single<List<Coin>> getCoins() {
        return mCoinRepository.getCoins();
    }

    private Single<String> getBalance(Wallet wallet) {
        return mApiClientProvider.provide(wallet.getCoinTicker()).getBalance(wallet.getAddress());
    }

    private Throwable mapThrowable(Throwable throwable) {
        if (throwable instanceof NoSuchElementException)
            return new CoinNotSupportedException();
        return throwable;
    }


}
