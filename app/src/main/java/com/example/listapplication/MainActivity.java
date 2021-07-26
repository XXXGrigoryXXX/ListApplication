package com.example.listapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;

    // final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        // Log.d(LOG_TAG, String.valueOf(years));

        Button buttonAdd = (Button) findViewById(R.id.buttonAdd);

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        HashMap<String, String> map;

        dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query("mytable", new String[]{"id", "surname", "firstName", "patronymic", "dateOfBirth", "gender", "age", "sport", "createDate"},
                null, null, null, null, "createDate DESC", null);

        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex("id");

            int surnameColIndex = c.getColumnIndex("surname");
            int firstNameColIndex = c.getColumnIndex("firstName");
            int patronymicColIndex = c.getColumnIndex("patronymic");
            int dateOfBirthColIndex = c.getColumnIndex("dateOfBirth");
            int genderColIndex = c.getColumnIndex("gender");

            do {
                map = new HashMap<>();
                String idColIndexString = String.valueOf(c.getInt(idColIndex));
                map.put("MemberID", idColIndexString);
                map.put("Name", c.getString(surnameColIndex) + " " + c.getString(firstNameColIndex) + " " + c.getString(patronymicColIndex));
                map.put("Gender", c.getString(genderColIndex));
                map.put("DateOfBirth", c.getString(dateOfBirthColIndex));
                arrayList.add(map);
            } while (c.moveToNext());
        } else
        c.close();

        dbHelper.close();

        SimpleAdapter adapter = new SimpleAdapter(this, arrayList,
                R.layout.list_item,
                new String[]{"MemberID", "Name", "Gender", "DateOfBirth"},
                new int[]{R.id.textViewMemberId, R.id.textViewName,
                        R.id.textViewGender, R.id.textViewDateOfBirth});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map elementList = arrayList.get(position);

                String elementListID = (String) elementList.get("MemberID");

                Intent intent = new Intent(MainActivity.this, CreateChangeDeleteCardActivity.class);
                intent.putExtra("ElementListID", elementListID);
                startActivity(intent);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String buttonAddClick = "true";

                Intent intent = new Intent(MainActivity.this, CreateChangeDeleteCardActivity.class);
                intent.putExtra("ButtonAddClick", buttonAddClick);
                startActivity(intent);
            }
        });
    }
}


