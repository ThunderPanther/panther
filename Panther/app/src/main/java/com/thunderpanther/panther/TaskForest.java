package com.thunderpanther.panther;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 10/28/2014.
 */
public class TaskForest {
    private List<Task> rootTasks = new ArrayList<Task>();

    public void addRoot(Task task) {
        rootTasks.add(task);
    }

    public Task getTask() {
        return null;
    }

    public void removeTask(Task task) {
        // Should a string be used here instead?
    }

    public void addTask(Task task, Task parent) {
        if (parent != null) {
            parent.addChild(task);
        } else {
            rootTasks.add(task);
        }
    }

    public List<TaskPair> getTaskList() {
        List<TaskPair> taskList = new ArrayList<TaskPair>();
        for (Task t : rootTasks) {
            t.addToList(taskList, 0);
        }
        return taskList;
    }
}
