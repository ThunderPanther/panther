package com.panthera.panther;

import java.util.List;

/**
 * Created by Steven on 11/6/2014.
 */
public class ServerHandler {
    /********************************
     * MAGIC SERVER STUFF GOES HERE *
     ********************************/

    private ServerHandler() {}

    public static ServerHandler openConnection() {
        return null;
    }

    public void closeConnection() {}

    public List<StudyGroup> searchStudyGroups(String term) { return null; }
    public void sendStudyGroupJoinRequest() {}
    public List<String> getPendingNotifications() { return null; }
    public void sendResponse(boolean response) {}
    public void inviteToStudyGroup(String username) {}
}
