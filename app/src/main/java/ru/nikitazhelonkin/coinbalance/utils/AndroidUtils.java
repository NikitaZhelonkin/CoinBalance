package ru.nikitazhelonkin.coinbalance.utils;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import ru.nikitazhelonkin.coinbalance.R;

public class AndroidUtils {

    public static void hideKeyboard(Context context, IBinder iBinder) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(iBinder, 0);
    }

    public static void openMarket(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context.getString(R.string.market_uri, context.getPackageName()))));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context.getString(R.string.play_store_uri, context.getPackageName()))));
        }
    }

    public static void share(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        try {
            context.startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            //do nothing
        }
    }

    public static void sendEmail(Context context, String reportTo, String reportSubject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", reportTo, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, reportSubject);
        intent.putExtra(Intent.EXTRA_TEXT, "");
        try {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_email)));
        } catch (ActivityNotFoundException e) {
            //do nothing
        }
    }

    public static void showFileChooser(Activity activity, String title, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            activity.startActivityForResult(
                    Intent.createChooser(intent, title), requestCode);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(activity, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void onPreDraw(final View view, final Runnable runnable) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final ViewTreeObserver observer = view.getViewTreeObserver();
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }
                runnable.run();
                return true;
            }
        });
    }

}
