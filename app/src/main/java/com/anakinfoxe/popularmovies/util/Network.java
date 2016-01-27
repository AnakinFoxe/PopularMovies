package com.anakinfoxe.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by xing on 1/26/16.
 */
public class Network {

    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ani = cm.getActiveNetworkInfo();
        if (ani != null) {
            return (ani.getType() == ConnectivityManager.TYPE_WIFI
                    || ani.getType() == ConnectivityManager.TYPE_MOBILE);
        }

        return false;
    }
}
