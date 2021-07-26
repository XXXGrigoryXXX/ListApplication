package com.example.listapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CreateChangeDeleteCardActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    String[] dataGender = {"М", "Ж"};

    private String checkBoxData;
    public String choosePosition;

    EditText editTextSurname, editTextFirstName, editTextPatronymic;
    TextView textViewDateOfBirthData, textViewAgeData;

    DBHelper dbHelper;

    Calendar dateAndTime = Calendar.getInstance();

    String myDay;
    String myMonth;
    String myYear;

    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_change_delete_card);

        String buttonAddClick = getIntent().getStringExtra("ButtonAddClick");

        String elementListID = getIntent().getStringExtra("ElementListID");

        CheckBox checkBox = findViewById(R.id.checkBox);

        Date currentDate = new Date();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);

        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, dataGender);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapterGender);

        ImageView buttonSave = findViewById(R.id.buttonSave);
        ImageView buttonDelete = findViewById(R.id.buttonDelete);
        ImageView buttonBack = findViewById(R.id.buttonBack);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String[] choose = getResources().getStringArray(R.array.dataGender);
                choosePosition = choose[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        textViewDateOfBirthData = (TextView) findViewById(R.id.textViewDateOfBirthData);
        textViewAgeData = (TextView) findViewById(R.id.textViewAgeData);

        editTextSurname = (EditText) findViewById(R.id.editTextSurname);
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextPatronymic = (EditText) findViewById(R.id.editTextPatronymic);

        dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (!(Boolean.valueOf(buttonAddClick))) {

            Cursor c = db.query("mytable", null, null, null, null, null, null);

            if (c.moveToFirst()) {

                int idColIndex = c.getColumnIndex("id");
                int surnameColIndex = c.getColumnIndex("surname");
                int firstNameColIndex = c.getColumnIndex("firstName");
                int patronymicColIndex = c.getColumnIndex("patronymic");
                int dateOfBirthColIndex = c.getColumnIndex("dateOfBirth");
                int genderColIndex = c.getColumnIndex("gender");
                int ageColIndex = c.getColumnIndex("age");
                int sportColIndex = c.getColumnIndex("sport");

                do {

                    if ((c.getInt(idColIndex)) == (Integer.parseInt(elementListID))) {

                        editTextSurname.setText(c.getString(surnameColIndex));
                        editTextFirstName.setText(c.getString(firstNameColIndex));
                        editTextPatronymic.setText(c.getString(patronymicColIndex));

                        textViewDateOfBirthData.setText(c.getString(dateOfBirthColIndex));
                        textViewAgeData.setText(c.getString(ageColIndex));

                        int spinnerPosition = adapterGender.getPosition(c.getString(genderColIndex));
                        spinner.setSelection(spinnerPosition);

                        if (Boolean.valueOf(c.getString(sportColIndex)) == Boolean.valueOf("true")) {

                            checkBox.setChecked(true);
                        }

                        else {

                            checkBox.setChecked(false);
                        }

                    }

                } while (c.moveToNext());

            } else
                c.close();

        }

        if (Boolean.valueOf(buttonAddClick)) {
            buttonDelete.setVisibility(View.INVISIBLE);
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues cv = new ContentValues();

                String surname = editTextSurname.getText().toString();
                String firstName = editTextFirstName.getText().toString();
                String patronymic = editTextPatronymic.getText().toString();
                String dateOfBirth = textViewDateOfBirthData.getText().toString();
                String age = textViewAgeData.getText().toString();

                if (surname.length() != 0 && firstName.length() != 0 && patronymic.length() != 0 && dateOfBirth.length() != 0) {
                    cv.put("surname", surname);
                    cv.put("firstName", firstName);
                    cv.put("patronymic", patronymic);
                    cv.put("dateOfBirth", dateOfBirth);
                    cv.put("gender", choosePosition);
                    cv.put("age", age);
                    cv.put("sport", checkBoxData);
                    cv.put("createDate", (dateText + "T" + timeText));

                    if (!(Boolean.valueOf(buttonAddClick))) {

                        db.update("mytable", cv, "id = ?", new String[]{elementListID});
                    } else {

                        db.insert("mytable", null, cv);
                    }

                    dbHelper.close();

                    startActivity(new Intent(CreateChangeDeleteCardActivity.this, MainActivity.class));

                    Toast.makeText(CreateChangeDeleteCardActivity.this, "Данные сохранены", Toast.LENGTH_LONG).show();
                }

                else {

                    Toast.makeText(CreateChangeDeleteCardActivity.this, "Заполните все обязательные поля", Toast.LENGTH_LONG).show();
                }
            }
        });

        alertDialog = new AlertDialog.Builder(CreateChangeDeleteCardActivity.this);
        alertDialog.setTitle("Удаление");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setMessage("Вы точно хотите удалить данные?");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                db.delete("mytable", "id = " + elementListID, null);
                dbHelper.close();

                startActivity(new Intent(CreateChangeDeleteCardActivity.this, MainActivity.class));

                Toast.makeText(CreateChangeDeleteCardActivity.this, "Данные удалены", Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.setNegativeButton("Отмена", null);
        alertDialog.create();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.show();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* if (!(Boolean.valueOf(buttonAddClick))) {

                    startActivity(new Intent(CreateChangeDeleteCardActivity.this, CardActivity.class));
                }

                else {
                 */
                    startActivity(new Intent(CreateChangeDeleteCardActivity.this, MainActivity.class));
                // }
            }
        });

    }

    public void setDate(View v) {

        new DatePickerDialog(CreateChangeDeleteCardActivity.this, setDate,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialDateTime() {

        textViewDateOfBirthData.setText(myDay + "." + myMonth + "." + myYear);
    }

    DatePickerDialog.OnDateSetListener setDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            dateAndTime.set(Calendar.YEAR, year);
            myYear = String.valueOf(dateAndTime.get(Calendar.YEAR));

            dateAndTime.set(Calendar.MONTH, monthOfYear);
            myMonth = String.valueOf(dateAndTime.get(Calendar.MONTH) + 1);

            if (myMonth.length() == 1) {
                myMonth = "0" + myMonth;
            }

            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            myDay = String.valueOf(dateAndTime.get(Calendar.DAY_OF_MONTH));

            if (myDay.length() == 1) {
                myDay = "0" + myDay;
            }

            setInitialDateTime();
            ageCalculation(textViewDateOfBirthData.getText().toString());
        }
    };

    public void onCheckboxClicked(View view) {

        CheckBox checkBox = (CheckBox) view;

        if (checkBox.isChecked()) {

            checkBoxData = "true";
        }
        else {

            checkBoxData = "false";
        }
    }
    
    public void ageCalculation (String dateOfBirth) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

            Log.d(LOG_TAG, "dateOfBirth " + dateOfBirth);

            Date date = simpleDateFormat.parse(dateOfBirth);

            long ageMil = new Date().getTime() - date.getTime();

            long age = ((TimeUnit.DAYS.convert(ageMil, TimeUnit.MILLISECONDS)) / 365L);

            textViewAgeData.setText(String.valueOf(age));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
