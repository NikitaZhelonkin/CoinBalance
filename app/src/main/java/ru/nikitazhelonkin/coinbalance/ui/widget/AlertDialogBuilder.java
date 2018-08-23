package ru.nikitazhelonkin.coinbalance.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.TypedValue;

import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.ui.text.FontSpan;

/**
 * AlertDialog.Builder extension to support dialog title font styling
 */
public class AlertDialogBuilder extends AlertDialog.Builder {

    private int mTitleFontResId;

    public AlertDialogBuilder(@NonNull Context context) {
        super(context);
        init(context, 0);
    }

    public AlertDialogBuilder(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context, themeResId);
    }

    private void init(Context context, int themeResId) {
        Context dialogContext = new ContextThemeWrapper(context, resolveDialogTheme(context, themeResId));

        mTitleFontResId = resolveTitleFontFromTheme(dialogContext);
    }

    @Override
    public AlertDialog.Builder setTitle(@StringRes int titleId) {
        return setTitle(getContext().getString(titleId));
    }

    @Override
    public AlertDialog.Builder setTitle(CharSequence title) {
        return super.setTitle(applyFont(title, mTitleFontResId));
    }

    private CharSequence applyFont(CharSequence text, int fontResId) {
        if (!TextUtils.isEmpty(text) && isRealResId(fontResId)) {
            return applyFontSpan(getContext(),new SpannableString(text), text.toString(), fontResId);
        }
        return text;
    }

    private static Spannable applyFontSpan(Context context, Spannable spannable, String toSpan, int fontResId) {
        FontSpan span = new FontSpan(ResourcesCompat.getFont(context, fontResId));
        int start = spannable.toString().indexOf(toSpan);
        int end = start + toSpan.length();
        spannable.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannable;
    }

    private static int resolveDialogTheme(@NonNull Context context, @StyleRes int resId) {
        if (isRealResId(resId)) {
            return resId;
        } else {
            return resolveAttribute(context, android.support.v7.appcompat.R.attr.alertDialogTheme);
        }
    }

    private static int resolveTitleFontFromTheme(Context context) {
        int buttonBarButtonStyle = resolveAttribute(context, android.R.attr.windowTitleStyle);
        return obtainStyledAttribute(context, buttonBarButtonStyle, R.attr.fontFamily);
    }


    private static int resolveAttribute(Context context, int attrResId) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(attrResId, outValue, true);
        return outValue.resourceId;
    }

    private static int obtainStyledAttribute(Context context, int styleResId, int attrResId) {
        TypedArray a = context.getTheme().obtainStyledAttributes(styleResId, new int[]{attrResId});
        int styledAttribute = a.getResourceId(0, 0);
        a.recycle();
        return styledAttribute;
    }

    private static boolean isRealResId(int resId) {
        return resId >= 0x01000000;
    }

}