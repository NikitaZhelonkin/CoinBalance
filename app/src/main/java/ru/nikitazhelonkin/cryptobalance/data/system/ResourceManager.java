package ru.nikitazhelonkin.cryptobalance.data.system;


import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;

import javax.inject.Inject;

public class ResourceManager {

    private Context mContext;

    @Inject
    public ResourceManager(Context context){
        mContext = context;
    }

    @NonNull
    public String getString(@StringRes int resId){
        return mContext.getString(resId);
    }

    @NonNull
    public String getString(@StringRes int resId, Object... formatArgs){
        return mContext.getString(resId, formatArgs);
    }

    @NonNull
    public String getQuantityString(@PluralsRes int id, int quantity, Object... formatArgs){
        return mContext.getResources().getQuantityString(id, quantity, formatArgs);
    }

    @NonNull
    public String[] getStringArray(@ArrayRes int id){
        return mContext.getResources().getStringArray(id);
    }
}
