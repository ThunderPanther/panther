package com.thunderpanther.panther;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qmoor_000 on 12/2/2014.
 */
public class TasksSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASKID = "_taskId";
    public static final String COLUMN_TASKNAME = "name";

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table"
            + TABLE_TASKS + "(" + COLUMN_TASKID
            + " integer primary key, " + COLUMN_TASKNAME
            + " text not null);";

    public TasksSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public void addTaskToDB( int id, String name){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASKID, id);
        values.put(COLUMN_TASKNAME, name);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_TASKS, null, values);
        db.close();

    }

    public String taskExists(int id){
        String query = "Select * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASKID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String name;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            name = cursor.getString(1);
            cursor.close();
        } else {
            name = null;
        }
        db.close();
        return name;
    }
}
