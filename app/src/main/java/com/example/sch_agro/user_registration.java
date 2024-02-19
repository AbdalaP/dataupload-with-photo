package com.example.sch_agro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.sch_agro.databinding.ActivityUserRegistrationBinding;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

public class user_registration extends AppCompatActivity {


    ActivityUserRegistrationBinding binding;
    DatabaseHelper databaseHelper;
    private static final int CAMERA_REQUEST_CODE = 200;//for camera
    public final int REQUEST_SELECT_PICTURE = 0x01;
    public final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static String TEMP_PHOTO_FILE_NAME ="photo_";
    Uri mImageCaptureUri;
    File mFileTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        databaseHelper = new DatabaseHelper(this);



binding.camera.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);

            } else {

            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (Exception e) {

            Log.d("error", "cannot take picture", e);
        }

    }


});








        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nome = binding.nome.getText().toString();
                String docid = binding.docid.getText().toString();
                String idade = binding.idade.getText().toString();

                String genderid = userGender.toString();

                String telefone = binding.telefone.getText().toString();
                if(nome.equals("")||docid.equals("")||idade.equals("")||genderid.equals(""))
                    Toast.makeText(user_registration.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else{
                   //
                    Boolean checdocid = databaseHelper.checkdocid(docid);
                    String userGender = "";
                 //   databaseHelper.RadioButtonClicked();
                    if(checdocid == false){
                        Boolean insert = databaseHelper.insertregistration(nome, docid, idade, telefone,genderid);
                       //databaseHelper.RadioButtonClicked(Srting userGender);
                        if(insert == true){

                            Toast.makeText(user_registration.this, "Dados inseridos com Sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(user_registration.this, "Erro ao Inserir!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(user_registration.this, "O Trabalhador ja foi registado no sistema!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            //insert ration button here


        });


    }

     String userGender="";
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
    
    






}