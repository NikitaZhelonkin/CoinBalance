package ru.nikitazhelonkin.coinbalance.data.entity


import android.support.annotation.DrawableRes

import ru.nikitazhelonkin.coinbalance.R

enum class ExchangeService constructor(val title: String, @field:DrawableRes val iconResId: Int) {

    BITFINEX("Bitfinex", R.drawable.bitfinex),
    BITTREX("Bittrex", R.drawable.bittrex),
    BINANCE("Binance", R.drawable.binance),
    COINBASE("Coinbase", R.drawable.coinbase),
    GEMINI("Gemini", R.drawable.gemini),
    HITBTC("HitBTC", R.drawable.hitbtc),
    KRAKEN("Kraken", R.drawable.kraken),
    POLONIEX("Poloniex", R.drawable.poloniex),
    YOBIT("YoBit", R.drawable.yobit),
    WEX("Wex", R.drawable.wex);

    override fun toString(): String {
        return title
    }

    companion object {

        @JvmStatic fun forName(name: String): ExchangeService? {
            for (service in values()) {
                if (service.title.equals(name, ignoreCase = true)) {
                    return service
                }
            }
            return null
        }
    }
}
