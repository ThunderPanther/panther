package com.thunderpanther.panther;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by qmoor_000 on 12/2/2014.
 */
public class TasksSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASKID = "_taskId";
    public static final String COLUMN_PARENTID = "parentId";
    public static final String COLUMN_TASKNAME = "name";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_ESTIMATE = "estimate";

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_TASKS + "(" + COLUMN_TASKID
            + " INTEGER PRIMARY KEY, " + COLUMN_TASKNAME
            + " TEXT NOT NULL, " + COLUMN_WEIGHT
            + " INTEGER, " + COLUMN_ESTIMATE
            + " INTEGER, " + COLUMN_PARENTID +" INTEGER);";

    public TasksSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("TasksSQLiteHelperDB","Creating Database");
        db.execSQL(DATABASE_CREATE);

        Log.i("TasksSQLiteHelperDB","Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public void addTaskToDB( int id, String name, int weight, int estimate, int parentId){
        Log.i("TasksSQLiteHelperDB","Adding Task To DB");
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASKID, id);
        values.put(COLUMN_TASKNAME, name);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_ESTIMATE, estimate);
        values.put(COLUMN_PARENTID, parentId);
        Log.i("Add Task To DB","Getting Writable Database");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("Add Task To DB","Got Writable Database ");
        db.insert(TABLE_TASKS, null, values);

        Log.i("TasksSQLiteHelperDB","Task Added To DB");
        db.close();

    }

    public TaskForest getUserTasks(){
        TaskForest taskList = new TaskForest();
        String query = "Select * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = cursorToTask(cursor);
            taskList.addTask(task, null);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        db.close();
        return taskList;
    }

    private Task cursorToTask(Cursor cursor){
        int id = Integer.parseInt(cursor.getString(0));
        String name = cursor.getString(1);
        int weight = Integer.parseInt(cursor.getString(2));
        int estimate = Integer.parseInt(cursor.getString(3));
        int parentId = Integer.parseInt(cursor.getString(4));
        Task task = new Task(id, name, weight, estimate);
        return task;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_TASKS);
        return numRows;
    }
//    public String findTask(int id){
//        String query = "Select * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASKID + " = " + id;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Cursor cursor = db.rawQuery(query, null);
//
//        String name;
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            Task task = cursorToTask(cursor);
//
//            cursor.moveToNext();
//        }
//        // make sure to close the cursor
//        cursor.close();
//        db.close();
//        return name;
//    }
}
