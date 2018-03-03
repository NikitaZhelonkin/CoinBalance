package ru.nikitazhelonkin.cryptobalance.data.system;


import android.content.ClipData;
import android.content.Context;

import javax.inject.Inject;

public class ClipboardManager {

    private Context mContext;

    private android.content.ClipboardManager mClipboardManager;

    @Inject
    public ClipboardManager(Context context){
        mContext = context;
        mClipboardManager = (android.content.ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public void copyToClipboard(String text){
        ClipData clip = ClipData.newPlainText(mContext.getPackageName(), text);
        mClipboardManager.setPrimaryClip(clip);
    }

}
