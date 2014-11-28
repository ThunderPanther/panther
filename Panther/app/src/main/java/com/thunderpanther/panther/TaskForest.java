package com.thunderpanther.panther;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 10/28/2014.
 */
public class TaskForest {
    private List<Task> rootTasks = new ArrayList<Task>();
    private List<TaskPair> taskStringList = new ArrayList<TaskPair>();
    private boolean taskStringModified = true;

    public void addRoot(Task task) {
        rootTasks.add(task);
    }

    public Task getTask() {
        return null;
    }

    public void removeTask(Task task) {
        // Should a string be used here instead?
        taskStringModified = true;
    }

    public void addTask(Task task, Task parent) {
        if (parent != null) {
            parent.addChild(task);
        } else {
            rootTasks.add(task);
        }
        taskStringModified = true;
    }

    public boolean isModified() {
        return taskStringModified;
    }

    public List<TaskPair> getTaskList() {
        if (taskStringModified) {
           taskStringList.clear();
            for (Task t : rootTasks) {
                t.addToList(taskStringList, 0);
            }
            taskStringModified = false;
        }
        return taskStringList;
    }
}
