package com.panthera.panther;

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
    private List<String> notes = new ArrayList<String>();
    private int timeEstimate;
    private Color displayColor;

    public Task() {
        this(null);
    }

    public Task(Task parentTask) {
        parent = parentTask;
    }

    public void logWork() {
    }

    public void editWork() {
    }

    public void addNote() {
        // TODO: implement this
    }

    public void editNote() {
        // TODO: implement this
    }

    public void removeNote() {
    }

    public void removeChild(Task task) {
        Iterator<Task> iter = children.iterator();
        while (iter.hasNext()) {
            Task curTask = iter.next();
            if (curTask.equals(task)) {
                iter.remove();
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
}
