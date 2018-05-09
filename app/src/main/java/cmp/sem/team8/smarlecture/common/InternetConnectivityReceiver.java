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

/**
 * Helper to notify clients with internet connectivity states (online - offline)
 * Adapt the internet change receiver to be easy used in the app
 */
public class InternetConnectivityReceiver {

    private OnInternetConnectionChangeListener mOnInternetConnectionChangeListener;
    private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
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

    /**
     * start monitor the connection
     *
     * @param context context of the app
     * @return the class itself as this function can be used like that:
     * mConnectivityReceiver = new InternetConnectivityReceiver(context).start();
     */
    public InternetConnectivityReceiver start(Context context) {
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(connectivityReceiver, intentFilter);
        return this;
    }

    /**
     * End the listening, this should be called so that free the memory
     *
     * @param context context that started the receiving in {@link #start(Context)} method
     */
    public void end(Context context) {
        context.unregisterReceiver(connectivityReceiver);
    }

    public interface OnInternetConnectionChangeListener {
        /**
         * called when connection lost (offline)
         */
        void onInternetConnectionLost();

        /**
         * called when connection back (online)
         */
        void onInternetConnectionBack();
    }
}

