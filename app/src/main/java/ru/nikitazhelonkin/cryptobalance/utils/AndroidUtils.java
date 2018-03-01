package ru.nikitazhelonkin.cryptobalance.utils;


import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

public class AndroidUtils {

    public static void hideKeyboard(Context context, IBinder iBinder) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(iBinder, 0);
    }
}
