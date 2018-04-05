package ru.nikitazhelonkin.coinbalance.data.entity;


import java.util.List;

import ru.nikitazhelonkin.coinbalance.data.api.response.Prices;
import ru.nikitazhelonkin.coinbalance.utils.ListUtils;

public class ExchangeDetailViewModel {

    private Exchange mExchange;

    private List<ExchangeBalance> mExchangeBalanceList;

    private Prices mPrices;

    public ExchangeDetailViewModel(Exchange exchange, List<ExchangeBalance> exchangeBalances, Prices prices) {
        mExchange = exchange;
        mExchangeBalanceList = exchangeBalances;
        mPrices = prices;
    }

    public Exchange getExchange() {
        return mExchange;
    }

    public List<ExchangeBalance> getExchangeBalanceList() {
        return mExchangeBalanceList;
    }

    public Prices getPrices() {
        return mPrices;
    }

    public float getBalance(List<ExchangeBalance> balances) {
        return ListUtils.reduce(balances, 0f,
                (b, exchangeBalance) -> b + exchangeBalance.getBalance() * getPriceValue(exchangeBalance.getCoinTicker())
        );
    }

    public float getPriceValue(String coin) {
        return mPrices.getPriceValue(coin);
    }
}
