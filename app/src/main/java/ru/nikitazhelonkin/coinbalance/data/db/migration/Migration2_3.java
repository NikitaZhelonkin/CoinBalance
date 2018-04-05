package ru.nikitazhelonkin.coinbalance.data.db.migration;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

public class Migration2_3 extends Migration {

    public Migration2_3() {
        super(2, 3);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Token` (`mId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `wallet_address` TEXT, `token_ticker` TEXT, `token_name` TEXT, `balance` REAL NOT NULL, FOREIGN KEY(`wallet_address`) REFERENCES `Wallet`(`address`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE  INDEX `wallet_index` ON `Token` (`wallet_address`)");
    }
}
