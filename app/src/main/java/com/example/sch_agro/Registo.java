package com.example.sch_agro;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Registo extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText name;
    Button submit;
    private ImageView image;

    public static final int CAMERA_REQUEST=100;
    public static final int STORAGE_REQUEST=101;
    String cameraPermission[];
    String storagePermission[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        databaseHelper= new DatabaseHelper(this);
        findid();
        insertData();
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
                        picFromGallery();
                    }
                }else if (image==1){
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        picFromGallery();
                    }
                }
            }
        });
    }

    private void requestStoragePermission(){requestPermissions(storagePermission,STORAGE_REQUEST);}

    private boolean checkStoragePermission() {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        return  result && result1;
    }

    private void picFromGallery() {

        //have to solve this shit to crop manifest and gradle app about crop
        //CropImage.activity().start(this);
    }

    private void requestCameraPermission() {requestPermissions(cameraPermission,CAMERA_REQUEST);}

    private boolean checkCameraPermission() {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
       // boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        return  result;
    }


    private void insertData() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put("name",name.getText().toString());
                cv.put("image",ImageViewToBy(image));

                sqLiteDatabase=databaseHelper.getWritableDatabase();
                long result = sqLiteDatabase.insert("testimage", null, cv);
                if (result== -1) {
                    Toast.makeText(Registo.this,"Not inserted",Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(Registo.this,"Inserted",Toast.LENGTH_LONG);
                    name.setText("");
                    image.setImageResource(R.mipmap.ic_launcher);
                }
            }
        });
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
        submit = (Button)findViewById(R.id.btn_submit);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if (grantResults.length>0){
                    boolean camera_accepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storage_accepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && storage_accepted){
                        picFromGallery();
                    }else {
                        Toast.makeText(this, "enabled camera and storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST:{
                if (grantResults.length>0){
                    boolean storage_accepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if (storage_accepted){
                        picFromGallery();
                    }else {
                        Toast.makeText(this, "please enable storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

        }
    }


    //check method below for the crop image purpose
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityRestult(data);
            if (resultCode==RESULT_OK){
                Uri resultUri=result.getUri();
                Picasso.with(this).load(resultUri).into(image);
            }
        }
    }
*/

}