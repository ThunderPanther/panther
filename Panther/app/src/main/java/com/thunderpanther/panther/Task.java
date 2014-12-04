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

    private int id;
    private String name;
    private int weight;
    // Maybe a map?
    private List<String> notes = new ArrayList<String>();

    private int timeWorked;
    private double timeEstimate;
    private Color displayColor;

    private static int currentID = 0;

    public Task(String name, int weight, double timeEstimate) {
        this(name, weight, timeEstimate, null);
    }

    public Task(String name, int weight, double timeEstimate, Task parentTask) {
        this.id = currentID++;
        this.name = /* "@" + (currentID) + ": " + */ name;
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

    public void removeChild(int taskID) {
        Iterator<Task> iterator = children.iterator();
        while (iterator.hasNext()) {
            Task curTask = iterator.next();
            if (curTask.id == taskID) {
                iterator.remove();
                break;
            } else {
                curTask.removeChild(taskID);
            }
        }
    }

    public Task getTask(int id) {
        for (Task t : children) {
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

    public void addChild(Task child) {
        children.add(child);
        child.parent = this;
    }

    public Task getParent() {
        return parent;
    }

    public int getID() {
        return id;
    }

    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int w) {
        weight = w;
    }

    public double getTimeEstimate() {
        return timeEstimate;
    }

    public void setTimeEstimate(double time) {
        timeEstimate = time;
    }

    public void addToList(List<TaskPair> taskList, int depth) {
        taskList.add(new TaskPair(id, name, depth));
        for (Task t : children) {
            t.addToList(taskList, depth + 1);
        }
    }

    public void addRefToList(List<Task> taskList) {
        taskList.add(this);
        for (Task t : children) {
            t.addRefToList(taskList);
        }
    }
}
