package ru.nikitazhelonkin.coinbalance.data.entity


import ru.nikitazhelonkin.coinbalance.data.api.response.Prices
import ru.nikitazhelonkin.coinbalance.utils.ListUtils

class ExchangeDetailViewModel(val exchange: Exchange, val exchangeBalanceList: List<ExchangeBalance>, val prices: Prices) {

    fun getBalance(balances: List<ExchangeBalance>): Float {
        return balances.fold(0f) { b, exchangeBalance -> b + exchangeBalance.balance * getPriceValue(exchangeBalance.coinTicker) }
    }

    fun getPriceValue(coin: String?): Float {
        return prices.getPriceValue(coin)
    }
}
