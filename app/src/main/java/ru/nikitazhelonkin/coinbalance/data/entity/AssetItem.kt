package ru.nikitazhelonkin.coinbalance.data.entity


class AssetItem(val coin: String,
                val balance: Float,
                val currency: String,
                val price: Float,
                val change24: Float,
                val percent: Float) {

    val currencyBalance: Float = balance * price

}
