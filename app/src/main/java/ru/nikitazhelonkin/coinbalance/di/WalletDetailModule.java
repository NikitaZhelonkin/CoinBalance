package ru.nikitazhelonkin.coinbalance.di;


import dagger.Module;
import dagger.Provides;

@Module
public class WalletDetailModule {

    private int mWalletId;

    public WalletDetailModule(int walletId){
        mWalletId = walletId;
    }

    @Provides
    public int getWalletId() {
        return mWalletId;
    }
}
