package com.example.listapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {

        super(context, "myDB", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "surname text,"
                + "firstName text,"
                + "patronymic text,"
                + "dateOfBirth text,"
                + "gender text,"
                + "age integer,"
                + "sport text,"
                + "createDate text,"
                + "photoPath text"+ ");");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
