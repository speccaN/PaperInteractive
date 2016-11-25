package com.example.paper.paperinteractive.Objects;

public class LibraryGroup {

    private String mGroupName;
    private int mId;


    public LibraryGroup() {

    }

    public LibraryGroup(String groupName) {
        mGroupName = groupName;
    }

    public LibraryGroup(int id, String groupName){
        this(groupName);
        mId = id;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setName(String name) {
        this.mGroupName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }
}
