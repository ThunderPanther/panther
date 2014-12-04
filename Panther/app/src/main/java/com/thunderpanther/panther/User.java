package com.thunderpanther.panther;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 11/6/2014.
 */
public class User {

    private static User currentUser = null;
    private static TasksSQLiteHelper db;
    private TaskForest taskList = new TaskForest();
    private Calendar calendar = new Calendar();

    protected User() {}

    public static void setDB(TasksSQLiteHelper db) {
        User.db = db;
    }


    public static final User getCurrentUser() {
        // TODO: Check this out, is singleton the right way to go?
        if (currentUser == null) {
            currentUser = new User();
            currentUser.loadData();
        }

        return currentUser;
    }

    public static final void switchUser() {
        // Magic mumbo jumbo
    }

    protected void loadData() {
        // Load the data from local storage

        loadTasks(Application.getDB());
        loadWorkSessions(Application.getDB());
    }

    public Task getTask(int id) {
        return taskList.getTask(id);
    }

    public List<TaskPair> getTaskList() {
        return taskList.getTaskList();
    }

    public List<Task> getTaskRefList() { return taskList.getTaskRefList(); }

    public boolean isTaskListModified() {
        return taskList.isModified();
    }

    public void addTask(Task t) {
        // TODO: Make the parent thing work
        taskList.addTask(t, null);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void addTask(Task t, Task p) {
        taskList.addTask(t, p);
    }

    public void removeTask(int id) {
        calendar.removeReferringWorkSessions(id);
        taskList.removeTask(id);
    }

    public void storeData() {
        // Called when the application is closed/loses focus?
        // For an offline user, this would just store the data locally
    }

    public void loadTasks(TasksSQLiteHelper TDBHelper) {
        // Load the data from local storage
        // for each task in the database call add task is what I would do but I cant get an
        // application context in this type of class
        taskList = TDBHelper.getUserTasks();

    }

    public void loadWorkSessions(TasksSQLiteHelper TDBHelper) {
        calendar = TDBHelper.getUserCalendar();
    }

    public void scheduleWorkSession(WorkSession w) {
        calendar.addWorkSession(w);
    }

    public void invalidateTaskList() {
        taskList.invalidate();
    }
}
