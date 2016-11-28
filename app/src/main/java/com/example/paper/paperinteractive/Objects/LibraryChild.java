package com.example.paper.paperinteractive.Objects;

public class LibraryChild {

    int mId;
    //int groupId;
    String name;
    LibraryGroup group;

    //LibraryGroup group;

    public LibraryChild(){

    }

    public LibraryChild(String groupName, String name){
        this.name = name;
        group = new LibraryGroup(groupName);
    }

    public LibraryChild(int id, String groupName, String name){
        this(groupName, name);
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

    public String getGroupName() {
        return group.getGroupName();
    }
}
