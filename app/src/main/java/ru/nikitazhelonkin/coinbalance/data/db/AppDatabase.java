package ru.nikitazhelonkin.coinbalance.data.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeBalance;
import ru.nikitazhelonkin.coinbalance.data.entity.Token;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;

@Database(entities = {Wallet.class, Exchange.class, ExchangeBalance.class, Token.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WalletDao userDao();

    public abstract ExchangeDao exchangeDao();

    public abstract ExchangeBalancesDao exchangeBalancesDao();

    public abstract TokenDao tokenDao();
}
