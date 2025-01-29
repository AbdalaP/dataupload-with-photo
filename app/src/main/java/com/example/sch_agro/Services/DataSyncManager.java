package com.example.sch_agro.Services;

import com.example.sch_agro.Configuration.ApiClient;
import com.example.sch_agro.DAO.ActivityDAO;
import com.example.sch_agro.DAO.TaskGebaDAO;
import com.example.sch_agro.DAO.TaskSanDAO;
import com.example.sch_agro.DAO.TrabalhadoresDAO;
import com.example.sch_agro.DAO.UserDAO;
import com.example.sch_agro.DTO.LoginDTO;
import com.example.sch_agro.DTO.LoginResponseDTO;
import com.example.sch_agro.Model.Activity;
import com.example.sch_agro.Model.TaskGeba;
import com.example.sch_agro.Model.TaskSan;
import com.example.sch_agro.Model.Trabalhadores;
import com.example.sch_agro.Model.User;
import com.example.sch_agro.util.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DataSyncManager {
    private final UserDAO usersDao;
    private final ActivityDAO activityDao;
    private final TrabalhadoresDAO trabalhadoresDao;
    private final TaskGebaDAO taskGebaDao;
    private final TaskSanDAO taskSanDao;
    private final ApiService apiService;

    public DataSyncManager(UserDAO usersDao, ActivityDAO activityDao, TrabalhadoresDAO trabalhadoresDao, TaskGebaDAO taskGebaDao, TaskSanDAO taskSanDao, ApiService apiService) {
        this.usersDao = usersDao;
        this.activityDao = activityDao;
        this.trabalhadoresDao = trabalhadoresDao;
        this.taskGebaDao = taskGebaDao;
        this.taskSanDao = taskSanDao;
        this.apiService = apiService;
    }

    public void syncData() {
        login();
    }

    private void login() {
        LoginDTO login = new LoginDTO("cmoda@jfs.san.co.mz", "  dev0");

        apiService.login(login).enqueue(new Callback<ApiResponse<LoginResponseDTO>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponseDTO>> call, Response<ApiResponse<LoginResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<LoginResponseDTO> apiResponse = response.body();

                    if (apiResponse.isSuccess()) {
                        System.out.println("Login bem-sucedido: " + apiResponse.getMessage());
                        String token = apiResponse.getData().getToken(); // Obtenção do token retornado
                        System.out.println("Token recebido: " + token);

                        // Configurar o token no ApiClient
                        ApiClient.setToken(token);

                        // Sincronizações necessárias após o login
                        syncUsers();
                        syncActivities();
                        syncTrabalhadores();
                        syncTaskGeba();
                        syncTaskSan();
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
    }

    private <T> void syncData(List<T> unsyncedData, Function<T, Call<Void>> sendFunction, Consumer<T> updateFunction) {
        for (T item : unsyncedData) {
            sendFunction.apply(item).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        updateFunction.accept(item); // Atualiza como sincronizado
                    } else {
                        System.err.println("Erro ao sincronizar: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    System.err.println("Erro na requisição: " + t.getMessage());
                }
            });
        }
    }


    private void syncUsers() {
        List<User> unsyncedUsers = usersDao.getUnsyncedUsers();
        syncData(unsyncedUsers,
                apiService::sendUser,
                user -> {
                    user.setSynced(true);
                    usersDao.update(user);
                });
    }

    private void syncActivities() {
        List<Activity> unsyncedActivities = activityDao.getUnsyncedActivities();
        syncData(unsyncedActivities,
                apiService::sendActivity,
                activity -> {
                    activity.setSynced(true);
                    activityDao.update(activity);
                });
    }

    private void syncTrabalhadores() {
        List<Trabalhadores> unsyncedTrabalhadores = trabalhadoresDao.getUnsyncedTrabalhadores();
        syncData(unsyncedTrabalhadores,
                apiService::sendTrabalhador,
                trabalhador -> {
                    trabalhador.setSynced(true);
                    trabalhadoresDao.update(trabalhador);
                });
    }

    private void syncTaskGeba() {
        List<TaskGeba> unsyncedTaskGeba = taskGebaDao.getUnsyncedTasks();
        syncData(unsyncedTaskGeba,
                apiService::sendTaskGeba,
                task -> {
                    task.setSynced(true);
                    taskGebaDao.update(task);
                });
    }

    private void syncTaskSan() {
        List<TaskSan> unsyncedTaskSan = taskSanDao.getUnsyncedTasks();
        syncData(unsyncedTaskSan,
                apiService::sendTaskSan,
                task -> {
                    task.setSynced(true);
                    taskSanDao.update(task);
                });
    }

}
