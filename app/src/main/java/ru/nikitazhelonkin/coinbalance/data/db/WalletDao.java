package ru.nikitazhelonkin.coinbalance.data.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;

@Dao
public interface WalletDao {

    @Query("SELECT * FROM wallet")
    Single<List<Wallet>> getAll();

    @Query("SELECT * FROM wallet WHERE mId=:walletId")
    Single<Wallet> getById(int walletId);

    @Insert
    void insert(Wallet wallet);

    @Update
    void update(Wallet wallet);

    @Delete
    void delete(Wallet wallet);
}
