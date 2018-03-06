package ru.nikitazhelonkin.coinbalance.ui.text;


import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class Typefaces {

    private static final String FONTS_FOLDER = "fonts/";

    private static final Map<String, Typeface> TYPEFACE_CACHE = new HashMap<String, Typeface>();

    public static Typeface getTypeface(Context context, @StringRes int nameResId) {
        return getTypeface(context, context.getString(nameResId));
    }

    public static Typeface getTypeface(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        } else if (TYPEFACE_CACHE.containsKey(name)) {
            return TYPEFACE_CACHE.get(name);
        } else {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), FONTS_FOLDER + name);
            if (typeface != null) {
                TYPEFACE_CACHE.put(name, typeface);
            }
            return typeface;
        }
    }
}
