package com.example.sch_agro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sch_agro.Configuration.ApiClient;
import com.example.sch_agro.Configuration.DatabaseInstance;
import com.example.sch_agro.DAO.ActivityDAO;
import com.example.sch_agro.DAO.ControleActividadeDAO;
import com.example.sch_agro.DAO.TaskGebaDAO;
import com.example.sch_agro.DAO.TrabalhadoresDAO;
import com.example.sch_agro.DAO.UserDAO;
import com.example.sch_agro.Services.ApiService;
import com.example.sch_agro.Services.DataSyncManager;
import com.example.sch_agro.Services.DataSyncUsers;
import com.example.sch_agro.Services.NetworkMonitor;
import com.example.sch_agro.databinding.ActivityLoginBinding;
import com.example.sch_agro.util.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Session session = new Session(this);

        databaseHelper = new DatabaseHelper(this);

        // Inicialização do monitor de rede
        NetworkMonitor networkMonitor = new NetworkMonitor(this);

        // Instância dos DAOs
        UserDAO userDao = DatabaseInstance.getInstance(this).userDao();

        // Instância da ApiService
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Instância do DataSyncManager com os DAOs e ApiService
        DataSyncUsers syncManager = new DataSyncUsers(userDao, apiService, this);

        // Iniciar o monitoramento da rede e executar sincronização quando conectado
        networkMonitor.startMonitoring(syncManager::syncData);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String nome = binding.loginEmail.getText();
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();
                if(email.equals("")||password.equals(""))
                    Toast.makeText(LoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);
                    if(checkCredentials == true){
                        Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                        session.createLoginSession(email);
                        Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}