package com.example.sch_agro.ui.activity;

import static com.example.sch_agro.R.mipmap.ic_launcher;
import static com.example.sch_agro.util.DatabaseHelper.trabalhadores;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sch_agro.R;
import com.example.sch_agro.util.DatabaseHelper;


public class UserActivoInativo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    TextView name;
    Spinner status;
    EditText trabalhador_id;
    Button btn_update;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_activo_inativo);
        status = findViewById(R.id.status);
        status.setOnItemSelectedListener(this);
        databaseHelper= new DatabaseHelper(this);
        findid();
        editData();
        insertData();
        loadSpinnerData();






    }

    private void insertData() {
        //////////button update para actualizar o estado do trabalhador activo ou inativo
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputEditText = findViewById(R.id.trabalhador_id);
                ContentValues cv =new ContentValues();
                 cv.put("status",status.getItemAtPosition(status.getSelectedItemPosition()).toString());
                sqLiteDatabase=databaseHelper.getWritableDatabase();
                if (status.getItemAtPosition(status.getSelectedItemPosition()).toString().equals("[Escolhe Status…]")){
                    Toast.makeText(UserActivoInativo.this, "Por Favor Escolhe o Status do Trabalhador!", Toast.LENGTH_SHORT).show();
                }else {
                    long recedit = sqLiteDatabase.update(trabalhadores, cv, "id = ?", new String[]{inputEditText.getText().toString()});
                    if (recedit != -1) {
                        Toast.makeText(UserActivoInativo.this, "Data Updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        image.setImageResource(ic_launcher);
                        name.setText("");

                    } else {
                        Toast.makeText(UserActivoInativo.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                    }
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
            trabalhador_id.setText(bundle.getString("trabalhador_id"));
            name.setText(bundle.getString("name"));
            // target.setText(bundle.getString("target"));
            name.setEnabled(false);
            image.setEnabled(false);
            trabalhador_id.setEnabled(false);


        }
    }

    private void findid() {
        name= (TextView)findViewById(R.id.name);
        trabalhador_id= (EditText)findViewById(R.id.trabalhador_id);
        image = (ImageView)findViewById(R.id.edtimage);
        btn_update = (Button)findViewById(R.id.btn_update);
    }

    private void loadSpinnerData() {
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.select_dialog_item);
        status.setAdapter(adapter1);
        status.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}