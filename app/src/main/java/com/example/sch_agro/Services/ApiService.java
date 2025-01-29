package com.example.sch_agro.Services;

import com.example.sch_agro.DTO.AtividadeDTO;
import com.example.sch_agro.DTO.LoginDTO;
import com.example.sch_agro.DTO.LoginResponseDTO;
import com.example.sch_agro.Model.Activity;
import com.example.sch_agro.Model.TaskGeba;
import com.example.sch_agro.Model.TaskSan;
import com.example.sch_agro.Model.Trabalhadores;
import com.example.sch_agro.Model.User;
import com.example.sch_agro.util.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    // Endpoint para enviar dados de usuários
    @POST("auth/login")
    Call<ApiResponse<LoginResponseDTO>> login(@Body LoginDTO user);

    // Endpoint para enviar dados de usuários
    @POST("users/sync")
    Call<Void> sendUser(@Body User user);

    // Endpoint para enviar dados de atividades
    @POST("activities/sync")
    Call<Void> sendActivity(@Body Activity activity);

    // Endpoint para enviar dados de trabalhadores
    @POST("trabalhadores/sync")
    Call<Void> sendTrabalhador(@Body Trabalhadores trabalhador);

    // Endpoint para enviar dados de tarefas (TaskGeba)
    @POST("tasks/geba/sync")
    Call<Void> sendTaskGeba(@Body TaskGeba taskGeba);

    // Endpoint para enviar dados de tarefas (TaskSan)
    @POST("tasks/san/sync")
    Call<Void> sendTaskSan(@Body TaskSan taskSan);

    @POST("atividades")
    Call<Void> creatActivity(@Body AtividadeDTO activity);
}


