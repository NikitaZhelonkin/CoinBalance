package ru.nikitazhelonkin.coinbalance.data.db;


import android.arch.persistence.room.TypeConverter;

import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeService;

public class ExchangeServiceConverter {

    @TypeConverter
    public static ExchangeService toService(String name) {
        return ExchangeService.forName(name);
    }

    @TypeConverter
    public static String toString(ExchangeService service) {
        return service.toString();
    }
}
