package com.example.paper.paperinteractive.Objects;

import java.util.ArrayList;
import java.util.List;

public class LibraryGroup {

    private String mGroupName;
    private int mId;
    private List<LibraryChild> mChildren;


    public LibraryGroup() {

    }

    public LibraryGroup(String groupName) {
        mGroupName = groupName;
        mChildren = new ArrayList<>();
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

    public List<LibraryChild> getList(){return mChildren;}
}
