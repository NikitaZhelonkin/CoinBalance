package ru.nikitazhelonkin.coinbalance.data.entity


import ru.nikitazhelonkin.coinbalance.data.api.response.Prices

class WalletDetailViewModel(val wallet: Wallet, val tokenList: List<Token>, val prices: Prices) {

    val price: Float = prices.getPriceValue(wallet.coinTicker)

    val currency: String = prices.currency
}
