package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import io.reactivex.Single;

public interface ApiClient {

    Single<String> getBalance(String address);
}
