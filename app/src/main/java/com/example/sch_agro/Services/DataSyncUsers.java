package com.example.sch_agro.Services;

import android.content.Context;
import com.example.sch_agro.DAO.UserDAO;
import com.example.sch_agro.DTO.UserDTO;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSyncUsers {
    private final UserDAO usersDao;
    private final ApiService apiService;
    private Context context;

    public DataSyncUsers(UserDAO usersDao, ApiService apiService, Context context) {
        this.usersDao = usersDao;
        this.apiService = apiService;
        this.context = context;
    }

    public void syncData() {
        fetchUsers();
    }

    private boolean userExists(String email, String username) {
        return usersDao.userExists(email, username);
    }
    private void logError(Response<?> response) {
        if (response.errorBody() != null) {
            try {
                String errorMessage = response.errorBody().string();
                System.err.println("Mensagem de erro: " + errorMessage);
            } catch (IOException e) {
                System.err.println("Erro ao processar o corpo de erro: " + e.getMessage());
            }
        }
    }

    public static String removeRolePrefix(String role) {
        if (role != null && role.startsWith("ROLE_")) {
            return role.substring(5); // Remove os primeiros 5 caracteres ("ROLE_")
        }
        return role;
    }
    private void fetchUsers() {
        apiService.getAllUsers().enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserDTO> users = response.body();

                    for (UserDTO user : users) {
                        if (!userExists(user.getEmail(), user.getUsername())) {

                            boolean inserted = usersDao.insertUser(
                                    user.getNomeCompleto(),
                                    user.getEmail(),
                                    user.getUsername(),
                                    user.getPassword(),
                                    removeRolePrefix(user.getRoles())
                            );

                            if (inserted) {
                                System.out.println("User inserido: " + user.getNomeCompleto());
                            } else {
                                System.out.println("Falha ao inserir o user: " + user.getNomeCompleto());
                            }
                        } else {
                            System.out.println("User já existe: " + user.getNomeCompleto());
                        }
                    }
                } else {
                    System.err.println("Falha ao buscar users. Código: " + response.code());
                    logError(response);
                }
            }

            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                System.err.println("Falha ao tentar buscar users.");
                System.err.println("Detalhes do erro: " + t.getMessage());
            }
        });
    }
}
