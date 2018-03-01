package ru.nikitazhelonkin.cryptobalance.data.api.client;


import io.reactivex.Single;

public interface ApiClient {

    Single<String> getBalance(String address);
}
