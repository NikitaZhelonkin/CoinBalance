package ru.nikitazhelonkin.coinbalance.data.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;

@Dao
public interface ExchangeDao {

    @Query("SELECT * FROM exchange")
    Single<List<Exchange>> getAll();

    @Query("SELECT * FROM exchange WHERE mId=:exchangeId")
    Single<Exchange> getById(int exchangeId);

    @Query("DELETE FROM exchange")
    void deleteAll();

    @Insert
    void insert(Exchange exchange);

    @Update
    void update(Exchange exchange);

    @Delete
    void delete(Exchange exchange);

}
