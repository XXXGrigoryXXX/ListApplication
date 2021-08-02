package com.example.listapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CreateChangeDeleteCardActivity extends AppCompatActivity {

    public final static String MODE = "com.example.listapplication.MODE";
    public final static String DB_ID = "com.example.listapplication.DB_ID";

    private final static int REQUEST_IMAGE_CAPTURE = 1;

    private final String[] dataGender = {"М", "Ж"};

    private int checkBoxData;
    private String choosePosition;

    private EditText editTextSurname, editTextFirstName, editTextPatronymic;
    private TextView textViewDateOfBirthData;
    private TextView textViewAgeData;

    private DBHelper dbHelper;

    private ImageView buttonSave;

    private ImageView imageView;

    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_change_delete_card);

        boolean addMode = getIntent().getBooleanExtra(MODE, false);
        int elementListID = getIntent().getIntExtra(DB_ID, -1);

        buttonSave = findViewById(R.id.buttonSave);

        CheckBox checkBox = findViewById(R.id.checkBox);

        Date currentDate = new Date();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);

        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataGender);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapterGender);

        ImageView buttonSave = findViewById(R.id.buttonSave);
        ImageView buttonDelete = findViewById(R.id.buttonDelete);
        ImageView buttonBack = findViewById(R.id.buttonBack);

        imageView = findViewById(R.id.imageView);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                choosePosition = dataGender[position];
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

        if (!addMode) {

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
                int photoPathColIndex = c.getColumnIndex("photoPath");

                do {

                    if (c.getInt(idColIndex) == elementListID) {

                        editTextSurname.setText(c.getString(surnameColIndex));
                        editTextFirstName.setText(c.getString(firstNameColIndex));
                        editTextPatronymic.setText(c.getString(patronymicColIndex));

                        textViewDateOfBirthData.setText(c.getString(dateOfBirthColIndex));
                        textViewAgeData.setText(c.getString(ageColIndex));

                        Bitmap imageBitmap = BitmapFactory.decodeFile(c.getString(photoPathColIndex));
                        imageView.setImageBitmap(imageBitmap);

                        int spinnerPosition = adapterGender.getPosition(c.getString(genderColIndex));
                        spinner.setSelection(spinnerPosition);

                        if (Boolean.valueOf(c.getString(sportColIndex)) == true) {

                            checkBox.setChecked(true);
                        } else {

                            checkBox.setChecked(false);
                        }

                    }

                } while (c.moveToNext());

            } else
                c.close();

        }

        if (addMode) {

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(125, 110);
            param.setMargins(350,0,0,0);

            buttonSave.setLayoutParams(param);
            buttonDelete.setVisibility(View.GONE);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(CreateChangeDeleteCardActivity.this,
                                "com.example.listapplication.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }

                }
            }
        });

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
                    cv.put("photoPath", currentPhotoPath);

                    if (!addMode) {

                        db.update("mytable", cv, "id = ?", new String[]{String.valueOf(elementListID)});
                    } else {

                        db.insert("mytable", null, cv);
                    }

                    dbHelper.close();

                    startActivity(new Intent(CreateChangeDeleteCardActivity.this, MainActivity.class));

                    Toast.makeText(CreateChangeDeleteCardActivity.this, "Данные сохранены", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(CreateChangeDeleteCardActivity.this, "Заполните все обязательные поля", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                alertDialog.show();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        textViewDateOfBirthData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar dateAndTime = Calendar.getInstance();
                new DatePickerDialog(CreateChangeDeleteCardActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateAndTime.set(Calendar.YEAR, year);
                        dateAndTime.set(Calendar.MONTH, month);
                        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                        String date = dateFormat.format(dateAndTime.getTime());
                        textViewDateOfBirthData.setText(date);
                        ageCalculation(dateAndTime.getTime());
                    }
                },
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
            imageView.setImageBitmap(imageBitmap);

        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onCheckboxClicked(View view) {

        CheckBox checkBox = (CheckBox) view;

        if (checkBox.isChecked()) {

            checkBoxData = 1;
        } else {

            checkBoxData = 0;
        }
    }

    public void ageCalculation(Date dateOfBirth) {

        long ageMil = new Date().getTime() - dateOfBirth.getTime();
        long age = ((TimeUnit.DAYS.convert(ageMil, TimeUnit.MILLISECONDS)) / 365L);
        textViewAgeData.setText(String.valueOf(age));
    }
}