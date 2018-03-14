package ru.nikitazhelonkin.coinbalance.domain;


import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.client.coin.ApiClientProvider;
import ru.nikitazhelonkin.coinbalance.data.entity.Coin;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.data.exception.CoinNotSupportedException;
import ru.nikitazhelonkin.coinbalance.data.exception.InvalidAddressException;
import ru.nikitazhelonkin.coinbalance.data.repository.WalletRepository;
import ru.nikitazhelonkin.coinbalance.data.validator.AddressValidator;
import ru.nikitazhelonkin.coinbalance.data.validator.AddressValidatorFactory;

public class AddWalletInteractor {

    private WalletRepository mWalletRepository;
    private ApiClientProvider mApiClientProvider;

    @Inject
    public AddWalletInteractor(WalletRepository walletRepository,
                               ApiClientProvider apiClientProvider) {
        mWalletRepository = walletRepository;
        mApiClientProvider = apiClientProvider;
    }

    public Completable addWallet(Wallet wallet) {
        return Single.fromCallable(() -> {
            Coin coin = Coin.forTicker(wallet.getCoinTicker());
            if (coin != null) {
                return coin;
            }
            throw new CoinNotSupportedException();
        }).doOnSuccess(coin -> {
            AddressValidator validator = AddressValidatorFactory.forCoin(coin.getTicker());
            if (validator != null && !validator.isValid(wallet.getAddress()))
                throw new InvalidAddressException();
        }).doOnSuccess(coin -> {
            try {
                wallet.setBalance(Float.parseFloat(getBalance(wallet).blockingGet()));
                wallet.setStatus(Wallet.STATUS_OK);
            } catch (Throwable e) {
                wallet.setStatus(Wallet.STATUS_ERROR);
            }
        }).flatMapCompletable(balance -> mWalletRepository.insert(wallet, false))
                .doOnComplete(() -> mWalletRepository.notifyChange());
    }

    public Single<List<Coin>> getCoins() {
        return Observable.fromArray(Coin.values()).toList();
    }

    private Single<String> getBalance(Wallet wallet) {
        return mApiClientProvider.provide(wallet.getCoinTicker()).getBalance(wallet.getAddress());
    }

}
