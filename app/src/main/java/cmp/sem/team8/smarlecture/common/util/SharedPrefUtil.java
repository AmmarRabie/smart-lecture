package cmp.sem.team8.smarlecture.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by AmmarRabie on 09/05/2018.
 */

public final class SharedPrefUtil {
    private static final String PREF_NAME = "myAppPref";

    private static final String KEY_FIRST_TIME = "first-time";

    public static boolean isFirstTimeOpenApp(Context context) {
        return getPref(context).getBoolean(KEY_FIRST_TIME, true);
    }


    public static void openAppOneTime(Context context) {
        getEditor(context).putBoolean(KEY_FIRST_TIME, false).commit();
    }

    private static SharedPreferences getPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
//        .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getPref(context).edit();
    }
}
