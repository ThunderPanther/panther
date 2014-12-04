package com.thunderpanther.panther;

import java.util.Date;

/**
 * Created by Steven on 11/6/2014.
 */
public class WorkSession {
    private int wsID;
    private Date startTime;
    private Date endTime;
    private Task target;

    public WorkSession(int id, Date start, Date end, Task target) {
        wsID = id;
        startTime = start;
        endTime = end;
        this.target = target;
    }

    public Task getTarget() {
        return target;
    }

    public int getID() { return wsID; }
    public Date getStartTime() {
        return startTime;
    }
    public Date getEndTime() { return endTime; }

    public void completeSession() {

    }
}
