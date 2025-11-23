package com.cloudmemo.app.sync;

import android.content.Context;
import android.content.SharedPreferences;

public class SyncPreferences {

    private static final String PREF_NAME = "cloudmemo_sync";
    private static final String KEY_LAST_SYNC = "last_sync";

    public static long getLastSyncTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getLong(KEY_LAST_SYNC, 0);
    }

    public static void setLastSyncTime(Context context, long time) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putLong(KEY_LAST_SYNC, time).apply();
    }
}
