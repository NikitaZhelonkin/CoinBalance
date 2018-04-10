package ru.nikitazhelonkin.coinbalance.data.db.migration;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

public class Migration3_4 extends Migration {

    public Migration3_4() {
        super(3, 4);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase _db) {
        _db.execSQL("ALTER TABLE `Exchange` ADD `error_message` TEXT;");
    }
}
