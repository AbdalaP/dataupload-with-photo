package com.example.sch_agro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sch_agro.R;
import com.example.sch_agro.databinding.ActivitySignupBinding;
import com.example.sch_agro.util.DatabaseHelper;
import com.google.android.material.navigation.NavigationBarView;

public class SignupActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, AdapterView.OnItemSelectedListener {

    ActivitySignupBinding binding;
    DatabaseHelper databaseHelper;
    Spinner spinner_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        spinner_role = findViewById(R.id.spinner_role);
        loadSpinnerData(); //to populate spinner

        databaseHelper = new DatabaseHelper(this);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = binding.nome.getText().toString();
                String email = binding.signupEmail.getText().toString();
                String username = binding.username.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirm.getText().toString();
                String role= spinner_role.getItemAtPosition(spinner_role.getSelectedItemPosition()).toString();

                       // getItemAtPosition(spinner_role.getSelectedItemPosition()).toString());

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()||  nome.isEmpty() || username.isEmpty() ) {
                    Toast.makeText(
                            SignupActivity.this,
                            "All fields are mandatory",
                            Toast.LENGTH_SHORT
                    ).show();
                }else if(spinner_role.getItemAtPosition(spinner_role.getSelectedItemPosition()).toString().equals("[Escolhe a Função…]")){
                    Toast.makeText(SignupActivity.this, "Por favor selectione a Funcao!", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(confirmPassword)) {

                        Boolean checkUserEmail = databaseHelper.checkEmail(email);
                        Boolean checkusername = databaseHelper.checkusername(username);

                        if (!checkUserEmail||!checkusername) { // Check if "checkUserEmail and username if exist" returns false, do this

                            Boolean insert = databaseHelper.insertData(nome, email, username, password,role);

                            if (insert) { // insert successful
                                Toast.makeText(SignupActivity.this, "Signup Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignupActivity.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(SignupActivity.this, "User already exists! Please login", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Invalid Password!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadSpinnerData() {
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Role, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner_role.setAdapter(adapter1);
        spinner_role.setOnItemSelectedListener(this);

       // spinner_role.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}