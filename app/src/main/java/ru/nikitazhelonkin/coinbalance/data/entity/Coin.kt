package ru.nikitazhelonkin.coinbalance.data.entity


import android.support.annotation.DrawableRes

import ru.nikitazhelonkin.coinbalance.R

enum class Coin constructor(val ticker: String, val title: String, @param:DrawableRes val iconResId: Int) {

    BTC("BTC", "Bitcoin", R.drawable.btc),
    BCH("BCH", "Bitcoin Cash", R.drawable.bch),
    ETH("ETH", "Ethereum", R.drawable.eth),
    ETC("ETC", "Ethereum Classic", R.drawable.etc),
    LTC("LTC", "Litecoin", R.drawable.ltc),
    XRP("XRP", "Ripple", R.drawable.xrp),
    DASH("DASH", "Dash", R.drawable.dash),
    XEM("XEM", "Nem", R.drawable.xem),
    XLM("XLM", "Stellar", R.drawable.xlm),
    ADA("ADA", "Cardano", R.drawable.ada),
    NEO("NEO", "Neo", R.drawable.neo),
    ZEC("ZEC", "ZCash", R.drawable.zec),
    DOGE("DOGE", "Doge", R.drawable.doge);

    override fun toString(): String {
        return ticker
    }

    companion object {

        @JvmStatic fun forTicker(ticker: String): Coin? {
            for (coin in values()) {
                if (coin.ticker.equals(ticker, ignoreCase = true)) {
                    return coin
                }
            }
            return null
        }
    }
}
