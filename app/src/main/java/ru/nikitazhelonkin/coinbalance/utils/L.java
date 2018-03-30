package ru.nikitazhelonkin.coinbalance.utils;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import ru.nikitazhelonkin.coinbalance.BuildConfig;

public class L {

    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void e(@Nullable final Throwable e) {
        if (BuildConfig.DEBUG) {
            e("error <> ", e);
        }
    }

    public static void e(@NonNull final Object... o) {
        if (BuildConfig.DEBUG) {
            StringBuilder builder = new StringBuilder("<> ");
            for (Object object : o) {
                builder.append(object);
            }
            e(builder.toString(), null);
        }
    }

    private static void e(@Nullable final String message, @Nullable final Throwable e) {
        if (BuildConfig.DEBUG) {
            StackTraceElement element = trace("e");
            String className;
            StringBuilder builder = new StringBuilder();
            if (element != null) {
                builder.append(element.getMethodName()).append(" ").append(element.getLineNumber()).append(" ").append(message);
                className = element.getClassName();
            } else {
                className = L.class.getName();
            }
            if (e == null) {
                Log.e(className, builder.toString());
            } else {
                Log.e(className, builder.toString(), e);
            }
        }
    }

    @Nullable
    private static StackTraceElement trace(@Nullable final String callingMethodName) {
        StackTraceElement[] e = Thread.currentThread().getStackTrace();
        boolean doNext = false;
        for (StackTraceElement s : e) {
            if (doNext && !s.getMethodName().equals(callingMethodName)) {
                return s;
            }
            doNext = s.getMethodName().equals(callingMethodName);
        }
        return null;
    }
}
