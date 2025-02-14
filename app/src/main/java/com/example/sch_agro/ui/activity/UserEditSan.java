package com.example.sch_agro.ui.activity;

import static com.example.sch_agro.R.mipmap.ic_launcher;
import static com.example.sch_agro.util.DatabaseHelper.controle_actividade;

import android.annotation.SuppressLint;
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

  // private EditText target;
   private TextView totals;
   private TextWatcher text = null;
   Button button;


    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    TextView name;
    TextView activity_id;
    EditText target;
    TextView trabalhador_id;
    Spinner spinner;


    Button btn_edit;
    Button btn_falta;
    ImageView image;

    public static final int CAMERA_REQUEST=100;
    public static final int STORAGE_REQUEST=101;
    String cameraPermission[];
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controlo_actividades);

       // Session session = new Session(this);
        databaseHelper= new DatabaseHelper(this);
        findid();
        editData();
        insertData();
//        spinner = findViewById(R.id.spinner1);
//        spinner.setOnItemSelectedListener(this);
//        loadSpinnerData();


       // targets = (EditText)findViewById(R.id.target);
        target = (EditText)findViewById(R.id.target);
       // totals = (TextView)findViewById(R.id.total);
        button =(Button)findViewById(R.id.btn_edit);
        button =(Button)findViewById(R.id.btn_falta);

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
        Session session = new Session(this);
        String act = activity_id.getText().toString();
      //  databaseHelper.checkcategory(act); // por a funcionar depois para levar funcao no activity
       // if (act!="Presença")
if (act.equals("Motorista")||act.equals("Ajudante")){
    View b = findViewById(R.id.btn_edit);
    b.setVisibility(View.GONE);
    View c = findViewById(R.id.target);
    c.setVisibility(View.GONE);

    View d = findViewById(R.id.btn_falta);
    d.setVisibility(View.VISIBLE);
   // insertDataMotorista(); //falta button

}else {
    View b = findViewById(R.id.btn_edit);
    b.setVisibility(View.VISIBLE);
    View c = findViewById(R.id.target);
    c.setVisibility(View.VISIBLE);

    View d = findViewById(R.id.btn_falta);
    d.setVisibility(View.GONE);



}
       // Session session = new Session(this); //calling session class that keeps de userid after user logsin
        //////////button edit or inserting new data tasksan
        btn_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                // sqLiteDatabase=databaseHelper.getReadableDatabase();
                String thisUser = session.getKeyUserId();
                ContentValues cv =new ContentValues();
                cv.put("activity_id",activity_id.getText().toString());
               // cv.put("act",spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
                cv.put("trabalhador_id",trabalhador_id.getText().toString());
                cv.put("target",target.getText().toString());
                cv.put("userlog",thisUser);

                sqLiteDatabase=databaseHelper.getWritableDatabase();

                if (target.length()==0) {
                    Toast.makeText(UserEditSan.this, "Insira o Numero da meta diaria", Toast.LENGTH_SHORT).show();
                }else {
                    long recedit = sqLiteDatabase.insert(controle_actividade, null, cv);
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
                }

            }
        });

        btn_falta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues cv =new ContentValues();
                String thisUser = session.getKeyUserId();
                cv.put("activity_id",activity_id.getText().toString());
                // cv.put("act",spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
                cv.put("trabalhador_id",trabalhador_id.getText().toString());
                cv.put("target",target.getText().toString());
                cv.put("faltas","True");
                cv.put("userlog",thisUser);

                sqLiteDatabase=databaseHelper.getWritableDatabase();


                long recedit = sqLiteDatabase.insert(controle_actividade, null, cv);
                if (recedit != -1) {
                    Toast.makeText(UserEditSan.this, "Data Inserted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    image.setImageResource(ic_launcher);
                    name.setText("");
                    btn_falta.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(UserEditSan.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
   // Session session = new Session(this); //calling session class that keeps de userid after user logsin


    private void editData() {
        if(getIntent().getBundleExtra("userdata")!=null){
            Bundle bundle=getIntent().getBundleExtra("userdata");
            byte[]bytes=bundle.getByteArray("image");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            image.setImageBitmap(bitmap);
            trabalhador_id.setText(bundle.getString("trabalhador_id"));
            activity_id.setText(bundle.getString("activity_id"));
            name.setText(bundle.getString("name"));
           // target.setText(bundle.getString("target"));
            target.setText(bundle.getString("target"));
            name.setEnabled(false);
            image.setEnabled(false);
            trabalhador_id.setEnabled(false);
            activity_id.setEnabled(false);

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
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();

        }

    }


    private void findid() {


        //target = (EditText)findViewById(R.id.target);
        name= (TextView)findViewById(R.id.nome);
        target= (EditText)findViewById(R.id.target);
        activity_id= (TextView)findViewById(R.id.activity_id);
        trabalhador_id= (TextView)findViewById(R.id.trabalhador_id);
        image = (ImageView)findViewById(R.id.edtimage);
        btn_edit = (Button)findViewById(R.id.btn_edit);
        btn_falta = (Button)findViewById(R.id.btn_falta);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}