package com.example.paper.paperinteractive.Objects;

public class LibraryChild {

    int mId;
    int groupId;
    String name;
    LibraryGroup groupName;

    //LibraryGroup group;

    public LibraryChild(){

    }

    public LibraryChild(int groupId, String name){
        this.name = name;
        this.groupId = groupId;
    }

    public LibraryChild(int id, int groupId, String name){
        this(groupId, name);
        mId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getGroupId() {
        return groupId;
    }
}
