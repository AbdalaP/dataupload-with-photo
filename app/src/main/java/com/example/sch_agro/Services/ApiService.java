package com.example.sch_agro.Services;

import com.example.sch_agro.DTO.AtividadeDTO;
import com.example.sch_agro.DTO.LoginDTO;
import com.example.sch_agro.DTO.LoginResponseDTO;
import com.example.sch_agro.Model.Activity;
import com.example.sch_agro.Model.TaskGeba;
import com.example.sch_agro.Model.ControleActividade;
import com.example.sch_agro.Model.Trabalhadores;
import com.example.sch_agro.Model.User;
import com.example.sch_agro.util.ApiResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    // Endpoint para enviar dados de usuários
    @POST("auth/login")
    Call<ApiResponse<LoginResponseDTO>> login(@Body LoginDTO user);

    // Endpoint para enviar dados de usuários
    @POST("user/sync")
    Call<Void> sendUser(@Body User user);

    // Endpoint para enviar dados de atividades
    @POST("atividades/sync")
    Call<Void> sendActivity(@Body Activity activity);

    // Endpoint para enviar dados de trabalhadores
    @Multipart
    @POST("trabalhadores/sync")
    Call<Void> sendTrabalhador(
            @Part("data") RequestBody data,
            @Part MultipartBody.Part image
    );

    // Endpoint para enviar dados de tarefas (TaskGeba)
    @POST("tasks/geba/sync")
    Call<Void> sendTaskGeba(@Body TaskGeba taskGeba);

    // Endpoint para enviar dados de tarefas (TaskSan)
    @POST("controle-atividades/sync")
    Call<Void> sendControleAtividade(@Body ControleActividade controleActividade);

    @POST("atividades")
    Call<Void> creatActivity(@Body AtividadeDTO activity);
}


