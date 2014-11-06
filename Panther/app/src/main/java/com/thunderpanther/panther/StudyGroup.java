package com.thunderpanther.panther;

/**
 * Created by Steven on 11/6/2014.
 */
public class StudyGroup {
    private String name;
    private String description;
    private boolean isPublic;
    private boolean isOpen;

    public StudyGroup() {}
    public void setPublic(boolean makePublic) { isPublic = makePublic; }
    public void setOpen(boolean makeOpen) { isOpen = makeOpen; }
}
