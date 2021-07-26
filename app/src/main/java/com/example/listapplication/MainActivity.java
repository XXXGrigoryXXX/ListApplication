package com.example.listapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.example.listapplication.CreateChangeDeleteCardActivity.DB_ID;
import static com.example.listapplication.CreateChangeDeleteCardActivity.MODE;


public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;

    private ArrayList<Person> list;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        Button buttonAdd = (Button) findViewById(R.id.buttonAdd);

        list = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person person = list.get(position);
                if (person == null || person.dbId < 0) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, CreateChangeDeleteCardActivity.class);
                intent.putExtra(DB_ID, person.dbId);
                startActivity(intent);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateChangeDeleteCardActivity.class);
                intent.putExtra(MODE, true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateIfNeed();

    }

    private void updateIfNeed() {
        HashMap<String, String> map;
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        HashSet<Person> set = new HashSet<>(list);
        ArrayList<Person> list1 = new ArrayList<>();
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
                int dbId = (c.getInt(idColIndex));
                String name = c.getString(surnameColIndex) + " " + c.getString(firstNameColIndex) + " " + c.getString(patronymicColIndex);
                String gender = c.getString(genderColIndex);
                String date = c.getString(dateOfBirthColIndex);
                Person person = new Person(dbId, name, gender, date);
                list1.add(person);
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();
        set.addAll(list1);
        list = new ArrayList<>(set);
        Collections.sort(list, (o1, o2) -> Integer.compare(o1.dbId, o2.dbId));

        PersonAdapter adapter = new PersonAdapter(this, list);
        listView.setAdapter(adapter);
    }
}


