package ru.nikitazhelonkin.coinbalance.data.repository;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Coin;

public class CoinRepository {

    private static final List<Coin> SUPPORTED_COINS = new ArrayList<Coin>();

    static {
        SUPPORTED_COINS.add(new Coin("BTC", "Bitcoin", R.drawable.btc));
        SUPPORTED_COINS.add(new Coin("BCH", "Bitcoin Cash", R.drawable.bch));
        SUPPORTED_COINS.add(new Coin("ETH", "Ethereum", R.drawable.eth));
        SUPPORTED_COINS.add(new Coin("ETC", "Ethereum Classic", R.drawable.etc));
        SUPPORTED_COINS.add(new Coin("LTC", "Litecoin", R.drawable.ltc));
        SUPPORTED_COINS.add(new Coin("XRP", "Ripple", R.drawable.xrp));
        SUPPORTED_COINS.add(new Coin("DASH", "Dash", R.drawable.dash));
        SUPPORTED_COINS.add(new Coin("XEM", "Nem", R.drawable.xem));
        SUPPORTED_COINS.add(new Coin("XLM", "Stellar", R.drawable.xlm));
        SUPPORTED_COINS.add(new Coin("DOGE", "Doge", R.drawable.doge));
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