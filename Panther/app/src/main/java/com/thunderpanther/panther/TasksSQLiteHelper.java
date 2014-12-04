package com.thunderpanther.panther;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qmoor_000 on 12/2/2014.
 */
public class TasksSQLiteHelper extends SQLiteOpenHelper {

    //Tasks
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASKID = "_taskId";
    public static final String COLUMN_PARENTID = "parentId";
    public static final String COLUMN_TASKNAME = "name";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_ESTIMATE = "estimate";
    //CurrentId
    public static final String TABLE_CURRENTID = "current";
    public static final String COLUMN_ID = "_Id";
    public static final String COLUMN_NEXTID = "nextId";

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_TASKS + "(" + COLUMN_TASKID + " INTEGER PRIMARY KEY, "
            + COLUMN_TASKNAME+ " TEXT NOT NULL, "
            + COLUMN_WEIGHT+ " INTEGER, "
            + COLUMN_ESTIMATE+ " DOUBLE, "
            + COLUMN_PARENTID +" INTEGER);";

    private static final String DATABASE_CREATE2 = "CREATE TABLE "
            + TABLE_CURRENTID + " (" + COLUMN_ID +" INTEGER PRIMARY KEY, "+ COLUMN_NEXTID + " INTEGER);";

    public TasksSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("TasksSQLiteHelperDB","Creating Database");
        db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE2);

        Log.i("TasksSQLiteHelperDB","Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENTID);
        onCreate(db);
    }

    public void addTaskToDB( int id, String name, int weight, double estimate, int parentId){
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

        ContentValues values2 = new ContentValues();
        values2.put(COLUMN_ID, 1);
        values2.put(COLUMN_NEXTID, id+1);
        db.replace(TABLE_CURRENTID, null, values2);

        Log.i("TasksSQLiteHelperDB","Task Added To DB");
        db.close();

    }

    public boolean deleteTask(int id){
        Log.i("TasksSQLiteHelperDB","Deletin' Shit");
        boolean result = false;
        String query = "Select * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASKID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            String delid = String.valueOf(id);
            db.delete(TABLE_TASKS, COLUMN_TASKID + " = "+ delid,null);
//                    new String[] { String.valueOf(id) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public int getNextId(){
        String query = "Select * FROM " + TABLE_CURRENTID;
        SQLiteDatabase db = this.getWritableDatabase();

        int nextId = 0;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            nextId = Integer.parseInt(cursor.getString(1));
        }

        cursor.close();
        db.close();
        return nextId;
    }

    public TaskForest getUserTasks(){
        Log.i("TasksSQLiteHelperDB","Getting Tasks From DB");
        TaskForest taskList = new TaskForest();
        String query = "Select * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();

        Map<Integer, Task> taskMap = new HashMap<Integer, Task>();
        Map<Integer, Integer> parentMap = new HashMap<Integer, Integer>();



        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = cursorToTask(cursor);
           // taskList.addTask(task, null);
            taskMap.put(task.getID(),task);
            parentMap.put(task.getID(),Integer.parseInt(cursor.getString(4)));

            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        db.close();

        if(!taskMap.isEmpty()) {
            for (int key : taskMap.keySet()) {
                if (parentMap.get(key) >= 0) {
                    Task parent = taskMap.get(parentMap.get(key));
                    if (parent != null) {
                        parent.addChild(taskMap.get(key));
                    }
                } else {
                    taskList.addRoot(taskMap.get(key));
                }
            }
        }
        Log.i("TasksSQLiteHelperDB","Returning Tasks From DB");
        return taskList;
    }

    private Task cursorToTask(Cursor cursor){
        int id = Integer.parseInt(cursor.getString(0));
        String name = cursor.getString(1);
        int weight = Integer.parseInt(cursor.getString(2));
        int estimate = Integer.parseInt(cursor.getString(3));//PARSE DOUBLE
        int parentId = Integer.parseInt(cursor.getString(4));
        Task task = new Task(name, weight, estimate, id);
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