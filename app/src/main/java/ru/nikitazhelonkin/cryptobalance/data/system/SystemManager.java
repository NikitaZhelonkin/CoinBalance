package ru.nikitazhelonkin.cryptobalance.data.system;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

public class SystemManager {

    private Context mContext;

    @Inject
    public SystemManager(Context context) {
        mContext = context;
    }

    public boolean isConnected() {
        NetworkInfo activeNetwork = getActiveNetworkInfo(mContext);
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm == null ? null : cm.getActiveNetworkInfo();
    }
}
