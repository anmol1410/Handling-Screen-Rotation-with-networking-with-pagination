package com.android.anmol.githubapi.utility.logging;

import android.util.Log;

/**
 * Logging utility.
 */
public class MyLog {

    // Change this to disable logs.
    private static final boolean IS_DEBUG = true;

    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (IS_DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (IS_DEBUG) {
            Log.i(tag, msg);
        }
    }
}
