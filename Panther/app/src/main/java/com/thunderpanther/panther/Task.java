package com.thunderpanther.panther;

import android.graphics.Color;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Steven on 10/28/2014.
 */
public class Task {
    private Task parent = null;
    private List<Task> children = new ArrayList<Task>();

    private String name;
    private int weight;
    // Maybe a map?
    private List<String> notes = new ArrayList<String>();

    private int timeWorked;
    private int timeEstimate;
    private Color displayColor;

    public Task(String name, int weight, int timeEstimate) {
        this(name, weight, timeEstimate, null);
    }

    public Task(String name, int weight, int timeEstimate, Task parentTask) {
        this.name = name;
        this.weight = weight;
        this.timeEstimate = timeEstimate;
        parent = parentTask;
    }

    public void logWork(int timeWorked) {
        this.timeWorked += timeWorked;
        timeEstimate -= timeWorked;
    }

    public void logWork(int timeWorked, int newEstimate) {
        this.timeWorked += timeWorked;
        timeEstimate = newEstimate;
    }

    public void editWork(int newTimeWorked, int newTimeEstimate) {
        timeWorked = newTimeWorked;
        timeEstimate = newTimeEstimate;
    }

    public void addNote(String note) {
        notes.add(note);
    }

    public void editNote(int index, String note) {
        notes.set(index, note);
    }

    public void removeNote(int index) {
        notes.remove(index);
    }

    public void removeChild(Task task) {
        Iterator<Task> iterator = children.iterator();
        while (iterator.hasNext()) {
            Task curTask = iterator.next();
            if (curTask.equals(task)) {
                iterator.remove();
                break;
            } else {
                curTask.removeChild(task);
            }
        }
    }

    public void addChild(Task child) {
        children.add(child);
        child.parent = this;
    }

    public String getName() {
        return name;
    }

    public void addToList(List<TaskPair> taskList, int depth) {
        taskList.add(new TaskPair(name, depth));
        for (Task t :children) {
            t.addToList(taskList, depth + 1);
        }
    }
}
