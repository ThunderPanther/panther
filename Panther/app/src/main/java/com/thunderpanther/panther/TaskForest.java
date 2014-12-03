package com.thunderpanther.panther;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Steven on 10/28/2014.
 */
public class TaskForest {
    private List<Task> rootTasks = new ArrayList<Task>();
    private List<TaskPair> taskStringList = new ArrayList<TaskPair>();
    private List<Task> taskList = new ArrayList<Task>();
    private boolean taskStringModified = true;
    private boolean tasksModified = true;

    public void addRoot(Task task) {
        rootTasks.add(task);
    }

    public Task getTask(int id) {
        for (Task t : rootTasks) {
            if (t.getID() == id) {
                return t;
            }
            Task subTask = t.getTask(id);
            if (subTask != null) {
                return subTask;
            }
        }
        return null;
    }

    public void removeTask(int taskID) {
        Iterator<Task> iterator = rootTasks.iterator();
        while (iterator.hasNext()) {
            Task curTask = iterator.next();
            if (curTask.getID() == taskID) {
                iterator.remove();
                break;
            } else {
                curTask.removeChild(taskID);
            }
        }
        taskStringModified = true;
        tasksModified = true;
    }

    public void addTask(Task task, Task parent) {
        if (parent != null) {
            parent.addChild(task);
        } else {
            rootTasks.add(task);
        }
        taskStringModified = true;
        tasksModified = true;
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

    public List<Task> getTaskRefList() {
        if (tasksModified) {
            taskList.clear();
            for (Task t : rootTasks) {
                t.addRefToList(taskList);
            }
            tasksModified = false;
        }
        return taskList;
    }
}
