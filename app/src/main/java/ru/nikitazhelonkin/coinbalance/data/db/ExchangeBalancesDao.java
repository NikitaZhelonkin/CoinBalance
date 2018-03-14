package ru.nikitazhelonkin.coinbalance.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeBalance;

@Dao
public interface ExchangeBalancesDao {

    @Query("SELECT * FROM exchangebalance")
    Single<List<ExchangeBalance>> getBalances();

    @Query("SELECT * FROM exchangebalance WHERE exchange_id=:exchange_id")
    Single<List<ExchangeBalance>> getBalances(int exchange_id);

    @Insert
    void insert(Iterable<ExchangeBalance> balances);

    @Query("DELETE FROM exchangebalance WHERE exchange_id=:exchange_id")
    void delete(int exchange_id);
}
