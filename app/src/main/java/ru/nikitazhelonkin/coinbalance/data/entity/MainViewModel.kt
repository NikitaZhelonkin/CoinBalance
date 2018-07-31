package ru.nikitazhelonkin.coinbalance.data.entity


import ru.nikitazhelonkin.coinbalance.data.api.response.Prices
import java.util.*

class MainViewModel(val wallets: List<Wallet>,
                    val exchanges: List<Exchange>,
                    private val tokens: List<Token>,
                    private val exchangeBalances: List<ExchangeBalance>,
                    private val prices: Prices) {

    val items: List<ListItem>

    val assets: List<AssetItem>

    val currency: String = prices.currency

    init {
        items = buildDataList()
        assets = buildAssetList()
    }

    private fun buildDataList(): List<ListItem> {
        val items = ArrayList<ListItem>()
        items.addAll(wallets)
        items.addAll(exchanges)
        items.sortWith(Comparator { t1, t2 ->
            if (t2.position != -1 && t1.position != -1) {
                t1.position - t2.position
            } else if (t2.position == -1 || t1.position == -1) {
                if (t1.position == -1) 1 else -1
            } else {
                t1.createdAt.compareTo(t2.createdAt)
            }
        })
        return items
    }

    private fun buildAssetList(): List<AssetItem> {
        val balances = HashMap<String, Float>()
        for (w in wallets) {
            if (w.coinTicker == null) continue
            val balance = balances[w.coinTicker]
            balances[w.coinTicker] = if (balance == null) w.balance else balance + w.balance

            val tokenList = getTokens(w.address)
            for (t in tokenList!!) {
                if (t.tokenTicker == null) continue
                val tBalance = balances[t.tokenTicker]
                balances[t.tokenTicker] = if (tBalance == null) t.balance else tBalance + t.balance
            }
        }
        for (e in exchanges) {
            val ebalances = getExchangeBalances(e.id)
            for (b in ebalances!!) {
                if (b.coinTicker == null) continue
                val balance = balances[b.coinTicker]
                balances[b.coinTicker] = if (balance == null) b.balance else balance + b.balance
            }
        }
        val items = ArrayList<AssetItem>()
        for (coin in balances.keys) {
            val balance = balances[coin] ?: continue
            val price = getPrice(coin)
            val priceValue = price?.price ?: 0f
            val change24 = if (price == null) 0f else price.change24 / price.open24 * 100
            val currencyBalance = balance * priceValue
            items.add(AssetItem(coin,
                    balance,
                    currency,
                    priceValue, change24,
                    currencyBalance / getTotalBalance() * 100))

        }
        items.sortWith(Comparator { a1, a2 ->
            val b1 = a1.currencyBalance
            val b2 = a2.currencyBalance
            b2.compareTo(b1)
        })
        return items
    }

    fun calculateChange24Hours(): Float {
        val totalValue24Hours = assets.fold(0f) { t, assetItem -> t + assetItem.currencyBalance / (1 + assetItem.change24 / 100f) }
        val totalValue = assets.fold(0f) { t, assetItem -> t + assetItem.currencyBalance }
        return (totalValue - totalValue24Hours) / totalValue24Hours * 100
    }

    fun getItem(position: Int): ListItem {
        return items[position]
    }

    fun getCoin(coin: String): Coin? {
        return Coin.forTicker(coin)
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
    }

    fun getWalletBalanceWithTokens(wallet: Wallet): Float {
        return wallet.balance * getPriceValue(wallet.coinTicker) + getTokensBalance(wallet)
    }

    fun getTokens(walletAddress: String?): List<Token>? {
        return tokens.filter { walletAddress == it.walletAddress }
    }

    private fun getTokensBalance(wallet: Wallet): Float {
        return getTokens(wallet.address)?.fold(0f) { b, token -> b + token.balance * getPriceValue(token.tokenTicker) }
                ?: 0f;
    }

    fun getExchangeBalances(exchangeId: Int): List<ExchangeBalance>? {
        return exchangeBalances.filter { it.exchangeId == exchangeId }
    }

    fun getBalance(balances: List<ExchangeBalance>): Float {
        return balances.fold(0f) { b, eBalance -> b + eBalance.balance * getPriceValue(eBalance.coinTicker) }
    }

    fun getPrice(coin: String): Prices.Price? = prices.getPrice(coin)

    fun getPriceValue(coin: String?): Float = prices.getPriceValue(coin)

    fun getTotalBalance(): Float = getWalletsBalance() + getExchangeBalances()

    private fun getWalletsBalance(): Float {
        return wallets.fold(0f) { b, wallet -> b + getWalletBalanceWithTokens(wallet); }
    }

    private fun getExchangeBalances(): Float = getBalance(exchangeBalances)

}
