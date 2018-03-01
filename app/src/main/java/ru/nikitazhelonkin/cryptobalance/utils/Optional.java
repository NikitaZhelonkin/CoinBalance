package ru.nikitazhelonkin.cryptobalance.utils;

import android.support.annotation.Nullable;

/**
 * Created by nikita on 11.10.17.
 */

public class Optional<T> {

    private final T optional;

    public static <T> Optional<T> from(T value) {
        return new Optional<>(value);
    }

    private Optional(@Nullable T optional) {
        this.optional = optional;
    }

    public boolean isEmpty() {
        return optional == null;
    }

    @Nullable
    public T get() {
        return optional;
    }
}
