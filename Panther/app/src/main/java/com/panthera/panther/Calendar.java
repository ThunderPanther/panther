package com.panthera.panther;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Steven on 11/6/2014.
 */
public class Calendar {

    private List<WorkSession> workSessions = new ArrayList<WorkSession>();

    public void Calendar() {}

    public void addWorkSession(WorkSession session) {
        if (workSessions.isEmpty()) {
            workSessions.add(session);
        } else {
            // Insert the WorkSessions in sorted order
            Date startDate = session.getStartTime();

            if (startDate.after(workSessions.get(workSessions.size() - 1).getStartTime())) {
                // The WorkSession is the last one, append it on the list
                workSessions.add(session);
            } else {
                // Search the list until a WorkSession with a later startDate is found
                ListIterator<WorkSession> iterator = workSessions.listIterator();
                while (iterator.hasNext()) {
                    WorkSession next = iterator.next();

                    if (startDate.before(next.getStartTime())) {
                        iterator.previous();
                        iterator.add(session);
                        break;
                    }
                }
            }
        }
    }

    public List<WorkSession> getWorkSessions() {
        return workSessions;
    }

    public List<WorkSession> getWorkSessionsInRange(Date startDate, Date endDate) {
        List<WorkSession> rangeSessions = new ArrayList<WorkSession>();

        for (WorkSession session : workSessions) {
            // Find all WorkSessions with start times within the range
            Date sessionStart = session.getStartTime();

            // Use not before and not after for an inclusive range
            if (!sessionStart.before(startDate) && !sessionStart.after(endDate)) {
                rangeSessions.add(session);
            }
        }

        return rangeSessions;
    }

    public boolean removeWorkSession(WorkSession session) {
        // Iterate the List until the WorkSession is found, then remove it
        ListIterator<WorkSession> iterator = workSessions.listIterator();
        while (iterator.hasNext()) {
            WorkSession next = iterator.next();

            if (next.equals(session)) {
                iterator.remove();
                // The element was found, return true
                return true;
            }
        }

        // The element was not found
        return false;
    }

}
