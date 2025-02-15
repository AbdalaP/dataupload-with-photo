package com.example.sch_agro.Services;

import static com.example.sch_agro.R.drawable.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.sch_agro.Configuration.ApiClient;
import com.example.sch_agro.DAO.ActivityDAO;
import com.example.sch_agro.DAO.ControleActividadeDAO;
import com.example.sch_agro.DAO.TaskGebaDAO;
import com.example.sch_agro.DAO.TrabalhadoresDAO;
import com.example.sch_agro.DAO.UserDAO;
import com.example.sch_agro.DTO.ActivityResponseDTO;
import com.example.sch_agro.DTO.LoginDTO;
import com.example.sch_agro.DTO.LoginResponseDTO;
import com.example.sch_agro.DTO.TrabalhadorResponseDTO;
import com.example.sch_agro.DTO.UserDTO;
import com.example.sch_agro.Model.Activity;
import com.example.sch_agro.Model.TaskGeba;
import com.example.sch_agro.Model.ControleActividade;
import com.example.sch_agro.Model.Trabalhadores;
import com.example.sch_agro.Model.User;
import com.example.sch_agro.R;
import com.example.sch_agro.util.ApiResponse;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

public class DataSyncManager {
    private final UserDAO usersDao;
    private final ActivityDAO activityDao;
    private final TrabalhadoresDAO trabalhadoresDao;
    private final TaskGebaDAO taskGebaDao;
    private final ControleActividadeDAO controleActividadeDAO;
    private final ApiService apiService;
    private final Context context;
    private final ExecutorService executorService;
    private final Set<String> pendingOperations = Collections.synchronizedSet(new HashSet<>());
    private SyncCompletionCallback completionCallback;

    public interface SyncCompletionCallback {
        void onSyncCompleted();
        void onSyncError(String error);
    }

