package com.example.sch_agro.ui.activity;

import static com.example.sch_agro.R.mipmap.ic_launcher;
import static com.example.sch_agro.util.DatabaseHelper.tasksan;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sch_agro.R;
import com.example.sch_agro.util.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class UserEditSan extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

   private EditText targets,feitas;
   private TextView totals;
   private TextWatcher text = null;
   Button button;


    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText name,id,target,feita;
    Spinner spinner;


    Button btn_edit;
    ImageView image;

    public static final int CAMERA_REQUEST=100;
    public static final int STORAGE_REQUEST=101;
    String cameraPermission[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_san);

        databaseHelper= new DatabaseHelper(this);
        findid();
        editData();
        insertData();
        spinner = findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);
        loadSpinnerData();


       // targets = (EditText)findViewById(R.id.target);
        feitas = (EditText)findViewById(R.id.feita);
       // totals = (TextView)findViewById(R.id.total);
        button =(Button)findViewById(R.id.btn_edit);

/*
    button.setOnClickListener(new View.OnClickListener() {
      //  @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            int n = Integer.parseInt(targets.getText().toString());
            int ns= Integer.parseInt(feitas.getText().toString());
            float sum = (float)ns/n;
            float reminder = (float)ns-n;

            totals.setText("Answer: " + (sum));
            if (n>ns){
                Toast.makeText(UserEditSan.this, "A meta é maior que o realizado "+ reminder +" sacos", Toast.LENGTH_SHORT).show();
            }else if(n==ns) {
                Toast.makeText(UserEditSan.this, "Parabens Meta atingida " + n +" un", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(UserEditSan.this, "A meta é menor que o realizado " + reminder +" sacos", Toast.LENGTH_SHORT).show();
            }

        }
    });

 */



     /*
        text = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                totals.setText(String.valueOf(sum));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };



        feitas.addTextChangedListener(text);

      */




    }

    //The query to populate this spineer is in the databaseHelper
    private void loadSpinnerData() {
        List<String> labels = databaseHelper.getAllData();
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

    public static byte[] ImageViewToBy(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[]bytes=stream.toByteArray();
        return bytes;
    }

    private void insertData() {

        //////////button edit or inserting new data tasksan
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // sqLiteDatabase=databaseHelper.getReadableDatabase();

                ContentValues cv =new ContentValues();
                cv.put("image",ImageViewToBy(image));//is diferente imageviewtobyte original.
                cv.put("name",name.getText().toString());
                cv.put("act",spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
                cv.put("user_id",id.getText().toString());
               // cv.put("target",target.getText().toString());
                cv.put("feita",feita.getText().toString());

                sqLiteDatabase=databaseHelper.getWritableDatabase();

                if (feita.length()==0) {
                    Toast.makeText(UserEditSan.this, "Insira o Numero de bloco", Toast.LENGTH_SHORT).show();
                }
                if (spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString() != "[Escolhe Actividade...]") {
                    long recedit = sqLiteDatabase.insert(tasksan, null, cv);
                    if (recedit != -1) {
                        Toast.makeText(UserEditSan.this, "Data Inserted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        image.setImageResource(ic_launcher);
                        name.setText("");
                        btn_edit.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(UserEditSan.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(UserEditSan.this, "Escolhe Actividade", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void editData() {
        if(getIntent().getBundleExtra("userdata")!=null){
            Bundle bundle=getIntent().getBundleExtra("userdata");
            byte[]bytes=bundle.getByteArray("image");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            image.setImageBitmap(bitmap);
            id.setText(bundle.getString("user_id"));
            name.setText(bundle.getString("name"));
           // target.setText(bundle.getString("target"));
            feita.setText(bundle.getString("feita"));
            name.setEnabled(false);
            image.setEnabled(false);
            id.setEnabled(false);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if (grantResults.length>0){
                    boolean camera_accepted=grantResults[0]== PackageManager.PERMISSION_GRANTED;
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
            Toast.makeText(this, "something is shit", Toast.LENGTH_SHORT).show();

        }

    }


    private void findid() {


        //target = (EditText)findViewById(R.id.target);
        name= (EditText)findViewById(R.id.nome);
        feita= (EditText)findViewById(R.id.feita);

        spinner= (Spinner) findViewById(R.id.spinner1);
        id= (EditText)findViewById(R.id.user_id);
        image = (ImageView)findViewById(R.id.edtimage);
        btn_edit = (Button)findViewById(R.id.btn_edit);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}