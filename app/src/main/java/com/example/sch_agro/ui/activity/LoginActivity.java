package com.example.sch_agro.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sch_agro.Configuration.ApiClient;
import com.example.sch_agro.Configuration.DatabaseInstance;
import com.example.sch_agro.DAO.ActivityDAO;
import com.example.sch_agro.DAO.ControleActividadeDAO;
import com.example.sch_agro.DAO.TaskGebaDAO;
import com.example.sch_agro.DAO.TrabalhadoresDAO;
import com.example.sch_agro.DAO.UserDAO;
import com.example.sch_agro.DTO.LoginDTO;
import com.example.sch_agro.DTO.LoginResponseDTO;
import com.example.sch_agro.Services.ApiService;
import com.example.sch_agro.Services.DataSyncManager;
import com.example.sch_agro.Services.DataSyncUsers;
import com.example.sch_agro.Services.NetworkMonitor;
import com.example.sch_agro.databinding.ActivityLoginBinding;
import com.example.sch_agro.util.ApiResponse;
import com.example.sch_agro.util.DatabaseHelper;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                        LoginDTO login = new LoginDTO(email, password);
                        apiService.login(login).enqueue(new Callback<ApiResponse<LoginResponseDTO>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<LoginResponseDTO>> call, Response<ApiResponse<LoginResponseDTO>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    ApiResponse<LoginResponseDTO> apiResponse = response.body();
                                    if (apiResponse.isSuccess()) {
                                        System.out.println("Login bem-sucedido: " + apiResponse.getMessage());
                                        String token = apiResponse.getData().getToken();

                                        // Configurar o token no ApiClient
                                        ApiClient.setToken(token);
                                    } else {
                                        System.err.println("Erro no login: " + apiResponse.getMessage());
                                    }
                                } else {
                                    System.err.println("Falha no login. Código: " + response.code());
                                    if (response.errorBody() != null) {
                                        try {
                                            String errorMessage = response.errorBody().string();
                                            System.err.println("Mensagem de erro: " + errorMessage);
                                        } catch (IOException e) {
                                            System.err.println("Erro ao processar o corpo de erro: " + e.getMessage());
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse<LoginResponseDTO>> call, Throwable t) {
                                System.err.println("Falha ao tentar realizar o login.");
                                System.err.println("Detalhes do erro: " + t.getMessage());
                            }
                        });
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