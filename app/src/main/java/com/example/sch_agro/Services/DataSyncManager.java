package com.example.sch_agro.Services;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.sch_agro.Configuration.ApiClient;
import com.example.sch_agro.DAO.ActivityDAO;
import com.example.sch_agro.DAO.ControleActividadeDAO;
import com.example.sch_agro.DAO.TaskGebaDAO;
import com.example.sch_agro.DAO.TrabalhadoresDAO;
import com.example.sch_agro.DAO.UserDAO;
import com.example.sch_agro.DTO.LoginDTO;
import com.example.sch_agro.DTO.LoginResponseDTO;
import com.example.sch_agro.Model.Activity;
import com.example.sch_agro.Model.TaskGeba;
import com.example.sch_agro.Model.ControleActividade;
import com.example.sch_agro.Model.Trabalhadores;
import com.example.sch_agro.Model.User;
import com.example.sch_agro.util.ApiResponse;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class DataSyncManager {
    private final UserDAO usersDao;
    private final ActivityDAO activityDao;
    private final TrabalhadoresDAO trabalhadoresDao;
    private final TaskGebaDAO taskGebaDao;
    private final ControleActividadeDAO controleActividadeDAO;
    private final ApiService apiService;

    public DataSyncManager(UserDAO usersDao, ActivityDAO activityDao, TrabalhadoresDAO trabalhadoresDao, TaskGebaDAO taskGebaDao, ControleActividadeDAO controleActividadeDAO, ApiService apiService) {
        this.usersDao = usersDao;
        this.activityDao = activityDao;
        this.trabalhadoresDao = trabalhadoresDao;
        this.taskGebaDao = taskGebaDao;
        this.controleActividadeDAO = controleActividadeDAO;
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
//                        syncTaskGeba();
                        syncControleAtividadea();
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
        if (unsyncedData.isEmpty()) {
            return;
        }

        // Inicia a sincronização com o primeiro item
        syncNext(new ArrayList<>(unsyncedData), 0, sendFunction, updateFunction);
    }

    private <T> void syncNext(List<T> unsyncedData, int currentIndex,
                              Function<T, Call<Void>> sendFunction,
                              Consumer<T> updateFunction) {
        // Verifica se todos os itens foram processados
        if (currentIndex >= unsyncedData.size()) {
            return;
        }

        T currentItem = unsyncedData.get(currentIndex);
        sendFunction.apply(currentItem).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    updateFunction.accept(currentItem);
                    // Processa o próximo item apenas após o sucesso do atual
                    syncNext(unsyncedData, currentIndex + 1, sendFunction, updateFunction);
                } else {
                    System.err.println("Erro ao sincronizar: " + response.message());
                    // Em caso de erro, pode-se decidir continuar ou não com o próximo item
                    syncNext(unsyncedData, currentIndex + 1, sendFunction, updateFunction);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.err.println("Erro na requisição: " + t.getMessage());
                // Em caso de falha, pode-se decidir continuar ou não com o próximo item
                syncNext(unsyncedData, currentIndex + 1, sendFunction, updateFunction);
            }
        });
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void syncTrabalhadores() {
        List<Trabalhadores> unsyncedTrabalhadores = trabalhadoresDao.getUnsyncedTrabalhadores();
        if (unsyncedTrabalhadores.isEmpty()) {
            return;
        }

        syncNext(new ArrayList<>(unsyncedTrabalhadores), 0);
    }

    private void syncNext(List<Trabalhadores> unsyncedTrabalhadores, int currentIndex) {
        if (currentIndex >= unsyncedTrabalhadores.size()) {
            return;
        }

        Trabalhadores trabalhador = unsyncedTrabalhadores.get(currentIndex);

        // Converter trabalhador para JSON
        String trabalhadorJson = new Gson().toJson(trabalhador);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                trabalhadorJson
        );

        // Criar MultipartBody.Part para a imagem se existir
        MultipartBody.Part imagePart = null;
        byte[] imageBytes = trabalhador.getImage();
        if (imageBytes != null && imageBytes.length > 0) {
            RequestBody imageRequestBody = RequestBody.create(
                    MediaType.parse("image/*"),
                    imageBytes
            );
            imagePart = MultipartBody.Part.createFormData("image",
                    "image.jpg",  // Nome padrão para o arquivo
                    imageRequestBody
            );
        }

        // Criar RequestBody para o campo data
        RequestBody dataRequestBody = RequestBody.create(
                MediaType.parse("application/json"),
                trabalhadorJson
        );

        apiService.sendTrabalhador(dataRequestBody, imagePart).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    trabalhador.setSynced(true);
                    trabalhadoresDao.update(trabalhador);
                    // Processa o próximo item após sucesso
                    syncNext(unsyncedTrabalhadores, currentIndex + 1);
                } else {
                    System.err.println("Erro ao sincronizar: " + response.message());
                    // Decide se continua com o próximo em caso de erro
                    syncNext(unsyncedTrabalhadores, currentIndex + 1);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.err.println("Erro na requisição: " + t.getMessage());
                // Decide se continua com o próximo em caso de falha
                syncNext(unsyncedTrabalhadores, currentIndex + 1);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void syncControleAtividadea() {
        List<ControleActividade> unsyncedControleActividade = controleActividadeDAO.getUnsyncedTasks();
        syncData(unsyncedControleActividade,
                apiService::sendControleAtividade,
                task -> {
                    task.setSynced(true);
                    controleActividadeDAO.update(task);
                });
    }

}
