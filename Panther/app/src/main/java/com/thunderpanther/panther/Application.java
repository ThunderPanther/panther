package com.thunderpanther.panther;

import android.content.Context;

/**
 * Created by Steven on 12/4/2014.
 */
public class Application {
    private Application() {}
    private static TasksSQLiteHelper db = null;

    public static void initDB(Context context) {
        if (db == null) {
            db = new TasksSQLiteHelper(context);
        }
    }

    public static TasksSQLiteHelper getDB() {
        return db;
    }
}
