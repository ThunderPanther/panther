package com.thunderpanther.panther;

import java.util.List;

/**
 * Created by Steven on 11/6/2014.
 */
public class OnlineUser extends User {
    private String username;
    // This is probably a bad idea
    private String password;

    private ServerHandler serverHandler;

    public OnlineUser() {
        serverHandler = ServerHandler.openConnection();
    }

    @Override
    protected void loadData() {
        // Query the database to load tasks/worksessions
    }

    public List<Object> getPeersAndGroupsList() {
        return null;
    }

    public void joinGroup(StudyGroup group) {}

    @Override
    public void storeData() {
        super.storeData();

        // server stuff
    }
}
