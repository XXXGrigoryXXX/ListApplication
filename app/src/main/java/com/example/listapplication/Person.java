package com.example.listapplication;

public class Person {

    private int dbId;
    private String name;
    private String gender;
    private String date;

    public Person(int dbId, String name, String gender, String date) {
        this.setDbId(dbId);
        this.setName(name);
        this.setGender(gender);
        this.setDate(date);
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
