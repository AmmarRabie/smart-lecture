package cmp.sem.team8.smarlecture.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by AmmarRabie on 06/04/2018.
 */


public class InternetConnectivityReceiver {

    OnInternetConnectionChangeListener mOnInternetConnectionChangeListener;
    BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isOnline = activeNetwork != null && activeNetwork.isConnected();
                if (isOnline) {
                    mOnInternetConnectionChangeListener.onInternetConnectionBack();
                    return;
                }
                mOnInternetConnectionChangeListener.onInternetConnectionLost();
            }
        }
    };


    public InternetConnectivityReceiver(OnInternetConnectionChangeListener
                                                onInternetConnectionChangeListener) {
        mOnInternetConnectionChangeListener = onInternetConnectionChangeListener;
    }

    public InternetConnectivityReceiver start(Context context) {
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(connectivityReceiver, intentFilter);
        return this;
    }

    public void end(Context context) {
        context.unregisterReceiver(connectivityReceiver);
    }

    public interface OnInternetConnectionChangeListener {
        void onInternetConnectionLost();
        void onInternetConnectionBack();
    }
}

