package ru.nikitazhelonkin.coinbalance.domain;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.client.coin.ApiClientProvider;
import ru.nikitazhelonkin.coinbalance.data.entity.Coin;
import ru.nikitazhelonkin.coinbalance.data.entity.Token;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletBalance;
import ru.nikitazhelonkin.coinbalance.data.exception.CoinNotSupportedException;
import ru.nikitazhelonkin.coinbalance.data.exception.InvalidAddressException;
import ru.nikitazhelonkin.coinbalance.data.repository.TokenRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.WalletRepository;
import ru.nikitazhelonkin.coinbalance.data.validator.AddressValidator;
import ru.nikitazhelonkin.coinbalance.data.validator.AddressValidatorFactory;
import ru.nikitazhelonkin.coinbalance.utils.L;

public class AddWalletInteractor {

    private WalletRepository mWalletRepository;
    private ApiClientProvider mApiClientProvider;
    private TokenRepository mTokenRepository;

    @Inject
    public AddWalletInteractor(WalletRepository walletRepository,
                               TokenRepository tokenRepository,
                               ApiClientProvider apiClientProvider) {
        mWalletRepository = walletRepository;
        mTokenRepository = tokenRepository;
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
            WalletBalance walletBalance = null;
            try {
                walletBalance = getBalance(wallet).blockingGet();
                wallet.setBalance(Float.parseFloat(walletBalance.getBalance()));
                wallet.setStatus(Wallet.STATUS_OK);
            } catch (Throwable e) {
                L.e(""+e);
                wallet.setStatus(Wallet.STATUS_ERROR);
            }
            mWalletRepository.insert(wallet, false).blockingAwait();

            if (walletBalance != null) addTokens(wallet, walletBalance.getTokenList());

        }).toCompletable().doOnComplete(() ->
                mWalletRepository.notifyChange());
    }

    public Single<List<Coin>> getCoins() {
        return Observable.fromArray(Coin.values()).toList();
    }

    private Single<WalletBalance> getBalance(Wallet wallet) {
        return mApiClientProvider.provide(wallet.getCoinTicker()).getBalance(wallet.getAddress());
    }

    private void addTokens(Wallet wallet, List<Token> tokenList) {
        mTokenRepository.delete(wallet.getAddress()).blockingAwait();
        if (tokenList != null) mTokenRepository.insert(tokenList).blockingAwait();
    }

}
