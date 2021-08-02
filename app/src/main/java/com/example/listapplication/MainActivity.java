package com.example.listapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.util.ArrayList;


import static com.example.listapplication.CreateChangeDeleteCardActivity.DB_ID;
import static com.example.listapplication.CreateChangeDeleteCardActivity.MODE;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_DATA_FROM_DATABASE = 1;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Person> arrayList = new ArrayList<>();

    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        DBHelper dbHelper = new DBHelper(this);

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
                int id = c.getInt(idColIndex);
                String surname = c.getString(surnameColIndex);
                String firstName = c.getString(firstNameColIndex);
                String patronymic = c.getString(patronymicColIndex);
                String name = surname + " " + firstName + " " + patronymic;
                String date = c.getString(dateOfBirthColIndex);
                String gender = c.getString(genderColIndex);

                Person person = new Person(id, name, gender, date);
                arrayList.add(person);
            } while (c.moveToNext());
        } else
            c.close();

        dbHelper.close();

        PersonAdapter.setOnClickListener onClickListener = new PersonAdapter.setOnClickListener() {
            @Override
            public void onClick(Person person, int position) {

                Intent intent = new Intent(MainActivity.this, CreateChangeDeleteCardActivity.class);
                intent.putExtra(DB_ID, person.getDbId());
                startActivity(intent);
            }
        };

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CreateChangeDeleteCardActivity.class);
                intent.putExtra(MODE, true);
                startActivity(intent);
            }
        });

        adapter = new PersonAdapter(this, arrayList, onClickListener);
        recyclerView.setAdapter(adapter);
    }


}



