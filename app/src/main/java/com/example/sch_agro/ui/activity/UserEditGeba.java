package com.example.sch_agro.ui.activity;

import static com.example.sch_agro.R.mipmap.ic_launcher;
import static com.example.sch_agro.util.DatabaseHelper.controle_actividade;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.sch_agro.R;
import com.example.sch_agro.util.DatabaseHelper;

import java.io.ByteArrayOutputStream;

public class UserEditGeba extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    TextView name;
     Spinner status;
    TextView trabalhador_id;
    Button btn_update;
    ImageView image;


    public static final int CAMERA_REQUEST=100;
    public static final int STORAGE_REQUEST=101;
    String cameraPermission[];
    String storagePermission[];




    Uri mImageCaptureUri;
    //File mFileTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usereditgeba);

        databaseHelper= new DatabaseHelper(this);

        findid();
        editData();
        insertData();
        status = findViewById(R.id.status);
        status.setOnItemSelectedListener(this);
        loadSpinnerData();

        //image= findViewById(R.id.edtimage);


    }

    //The query to populate this spineer is in the databaseHelper


    //to choose the textfied to show bunddle data is here.
    private void editData() {
        if(getIntent().getBundleExtra("userdata")!=null){
            Bundle bundle=getIntent().getBundleExtra("userdata");
            byte[]bytes=bundle.getByteArray("image");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            image.setImageBitmap(bitmap);
            trabalhador_id.setText(bundle.getString("trabalhador_id"));
            name.setText(bundle.getString("name"));
            // target.setText(bundle.getString("target"));
            name.setEnabled(false);
            image.setEnabled(false);
            trabalhador_id.setEnabled(false);


        }
    }

    String userGender = "";
    String checked;



    private void requestCameraPermission() {requestPermissions(cameraPermission,CAMERA_REQUEST);}

    private boolean checkCameraPermission() {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        return  result && result1;
    }

    private void insertData() {
        //////////button edit or inserting new data task
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spinner= status.getItemAtPosition(status.getSelectedItemPosition()).toString();
                ContentValues cv =new ContentValues();
                // cv.put("status",status.getItemAtPosition(status.getSelectedItemPosition()).toString());
                cv.put("trabalhador_id",trabalhador_id.getText().toString());
                //cv.put("faltas","True");
                sqLiteDatabase=databaseHelper.getWritableDatabase();
                long recedit = sqLiteDatabase.insert(controle_actividade, null, cv);
                if (recedit != -1) {
                    Toast.makeText(UserEditGeba.this, "Data Inserted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    image.setImageResource(ic_launcher);
                    name.setText("");

                } else {
                    Toast.makeText(UserEditGeba.this, "Something is wrong", Toast.LENGTH_SHORT).show();
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

    private void loadSpinnerData() {
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Empresa, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.select_dialog_item);
        status.setAdapter(adapter1);
        status.setOnItemSelectedListener(this);


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
        //target = (EditText)findViewById(R.id.target);
        name= (TextView)findViewById(R.id.name);
        trabalhador_id= (TextView)findViewById(R.id.trabalhador_id);
        image = (ImageView)findViewById(R.id.edtimage);
        btn_update = (Button)findViewById(R.id.btn_update);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}