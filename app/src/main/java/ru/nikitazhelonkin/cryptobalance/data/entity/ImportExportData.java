package ru.nikitazhelonkin.cryptobalance.data.entity;


import java.util.ArrayList;
import java.util.List;

public class ImportExportData {

    private List<WalletData> mData;

    public static ImportExportData fromWallets(List<Wallet> wallets) {
        List<WalletData> data = new ArrayList<>();
        for (Wallet w : wallets) {
            WalletData walletData = new WalletData();
            walletData.coin = w.getCoinTicker();
            walletData.address = w.getAddress();
            walletData.alias = w.getAlias();
            data.add(walletData);
        }
        ImportExportData ieData = new ImportExportData();
        ieData.setData(data);
        return ieData;
    }

    public ImportExportData() {

    }

    public void setData(List<WalletData> wallets) {
        mData = wallets;
    }

    public List<WalletData> getData() {
        return mData;
    }

    public static class WalletData {
        public String coin;
        public String address;
        public String alias;
    }


}
