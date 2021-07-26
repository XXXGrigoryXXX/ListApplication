package com.example.listapplication;

public class Person {
    public final int dbId;
    public final String name;
    public final String gender;
    public final String date;

    public Person(int dbId, String name, String gender, String date) {
        this.dbId = dbId;
        this.name = name;
        this.gender = gender;
        this.date = date;
    }

}
