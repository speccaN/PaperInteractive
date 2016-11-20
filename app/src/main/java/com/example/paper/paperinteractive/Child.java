package com.example.paper.paperinteractive;

/**
 * Created by Eric on 2016-11-18.
 */

public class Child {

    private int _id, _age;
    private String _name;

    // Parameter-less constructor
    public Child(){

    }

    public Child(String name, int age){
        this._name = name;
        this._age = age;
    }

    public Child(int id, String name, int age) {
        this(name, age);
        this._id = id;
    }

    public int getAge() {
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

    public void setAge(int age) {
        this._age = age;
    }

    public int getId() {
        return _id;
    }
}
