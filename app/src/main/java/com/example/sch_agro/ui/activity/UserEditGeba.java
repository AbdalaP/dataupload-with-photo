package com.example.sch_agro.ui.activity;

import static com.example.sch_agro.R.mipmap.ic_launcher;
import static com.example.sch_agro.util.DatabaseHelper.taskgeba;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.sch_agro.R;
import com.example.sch_agro.util.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class UserEditGeba extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText name,docid,data_nascimento,telefone,gender,id,bloc;
    Spinner spinner;

    String doid;
    Button Submit,edit;
    ImageView image;
    RadioButton male,female;




    public static final int CAMERA_REQUEST=100;
    public static final int STORAGE_REQUEST=101;
    String cameraPermission[];
    String storagePermission[];




    Uri mImageCaptureUri;
    File mFileTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usereditgeba);

        databaseHelper= new DatabaseHelper(this);
        findid();
        editData();
        insertData();
        spinner = findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);
        loadSpinnerData();
        //image= findViewById(R.id.edtimage);

/*

The upload image in only in the user registrarion this not useful
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
                        Toast.makeText(UserEditGeba.this, "No camera permission", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UserEditGeba.this, "Something is Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

 */

    }

    //The query to populate this spineer is in the databaseHelper
    private void loadSpinnerData() {
        List<String> labels = databaseHelper.getAllLabels();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.color_spinner_layout,
                labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        dataAdapter.add("[Escolhe Actividade...]");
        spinner.setSelection(dataAdapter.getCount()); //set the hint the default selection so it appears on launch.
        spinner.setOnItemSelectedListener(this);
    }

    //to choose the textfied to show bunddle data is here.
    private void editData() {
        if(getIntent().getBundleExtra("userdata")!=null){
            Bundle bundle=getIntent().getBundleExtra("userdata");
            byte[]bytes=bundle.getByteArray("image");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            image.setImageBitmap(bitmap);
            id.setText(bundle.getString("user_id"));
            name.setText(bundle.getString("name"));
            docid.setText(bundle.getString("docid"));
            telefone.setText(bundle.getString("telefone"));
            //spinner.setText(bundle.getString("act"));
           // EditText name = (EditText) findViewById(R.id.nome);
            name.setEnabled(false);
            image.setEnabled(false);
            id.setEnabled(false);
            docid.setEnabled(false);
            telefone.setEnabled(false);

        }
    }

    String userGender = "";
    String checked;

    public void RadioButtonClicked(View view) {
        //This variable will store whether the user was male or female


        // Check that the button is  now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        int id = view.getId();
        if (id == R.id.female) {
            if (checked)
                userGender = "female";
        } else if (id == R.id.male) {
            if (checked)
                userGender = "male";
        }

    }

    private void requestCameraPermission() {requestPermissions(cameraPermission,CAMERA_REQUEST);}

    private boolean checkCameraPermission() {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        return  result && result1;
    }

    private void insertData() {
/*
        Submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {


                sqLiteDatabase = databaseHelper.getWritableDatabase();


                if (name.length() == 0 || docid.length() == 0 || idade.length() == 0 || userGender.isEmpty() || telefone.length() == 0 || image.equals(R.mipmap.ic_launcher)) {
                    Toast.makeText(UserEditGeba.this, "All fields are mandatory", Toast.LENGTH_SHORT
                    ).show();
                }
                // image.callOnClick(); this function one day i have to investigate so that when submit button is clicked it opens camera and take image and save image together with the data.
                else {
                    Boolean checdocid = databaseHelper.checkdocid(String.valueOf(docid));



                    if (checdocid == false) {

                        ContentValues cv = new ContentValues();
                        cv.put("nome", name.getText().toString());
                        cv.put("docid", docid.getText().toString());
                        cv.put("idade", idade.getText().toString());
                        cv.put("genero", userGender.toString());
                        cv.put("telefone", telefone.getText().toString());
                        cv.put("image", ImageViewToBy(image));

                        long result = sqLiteDatabase.insert("trabalhadores", null, cv);


                        if (result == -1) {

                            Toast.makeText(UserEditGeba.this, "Dados nao inseridos!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(UserEditGeba.this, "Dados inseridos com Sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            name.setText("");
                            docid.setText("");
                            idade.setText("");
                            gender.setText("");
                            telefone.setText("");
                            image.setImageResource(R.mipmap.ic_launcher);
                        }
                    } else {
                        Toast.makeText(UserEditGeba.this, "O Trabalhador ja foi registado no sistema!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

 */

        //////////button edit or inserting new data task
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                   // sqLiteDatabase=databaseHelper.getReadableDatabase();

                    ContentValues cv =new ContentValues();
                   // cv.put("id",id);
                    cv.put("image",ImageViewToBy(image));//is diferente imageviewtobyte original.
                    cv.put("name",name.getText().toString());
                    cv.put("docid",docid.getText().toString());
                    cv.put("telefone",telefone.getText().toString());
                    //cv.put("act",act.getText().toString());
                    cv.put("act",spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
                    cv.put("user_id",id.getText().toString());
                    cv.put("block",bloc.getText().toString());

                sqLiteDatabase=databaseHelper.getWritableDatabase();

                if (bloc.length()==0) {
                    Toast.makeText(UserEditGeba.this, "Insira o Numero de bloco", Toast.LENGTH_SHORT).show();
                }
                    if (spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString() != "[Escolhe Actividade...]") {
                        long recedit = sqLiteDatabase.insert(taskgeba, null, cv);
                        if (recedit != -1) {
                            Toast.makeText(UserEditGeba.this, "Data Inserted successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            image.setImageResource(ic_launcher);
                            name.setText("");
                            Submit.setVisibility(View.GONE);
                            edit.setVisibility(View.VISIBLE);

                        } else {
                            Toast.makeText(UserEditGeba.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(UserEditGeba.this, "Escolhe Actividade", Toast.LENGTH_SHORT).show();
                    }


            }
        });


    }


    public static byte[] ImageViewToBy(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[]bytes=stream.toByteArray();
        return bytes;
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
           // image.setImageIcon(Icon.createWithBitmap(bitmap));
           // image.getDrawable();//novo

        }else {
            Toast.makeText(this, "something is shit", Toast.LENGTH_SHORT).show();

        }

    }

    private void findid() {
        bloc = (EditText)findViewById(R.id.bloc);
        name= (EditText)findViewById(R.id.nome);
        docid= (EditText)findViewById(R.id.docid);
        data_nascimento= (EditText)findViewById(R.id.data_nascimento);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        telefone= (EditText)findViewById(R.id.telefone);
       spinner= (Spinner) findViewById(R.id.spinner1);
        id= (EditText)findViewById(R.id.user_id);
        userGender=userGender.toString();
        image = (ImageView)findViewById(R.id.edtimage);
        Submit = (Button)findViewById(R.id.Submit);
       edit = (Button)findViewById(R.id.btn_edit);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}