package cmp.sem.team8.smarlecture.joinsession.writeattendance;

/**
 * Created by AmmarRabie on 31/03/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * This class detects the internet connection or airplane mode state transaction (on->off, off->on).
 * notify subscribers with on all connection lost (airplane mode on + no internet connection),
 * and also with on connection back (airplane mode or internet).
 * This class is used to detect
 */
public class ConnectionDetector {

    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private OnConnectionChangeListener mListener;
    private Context mContext;

    private boolean mInternetState;
    private boolean mAirplaneState;

    private boolean mIsStarted = false;


    private BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Intent.ACTION_AIRPLANE_MODE_CHANGED:
                    boolean isTurnedOn = intent.getBooleanExtra("state", false);
                    if (isTurnedOn) {
                        onAirplaneModeActivated();
                        break;
                    }
                    onAirplaneModeDeactivated();
                    break;

                case ACTION_CONNECTIVITY_CHANGE:
                    if (isOnline()) {
                        onInternetConnectionActivated();
                        break;
                    }
                    onInternetConnectionDeactivated();
                    break;
            }

        }


    };

    public ConnectionDetector(Context context, OnConnectionChangeListener onConnectionChangeListener) {
        mListener = onConnectionChangeListener;
        mContext = context;

        mAirplaneState = isAirplaneModeOn();
        mInternetState = isOnline();
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private boolean isAirplaneModeOn() {
        return Settings.System.getInt(mContext.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    private void onInternetConnectionDeactivated() {
        mInternetState = false;
        refreshSubscribers();
    }


    private void onInternetConnectionActivated() {
        mInternetState = true;
        refreshSubscribers();
    }


    private void onAirplaneModeDeactivated() {
        mAirplaneState = false;
        refreshSubscribers();
    }


    private void onAirplaneModeActivated() {
        mAirplaneState = true;
        refreshSubscribers();
    }

    public void start() {
        if (mIsStarted)
            return;
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        mContext.registerReceiver(mConnectionReceiver, intentFilter);
    }

    public void end() {
        if (!mIsStarted)
            return;
        mIsStarted = false;
        mContext.unregisterReceiver(mConnectionReceiver);
    }

    public boolean isConnected() {
        return mAirplaneState && mInternetState;
    }


    private void refreshSubscribers() {
        if (!mIsStarted)
            return;
        if (mAirplaneState && mInternetState)
            mListener.onConnectionBack();
        else if (!mAirplaneState && !mInternetState)
            mListener.onConnectionLost();
    }

    interface OnConnectionChangeListener {
        void onConnectionLost();

        void onConnectionBack();
    }


}
