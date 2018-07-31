package ru.nikitazhelonkin.coinbalance.data.entity;


import java.util.ArrayList;
import java.util.List;

public class ImportExportData {

    private List<WalletData> mWallets;

    private List<ExchangeData> mExchanges;

    public static ImportExportData from(List<Wallet> wallets, List<Exchange> exchanges) {
        List<WalletData> walletsData = new ArrayList<>();
        for (Wallet w : wallets) {
            WalletData walletData = new WalletData();
            walletData.coin = w.getCoinTicker();
            walletData.address = w.getAddress();
            walletData.alias = w.getAlias();
            walletsData.add(walletData);
        }
        List<ExchangeData> exchangesData = new ArrayList<>();
        for (Exchange e : exchanges) {
            ExchangeData exchangeData = new ExchangeData();
            exchangeData.serviceName = e.getService().getTitle();
            exchangeData.apiKey = e.getApiKey();
            exchangeData.apiSecret = e.getApiSecret();
            exchangeData.title = e.getTitle();
            exchangesData.add(exchangeData);
        }
        ImportExportData ieData = new ImportExportData();
        ieData.setWallets(walletsData);
        ieData.setExchanges(exchangesData);
        return ieData;
    }

    public ImportExportData() {

    }

    public void setWallets(List<WalletData> wallets) {
        mWallets = wallets;
    }

    public List<WalletData> getWallets() {
        return mWallets;
    }

    public void setExchanges(List<ExchangeData> exchanges) {
        mExchanges = exchanges;
    }

    public List<ExchangeData> getExchanges() {
        return mExchanges;
    }

    public static class WalletData {
        public String coin;
        public String address;
        public String alias;
    }

    public static class ExchangeData {
        public String serviceName;
        public String apiKey;
        public String apiSecret;
        public String title;
    }

}
