package com.thunderpanther.panther;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 11/6/2014.
 */
public class User {

    private static User currentUser = null;
    private TaskForest taskList = new TaskForest();

    protected User() {}

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

        // TODO: Don't actually do this
        // TEST TEST TEST TEST TEST

        Task rootTask1 = new Task("Test root task", 0, 0);
        currentUser.addTask(rootTask1);

        Task taskTree[] = new Task[17];
        taskTree[1] = new Task("Task tree task 1", 0, 0);
        currentUser.addTask(taskTree[1]);
        for (int i = 2; i < 17; i++) {
            taskTree[i] = new Task("Task tree task" + i, 0, 0);
            currentUser.addTask(taskTree[i], taskTree[i / 2]);
        }

        // END TEST TEST TEST TEST
    }

    public List<TaskPair> getTaskList() {
        return taskList.getTaskList();
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
    }
}
