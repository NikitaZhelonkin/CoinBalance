package ru.nikitazhelonkin.coinbalance.data.entity;


public class AssetItem {

    private String coin;
    private float balance;
    private String currency;
    private float percent;
    private float price;
    private float change24;

    public AssetItem(String coin, float balance, String currency, float price, float change24, float percent) {
        this.coin = coin;
        this.balance = balance;
        this.currency = currency;
        this.price = price;
        this.percent = percent;
        this.change24 = change24;

    }

    public float getChange24() {
        return change24;
    }

    public float getPrice() {
        return price;
    }

    public String getCoin() {
        return coin;
    }

    public float getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public float getPercent() {
        return percent;
    }

    public float getCurrencyBalance() {
        return balance * price;
    }

}
