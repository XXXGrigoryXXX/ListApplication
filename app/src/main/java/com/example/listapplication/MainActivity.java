package com.example.listapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;



import java.util.ArrayList;


import static com.example.listapplication.CreateChangeDeleteCardActivity.DB_ID;
import static com.example.listapplication.CreateChangeDeleteCardActivity.MODE;


public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    private static final int REQUEST_DATA_FROM_DATABASE = 1;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Person> arrayList = new ArrayList<>();

    private Button buttonAdd;

    private int positionRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
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
        }
        c.close();

        dbHelper.close();

        buttonAdd.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, CreateChangeDeleteCardActivity.class);
            intent.putExtra(MODE, true);
            startActivityForResult(intent, REQUEST_DATA_FROM_DATABASE);
        });

        adapter = new PersonAdapter(arrayList, (dbId, position) -> {

            positionRecyclerView = position;
            Intent intent = new Intent(MainActivity.this, CreateChangeDeleteCardActivity.class);
            intent.putExtra(DB_ID, dbId);
            startActivityForResult(intent, REQUEST_DATA_FROM_DATABASE);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DATA_FROM_DATABASE && resultCode == Activity.RESULT_OK) {

            Person person = data.getParcelableExtra(DB_ID);

            int statusAction = data.getIntExtra("statusAction", -1);

            Log.d(LOG_TAG, "position " + positionRecyclerView);

            switch (statusAction) {
                case 1:
                    arrayList.set(positionRecyclerView, person);
                    adapter.notifyItemChanged(positionRecyclerView);
                    break;
                case 2:
                    arrayList.remove(positionRecyclerView);
                    adapter.notifyItemRemoved(positionRecyclerView);
                    break;
                default:
                    positionRecyclerView = 0;
                    arrayList.add(positionRecyclerView, person);
                    adapter.notifyItemInserted(positionRecyclerView);
                    break;

            }
        }

    }
}




