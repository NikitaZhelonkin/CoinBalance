package ru.nikitazhelonkin.coinbalance.domain;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.AppSettings;
import ru.nikitazhelonkin.coinbalance.data.api.response.Prices;
import ru.nikitazhelonkin.coinbalance.data.entity.Token;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletDetailViewModel;
import ru.nikitazhelonkin.coinbalance.data.repository.TokenRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.WalletRepository;

public class WalletDetailInteractor {

    private WalletRepository mWalletRepository;
    private TokenRepository mTokenRepository;
    private PriceInteractor mPriceInteractor;
    private AppSettings mAppSettings;

    @Inject
    public WalletDetailInteractor(WalletRepository walletRepository,
                                  TokenRepository tokenRepository,
                                  PriceInteractor priceInteractor,
                                  AppSettings appSettings) {
        mWalletRepository = walletRepository;
        mTokenRepository = tokenRepository;
        mPriceInteractor = priceInteractor;
        mAppSettings = appSettings;
    }

    public String getCurrency() {
        return mAppSettings.getCurrency();
    }

    public Completable editWalletName(Wallet wallet, String name) {
        wallet.setAlias(name);
        return mWalletRepository.update(wallet, true);
    }

    public Completable deleteWallet(Wallet wallet) {
        return mWalletRepository.delete(wallet, true);
    }

    public Single<WalletDetailViewModel> getWallet(int walletId) {
        return mWalletRepository.getById(walletId).flatMap(wallet ->
                Single.zip(
                        Single.just(wallet),
                        getTokens(wallet.getAddress()),
                        getPrices(getCurrency()),
                        WalletDetailViewModel::new
                ));
    }

    private Single<List<Token>> getTokens(String walletAddress) {
        return mTokenRepository.getTokens(walletAddress);
    }

    private Single<Prices> getPrices(String currency) {
        return mPriceInteractor.getPrices(currency, true);
    }

}
