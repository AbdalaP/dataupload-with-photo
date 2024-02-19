package com.example.sch_agro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sch_agro.databinding.ActivityAddBinding;

import androidx.appcompat.app.AppCompatActivity;

public class add_activity extends AppCompatActivity {

    ActivityAddBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);
        binding.adicionarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = binding.name.getText().toString();
                String pessoa = binding.pessoaResponsavel.getText().toString();
                if(nome.equals("")||pessoa.equals(""))
                    Toast.makeText(add_activity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else{
                        Boolean checkactivity = databaseHelper.checkactivity(nome);
                        if(checkactivity == false){
                            Boolean insert = databaseHelper.insertactivity(nome, pessoa);
                            if(insert == true){
                                Toast.makeText(add_activity.this, "Dados inseridos com Sucesso!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(add_activity.this, "Erro ao Inserir!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(add_activity.this, "User already exists! Please login", Toast.LENGTH_SHORT).show();
                        }
                }
            }
        });

    }

    }
