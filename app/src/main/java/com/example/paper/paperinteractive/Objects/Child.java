package com.example.paper.paperinteractive.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Child implements Serializable {

    private int _id;
    private String _name, _age;
    private List<LibraryChild> libraryChildList = new ArrayList<>();

    // Parameter-less constructor
    public Child(){

    }

    public Child(String name, String age){
        this._name = name;
        this._age = age;
    }

    public Child(int id, String name, String age) {
        this(name, age);
        this._id = id;
    }

    public String getAge() {
        return _age;
    }

    public String getName() {
        return _name;
    }

    public void setId(int id) {
        this._id = id;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setAge(String age) {
        this._age = age;
    }

    public int getId() {
        return _id;
    }

    public List<LibraryChild> getLibraryChildList() {
        return libraryChildList;
    }

    public int getListCount(){
        return libraryChildList.size();
    }
}
