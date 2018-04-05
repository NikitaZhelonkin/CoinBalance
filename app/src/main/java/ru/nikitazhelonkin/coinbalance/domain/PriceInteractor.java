package ru.nikitazhelonkin.coinbalance.domain;


import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.response.Prices;
import ru.nikitazhelonkin.coinbalance.data.api.service.CryptoCompareApiService;
import ru.nikitazhelonkin.coinbalance.data.entity.Coin;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeBalance;
import ru.nikitazhelonkin.coinbalance.data.entity.Token;
import ru.nikitazhelonkin.coinbalance.data.repository.ExchangeBalancesRepository;
import ru.nikitazhelonkin.coinbalance.data.repository.TokenRepository;

@Singleton
public class PriceInteractor {

    private CryptoCompareApiService mApiService;
    private TokenRepository mTokenRepository;
    private ExchangeBalancesRepository mExchangeBalancesRepository;

    private HashMap<String, Prices> mCache = new HashMap<>();

    @Inject
    public PriceInteractor(CryptoCompareApiService apiService,
                           TokenRepository tokenRepository,
                           ExchangeBalancesRepository balancesRepository){
        mApiService = apiService;
        mTokenRepository = tokenRepository;
        mExchangeBalancesRepository = balancesRepository;
    }

    public Single<Prices> getPrices(String currency, boolean fromCache) {
        if (fromCache && mCache.get(currency) != null) {
            return Single.just(mCache.get(currency));
        }
        return getCoinForPrices()
                .map(strings -> TextUtils.join(",", strings))
                .flatMap(s -> mApiService.getPrices(s, currency))
                .doOnSuccess(prices -> prices.setCurrency(currency))
                .doOnSuccess(prices -> mCache.put(currency, prices));
    }

    private Single<List<String>> getCoinForPrices() {
        Observable<String> privateCoins = Observable.fromArray(Coin.values()).toList()
                .flatMapObservable(Observable::fromIterable)
                .map(Coin::getTicker);
        Observable<String> tokens = getTokens()
                .flatMapObservable(Observable::fromIterable)
                .map(Token::getTokenTicker);
        Observable<String> exchangeCoins = getExchangeBalances()
                .flatMapObservable(Observable::fromIterable)
                .map(ExchangeBalance::getCoinTicker);
        return Observable.merge(privateCoins, exchangeCoins, tokens)
                .distinct()
                .map(this::mapCoinTicker)
                .toList();
    }

    private String mapCoinTicker(String ticker) {
        if ("BCC".equalsIgnoreCase(ticker)) {
            return "BCCOIN";
        }
        return ticker;
    }

    private Single<List<Token>> getTokens(){
        return mTokenRepository.getTokens();
    }


    private Single<List<ExchangeBalance>> getExchangeBalances() {
        return mExchangeBalancesRepository.getBalances();
    }
}
