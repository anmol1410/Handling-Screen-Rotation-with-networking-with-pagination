package com.android.anmol.githubapi;

import android.util.Log;

public class MyLog {

    private static final boolean IS_DEBUG = true;
    private static final String TAG_IDENTIFIER = "ABCDE";

    static void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(tag + TAG_IDENTIFIER, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (IS_DEBUG) {
            Log.e(tag + TAG_IDENTIFIER, msg);
        }
    }
}
