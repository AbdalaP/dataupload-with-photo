package com.example.sch_agro.Services;

import com.example.sch_agro.DTO.ActivityResponseDTO;
import com.example.sch_agro.DTO.AtividadeDTO;
import com.example.sch_agro.DTO.IntervaloDTO;
import com.example.sch_agro.DTO.LoginDTO;
import com.example.sch_agro.DTO.LoginResponseDTO;
import com.example.sch_agro.DTO.TrabalhadorResponseDTO;
import com.example.sch_agro.DTO.UserDTO;
import com.example.sch_agro.Model.Activity;
import com.example.sch_agro.Model.TaskGeba;
import com.example.sch_agro.Model.ControleActividade;
import com.example.sch_agro.Model.Trabalhadores;
import com.example.sch_agro.Model.User;
import com.example.sch_agro.util.ApiResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

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


    // Endpoint para atualizar dados de trabalhadores
    @Multipart
    @PUT("trabalhadores/{id}")
    Call<Void> updateTrabalhador(
            @Path("id") Long id,
            @Part("data") RequestBody data,
            @Part MultipartBody.Part image
    );

    // Endpoint para enviar dados de tarefas (TaskSan)
    @POST("controle-atividades/sync")
    Call<Void> sendControleAtividade(@Body ControleActividade controleActividade);

    @POST("atividades")
    Call<Void> creatActivity(@Body AtividadeDTO activity);

    @GET("atividades/list")
    Call<List<ActivityResponseDTO>> getAllActivitys();

    @GET("trabalhadores/list")
    Call<List<TrabalhadorResponseDTO>> getAllTrabalhadores();

    @GET("user/list")
    Call<List<UserDTO>> getAllUsers();

    @POST("relatorio/atividades/pdf")
    Call<ResponseBody> gerarRelatorioAtividadesPdf(@Body IntervaloDTO intervaloDTO);
    @POST("relatorio/atividades/motoristas/pdf")
    Call<ResponseBody> gerarRelatorioMotoristasPdf(@Body IntervaloDTO intervaloDTO);
    @POST("relatorio/atividades/excel")
    Call<ResponseBody> gerarRelatorioAtividadesExcel(@Body IntervaloDTO intervaloDTO);
    @POST("relatorio/atividades/motoristas/excel")
    Call<ResponseBody> gerarRelatorioMotoristasExcel(@Body IntervaloDTO intervaloDTO);
}


