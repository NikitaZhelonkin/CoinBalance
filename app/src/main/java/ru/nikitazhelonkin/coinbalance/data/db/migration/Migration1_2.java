package ru.nikitazhelonkin.coinbalance.data.db.migration;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

public class Migration1_2 extends Migration {

    public Migration1_2() {
        super(1, 2);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Exchange` (`mId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `service` TEXT, `api_key` TEXT, `api_secret` TEXT, `title` TEXT, `position` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `status` INTEGER NOT NULL)");
        _db.execSQL("CREATE UNIQUE INDEX `exchange_index` ON `Exchange` (`service`, `api_key`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ExchangeBalance` (`mId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `exchange_id` INTEGER NOT NULL, `coin_ticker` TEXT, `balance` REAL NOT NULL, FOREIGN KEY(`exchange_id`) REFERENCES `Exchange`(`mId`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE INDEX `exchange_id_index` ON `ExchangeBalance` (`exchange_id`)");
    }
}
