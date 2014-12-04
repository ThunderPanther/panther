package com.thunderpanther.panther;

import java.util.List;

/**
 * Created by Steven on 11/6/2014.
 */
public class User {

    private static User currentUser = null;
    private TaskForest taskList = new TaskForest();

    //TasksSQLiteHelper TDBHelper = new TasksSQLiteHelper(getActivity());

    protected User() {}

    public static final User getCurrentUser() {
        // TODO: Check this out, is singleton the right way to go?
        if (currentUser == null) {
            currentUser = new User();
            //currentUser.loadData(TDBHelper);
        }

        return currentUser;
    }
    public static final User getCurrentUser(TasksSQLiteHelper TDBHelper) {
        // TODO: Check this out, is singleton the right way to go?
        if (currentUser == null) {
            currentUser = new User();
            currentUser.loadData(TDBHelper);
        }

        return currentUser;
    }

    public static final void switchUser() {
        // Magic mumbo jumbo
    }

    protected void loadData(TasksSQLiteHelper TDBHelper) {
        // Load the data from local storage
        // for each task in the database call add task is what I would do but I cant get an
        // application context in this type of class

    }

    public List<TaskPair> getTaskList() {
        return taskList.getTaskList();
    }

    public boolean isTaskListModified() {
        return taskList.isModified();
    }

    public void addTask(Task t) {
        // TODO: Make the parent thing work
        taskList.addTask(t, null);
    }

    public void addTask(Task t, Task p) {
        taskList.addTask(t, p);
    }

    public void removeTask(Task t) {
        taskList.removeTask(t);
    }

    public void storeData() {
        // Called when the application is closed/loses focus?
        // For an offline user, this would just store the data locally

        //for task in task list put them in the database is what I would do but I cant get an
        // application context in this type of class
    }
}
