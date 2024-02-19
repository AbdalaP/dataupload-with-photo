package com.example.sch_agro;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sch_agro.databinding.ActivityTakephotoBinding;

import androidx.appcompat.app.AppCompatActivity;

public class TakePhoto extends AppCompatActivity {


    SQLiteDatabase sqLiteDatabase;
    EditText name;
    Button submit;
    private ImageView image;
    ImageView imageViewshow;
    Button buttonshow;

    ActivityTakephotoBinding binding;
    DatabaseHelper databaseHelper;
   // private Bitmap imageTostore;
    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takephoto);

        databaseHelper = new DatabaseHelper(this);

        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageView image = v.findViewById(R.id.edtimage);
                boolean pick=true;
                if (pick==true){
                    if (!)

                }else {

                }

            }
        });



        binding.btnSubmit(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name = binding.edtname.getText().toString();
                String idade = binding.idade.getText().toString();



                if(name.equals("")||imageView.equals(""))
                    Toast.makeText(TakePhoto.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else{

                        Boolean insert = databaseHelper.insertimage(name);

                        if(insert == true){

                            Toast.makeText(TakePhoto.this, "Dados inseridos com Sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(TakePhoto.this, "Erro ao Inserir!", Toast.LENGTH_SHORT).show();
                        }

                }
            }
            //insert ration button here


        });


    }




}