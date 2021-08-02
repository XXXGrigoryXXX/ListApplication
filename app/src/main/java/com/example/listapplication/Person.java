package com.example.listapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dbId);
        dest.writeString(this.name);
        dest.writeString(this.gender);
        dest.writeString(this.date);
    }

    public void readFromParcel(Parcel source) {
        this.dbId = source.readInt();
        this.name = source.readString();
        this.gender = source.readString();
        this.date = source.readString();
    }

    protected Person(Parcel in) {
        this.dbId = in.readInt();
        this.name = in.readString();
        this.gender = in.readString();
        this.date = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
