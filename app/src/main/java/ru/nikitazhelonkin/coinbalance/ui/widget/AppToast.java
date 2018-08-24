package ru.nikitazhelonkin.coinbalance.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.nikitazhelonkin.coinbalance.R;

public class AppToast {

    public static Toast make(@NonNull Context context, @StringRes int stringResId, int duration){
        return make(context, context.getString(stringResId), duration);
    }

    public static Toast make(@NonNull Context context, @NonNull CharSequence text, int duration) {
        @SuppressLint("ShowToast") Toast toast = Toast.makeText(context, text, duration);
        View snackBarView = toast.getView();
        snackBarView.setBackgroundResource(R.drawable.bg_toast);
        TextView textView = snackBarView.findViewById(android.R.id.message);
        textView.setGravity(Gravity.CENTER);
        textView.setShadowLayer(0, 0, 0, 0);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_condensed_regular));

        int offset = (int) (context.getResources().getDisplayMetrics().density);
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, offset);
        return toast;
    }
}
