package ru.nikitazhelonkin.cryptobalance.data.repository;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nikitazhelonkin.cryptobalance.R;
import ru.nikitazhelonkin.cryptobalance.data.entity.Coin;

public class CoinRepository {

    private static final List<Coin> SUPPORTED_COINS = new ArrayList<Coin>();

    static {
        SUPPORTED_COINS.add(new Coin("BTC", "Bitcoin", R.drawable.btc));
        SUPPORTED_COINS.add(new Coin("ETH", "Etherium", R.drawable.eth));
        SUPPORTED_COINS.add(new Coin("LTC", "Litecoin", R.drawable.ltc));
        SUPPORTED_COINS.add(new Coin("XRP", "Ripple", R.drawable.xrp));
    }

    public Single<List<Coin>> getCoins() {
        return Single.just(SUPPORTED_COINS);
    }

    public Single<Coin> getCoin(@NonNull String ticker) {
        return Observable.fromIterable(SUPPORTED_COINS)
                .filter(coin -> coin.getTicker().toUpperCase().equals(ticker.toUpperCase()))
                .firstElement().toSingle();
    }

    public Single<List<Coin>> search(String text) {
        return Observable.fromIterable(SUPPORTED_COINS)
                .filter(coin ->
                        coin.getTicker().toUpperCase().contains(text.toUpperCase()) ||
                                coin.getName().toUpperCase().contains(text.toUpperCase())
                )
                .toList();
    }
}