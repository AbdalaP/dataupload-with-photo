package com.example.sch_agro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.sch_agro.databinding.ActivityTakephotoBinding;
import com.example.sch_agro.util.DatabaseHelper;

import java.io.ByteArrayOutputStream;

public class TakePhoto extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase;
    EditText name;
    Button btn_submit;
    ImageView images;

    ActivityTakephotoBinding binding;
    DatabaseHelper databaseHelper;
   // private Bitmap imageTostore;


    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

        }
    });
   //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        insertdatas();
        setContentView(R.layout.activity_takephoto);

       // databaseHelper = new DatabaseHelper(this);
       images= findViewById(R.id.images);


        if (ContextCompat.checkSelfPermission(TakePhoto.this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(TakePhoto.this,new String[]{
                    Manifest.permission.CAMERA
            },100);
        }

        images.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
               // startForResult.launch(intent);

            }


        });



    }

private void insertdatas(){
    btn_submit= findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TakePhoto.this, "vamos ver agora", Toast.LENGTH_SHORT).show();
            }
        });


}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            images.setImageBitmap(bitmap);

        }else {
            Toast.makeText(this, "something is wrong", Toast.LENGTH_SHORT).show();

        }

    }



    public static byte[] ImageViewToBy(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
        byte[]bytes=stream.toByteArray();
        return bytes;
    }



}