    public DataSyncManager(UserDAO usersDao, ActivityDAO activityDao, TrabalhadoresDAO trabalhadoresDao,
                           TaskGebaDAO taskGebaDao, ControleActividadeDAO controleActividadeDAO,
                           ApiService apiService, Context context) {
        this.usersDao = usersDao;
        this.activityDao = activityDao;
        this.trabalhadoresDao = trabalhadoresDao;
        this.taskGebaDao = taskGebaDao;
        this.controleActividadeDAO = controleActividadeDAO;
        this.apiService = apiService;
        this.context = context;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void syncData(SyncCompletionCallback callback) {
        this.completionCallback = callback;
        pendingOperations.clear();

        // Registrar todas as operações de sincronização
        pendingOperations.add("users");
        pendingOperations.add("activities");
        pendingOperations.add("trabalhadores");
        pendingOperations.add("controleActividades");
        pendingOperations.add("fetchActivities");
        pendingOperations.add("fetchTrabalhadores");

        // Iniciar todas as operações de sincronização
        syncUsers();
        syncActivities();
        syncTrabalhadores();
        syncControleAtividadea();
        fetchActivities();
        fetchTrabalhadores();
    }

    private void onOperationComplete(String operation) {
        pendingOperations.remove(operation);
        Log.d("SYNC", "Operação completada: " + operation + ". Restantes: " + pendingOperations.size());

        if (pendingOperations.isEmpty()) {
            completionCallback.onSyncCompleted();
        }
    }

    private void syncUsers() {
        List<User> unsyncedUsers = usersDao.getUnsyncedUsers();
        if (unsyncedUsers.isEmpty()) {
            onOperationComplete("users");
            return;
        }

        AtomicInteger completedCount = new AtomicInteger(0);

        for (User user : unsyncedUsers) {
            apiService.sendUser(user).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        user.setSynced(true);
                        usersDao.update(user);
                    }
                    if (completedCount.incrementAndGet() == unsyncedUsers.size()) {
                        onOperationComplete("users");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("SYNC", "Erro ao sincronizar usuário: " + t.getMessage());
                    if (completedCount.incrementAndGet() == unsyncedUsers.size()) {
                        onOperationComplete("users");
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void syncActivities() {
        List<Activity> unsyncedActivities = activityDao.getUnsyncedActivities();
        if (unsyncedActivities.isEmpty()) {
            onOperationComplete("activities");
            return;
        }

        AtomicInteger completedCount = new AtomicInteger(0);

        for (Activity activity : unsyncedActivities) {
            apiService.sendActivity(activity).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        activity.setSynced(true);
                        activityDao.update(activity);
                    }
                    if (completedCount.incrementAndGet() == unsyncedActivities.size()) {
                        onOperationComplete("activities");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("SYNC", "Erro ao sincronizar atividade: " + t.getMessage());
                    if (completedCount.incrementAndGet() == unsyncedActivities.size()) {
                        onOperationComplete("activities");
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void syncTrabalhadores() {
        List<Trabalhadores> unsyncedTrabalhadores = trabalhadoresDao.getUnsyncedTrabalhadores();
        if (unsyncedTrabalhadores.isEmpty()) {
            onOperationComplete("trabalhadores");
            return;
        }

        AtomicInteger completedCount = new AtomicInteger(0);

        for (Trabalhadores trabalhador : unsyncedTrabalhadores) {
            String trabalhadorJson = new Gson().toJson(trabalhador);
            RequestBody dataRequestBody = RequestBody.create(
                    MediaType.parse("application/json"),
                    trabalhadorJson
            );

            MultipartBody.Part imagePart = null;
            byte[] imageBytes = trabalhador.getImage();
            if (imageBytes != null && imageBytes.length > 0) {
                RequestBody imageRequestBody = RequestBody.create(
                        MediaType.parse("image/*"),
                        imageBytes
                );
                imagePart = MultipartBody.Part.createFormData("image", "image.jpg", imageRequestBody);
            }

            apiService.sendTrabalhador(dataRequestBody, imagePart).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        trabalhador.setSynced(true);
                        trabalhadoresDao.update(trabalhador);
                    }
                    if (completedCount.incrementAndGet() == unsyncedTrabalhadores.size()) {
                        onOperationComplete("trabalhadores");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("SYNC", "Erro ao sincronizar trabalhador: " + t.getMessage());
                    if (completedCount.incrementAndGet() == unsyncedTrabalhadores.size()) {
                        onOperationComplete("trabalhadores");
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void syncControleAtividadea() {
        List<ControleActividade> unsyncedControls = controleActividadeDAO.getUnsyncedTasks();
        if (unsyncedControls.isEmpty()) {
            onOperationComplete("controleActividades");
            return;
        }

        AtomicInteger completedCount = new AtomicInteger(0);

        for (ControleActividade control : unsyncedControls) {
            apiService.sendControleAtividade(control).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        control.setSynced(true);
                        controleActividadeDAO.update(control);
                    }
                    if (completedCount.incrementAndGet() == unsyncedControls.size()) {
                        onOperationComplete("controleActividades");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("SYNC", "Erro ao sincronizar controle de atividade: " + t.getMessage());
                    if (completedCount.incrementAndGet() == unsyncedControls.size()) {
                        onOperationComplete("controleActividades");
                    }
                }
            });
        }
    }

    private void fetchActivities() {
        apiService.getAllActivitys().enqueue(new Callback<List<ActivityResponseDTO>>() {
            @Override
            public void onResponse(Call<List<ActivityResponseDTO>> call, Response<List<ActivityResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ActivityResponseDTO> activities = response.body();
                    for (ActivityResponseDTO activity : activities) {
                        if (!activityExists(activity.getDesignacao(), activity.getEmpresa())) {
                            activityDao.insertActivity(
                                    activity.getEmpresa(),
                                    activity.getDesignacao(),
                                    activity.getResponsavel(),
                                    String.valueOf(activity.getValor()),
                                    activity.getTipo(),
                                    "admin"
                            );
                        }
                    }
                }
                onOperationComplete("fetchActivities");
            }

            @Override
            public void onFailure(Call<List<ActivityResponseDTO>> call, Throwable t) {
                Log.e("SYNC", "Erro ao buscar atividades: " + t.getMessage());
                onOperationComplete("fetchActivities");
            }
        });
    }

    private void fetchTrabalhadores() {
        apiService.getAllTrabalhadores().enqueue(new Callback<List<TrabalhadorResponseDTO>>() {
            @Override
            public void onResponse(Call<List<TrabalhadorResponseDTO>> call, Response<List<TrabalhadorResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TrabalhadorResponseDTO> trabalhadores = response.body();
                    for (TrabalhadorResponseDTO trabalhador : trabalhadores) {
                        if (!trabalhadorExists(trabalhador.getDocumentoIdentificacao(), trabalhador.getEmpresaNome())) {
                            String url = substituirString(trabalhador.getImagem(), "localhost:8081", "192.168.8.102:8081");
                            byte[] imageBytes = downloadImage(url);
                            trabalhadoresDao.insertTrabalhador(
                                    trabalhador.getEmpresaNome(),
                                    trabalhador.getNome(),
                                    trabalhador.getDocumentoIdentificacao(),
                                    trabalhador.getDataNascimento(),
                                    trabalhador.getGenero() == "MASCULINO" ? "male" : "female",
                                    trabalhador.getTelefone(),
                                    trabalhador.getAtividade(),
                                    "admin",
                                    imageBytes,
                                    trabalhador.getEstado()
                            );
                        }
                    }
                }
                onOperationComplete("fetchTrabalhadores");
            }

            @Override
            public void onFailure(Call<List<TrabalhadorResponseDTO>> call, Throwable t) {
                Log.e("SYNC", "Erro ao buscar trabalhadores: " + t.getMessage());
                onOperationComplete("fetchTrabalhadores");
            }
        });
    }

    // Helper methods remain the same
    public static String substituirString(String original, String alvo, String substituto) {
        if (original == null || alvo == null || substituto == null) {
            return original;
        }
        return original.replace(alvo, substituto);
    }

    public byte[] downloadImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return getDefaultImage(context);
        }

        try {
            return executorService.submit(() -> fetchImage(imageUrl)).get();
        } catch (Exception e) {
            Log.e("SYNC", "Erro ao baixar imagem: " + e.getMessage());
            return getDefaultImage(context);
        }
    }

    private byte[] fetchImage(String imageUrl) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setDoInput(true);
            connection.connect();

            inputStream = connection.getInputStream();
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultImage(context);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (byteArrayOutputStream != null) byteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public byte[] getDefaultImage(Context context) {
        try {
            Drawable drawable = context.getResources().getDrawable(R.drawable.user, null);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Caso não consiga carregar a imagem
        }
    }


    private boolean userExists(String email, String username) {
        return usersDao.userExists(email, username);
    }

    private boolean activityExists(String designacao, String empresa) {
        return activityDao.activityExists(designacao, empresa);
    }
    private boolean trabalhadorExists(String docid, String empresa) {
        return trabalhadoresDao.trabalhadorExists(docid, empresa);
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

    // Remaining helper methods stay the same...

    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            Log.d("SYNC", "DataSyncManager foi desligado corretamente.");
        }
    }
}
