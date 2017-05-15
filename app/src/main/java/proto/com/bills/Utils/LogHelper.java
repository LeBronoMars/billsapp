package proto.com.bills.utils;

import android.util.Log;

import proto.com.bills.BuildConfig;


/**
 * Created by rsbulanon on 5/15/17.
 */
public class LogHelper {

    public static void log(final String key, final String message) {
        if (BuildConfig.DEBUG) {
            Log.d(key, message);
        }
    }
}
