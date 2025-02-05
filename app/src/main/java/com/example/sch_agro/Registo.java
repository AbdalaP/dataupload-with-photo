package com.example.sch_agro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.sch_agro.util.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

public class Registo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText name;
    Button submitt,display,export;
    ImageView image;
    Spinner spinner,spinner2;
    public static final int CAMERA_REQUEST=100;
    public static final int STORAGE_REQUEST=101;
    String cameraPermission[];
    String storagePermission[];

    TextView tvDate;
    Button btPickDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        tvDate = findViewById(R.id.tvDate);
        //btPickDate = findViewById(R.id.btPickDate);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Please note that use your package name here
                com.example.sch_agro.util.DatePicker mDatePickerDialogFragment;
                mDatePickerDialogFragment = new com.example.sch_agro.util.DatePicker();
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
            }
        });


        //below the links for data export from database table to excel
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");


        databaseHelper= new DatabaseHelper(this);
        image= findViewById(R.id.edtimage);
        findid();
        insertData();
        spinner = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
      //  spinner.setOnItemSelectedListener(this);
       // spinner2.setOnItemSelectedListener(this);
        loadSpinnerData();
        loadSpinnerData2();

        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int image = 0;
                if(image==0){
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                        Toast.makeText(Registo.this, "No camera permission", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Registo.this, "Something is Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }




    private void loadSpinnerData() {

        List<String> labels = databaseHelper.getAllLabels();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        dataAdapter.add("[Escolhe Actividade...]");


        spinner.setSelection(dataAdapter.getCount()); //set the hint the default selection so it appears on launch.
        //spinner.setOnItemSelectedListener(this);
    }

    private void loadSpinnerData2() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.tipo_trabalahador,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        //link the adapter to the spinner
        Spinner coffeeChoice = (Spinner) findViewById(R.id.spinner2);
        coffeeChoice.setAdapter(adapter);

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(mCalendar.getTime());
        tvDate.setText(selectedDate);
    }

    // THIS FUNCTION SHOWS DATA FROM THE DATABASE


    private void requestCameraPermission() {requestPermissions(cameraPermission,CAMERA_REQUEST);}

    private boolean checkCameraPermission() {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
       boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        return  result && result1;
    }


    private void insertData() {
        submitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                //cv.put("name",name.getText().toString());
                cv.put("name",spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
                cv.put("image",ImageViewToBy(image));


                sqLiteDatabase=databaseHelper.getWritableDatabase();
                if (spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString()!="[Escolhe Actividade...]"){
                    long result = sqLiteDatabase.insert("testimage", null, cv);
                    if (result== -1) {
                        Toast.makeText(Registo.this,"Not inserted",Toast.LENGTH_LONG);
                    } else {
                        Toast.makeText(Registo.this,"Inserted",Toast.LENGTH_LONG);
                        name.setText("");
                        image.setImageResource(R.mipmap.ic_launcher);
                        Toast.makeText(Registo.this,"Insertedsssss",Toast.LENGTH_LONG);
                    }
                }else{

                    Toast.makeText(Registo.this,"Por favor escolhe actividade",Toast.LENGTH_LONG);
                }

            }
        });

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(Registo.this, DisplayData.class));
            }
        });

        export.setOnClickListener(new View.OnClickListener() {

           // Context dbname;
          // String dd;
            @Override
            public void onClick(View v) {
                //ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
               // task.execute();
              // Test.exportDatabase(dbname,dd);
                //Test.exportDatabase(this, "SchAgro.db");

                exportDB();

            }
        });
    }


    private void exportDB() {

        sqLiteDatabase=databaseHelper.getWritableDatabase();
        DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());

        //File exportDir = Environment.getExternalStorageDirectory();
        File exportDir =  new File(Environment.getExternalStorageDirectory(), "Download");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "Ficheiro de actividades.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM task",null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1)};
                csvWrite.writeNext(arrStr);
                Toast.makeText(this, "Ficheiro baixado com Sucesso", Toast.LENGTH_SHORT).show();
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("Registo", sqlEx.getMessage(), sqlEx);
        }

    }
    

    public static byte[] ImageViewToBy(ImageView image) {
       Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
        byte[]bytes=stream.toByteArray();
        return bytes;
    }

    private void findid() {
        name= (EditText)findViewById(R.id.edtname);
        image = (ImageView)findViewById(R.id.edtimage);
        submitt = (Button)findViewById(R.id.btn_submit);
        display = (Button)findViewById(R.id.btn_display);
        export = (Button)findViewById(R.id.btn_export);
       // spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if (grantResults.length>0){
                    boolean camera_accepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted){
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,100);
                    }else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,100);
                    }
                }
            }
            break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);

        }else {
            Toast.makeText(this, "something is wrong", Toast.LENGTH_SHORT).show();

        }

    }


 //   @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

  //  @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}