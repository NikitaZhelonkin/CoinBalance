package ru.nikitazhelonkin.coinbalance.data.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;

@Database(entities = Wallet.class, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WalletDao userDao();

}
