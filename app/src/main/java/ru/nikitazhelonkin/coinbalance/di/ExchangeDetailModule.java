package ru.nikitazhelonkin.coinbalance.di;


import dagger.Module;
import dagger.Provides;

@Module
public class ExchangeDetailModule {

    private int mExchangeId;

    public ExchangeDetailModule(int exchangeId){
        mExchangeId = exchangeId;
    }

    @Provides
    public int getExchangeId() {
        return mExchangeId;
    }
}
