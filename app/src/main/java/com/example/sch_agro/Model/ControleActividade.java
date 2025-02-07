package com.example.sch_agro.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ControleActividade {
    private int id;
    private String atividadeId;
    private String trabalhadorId;
    private Boolean presenca;
    private Integer quantidadeFeita;
    private String task_date;
    private String user;
    private boolean isSynced;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAtividadeId() {
        return atividadeId;
    }

    public void setAtividadeId(String atividadeId) {
        this.atividadeId = atividadeId;
    }

    public String getTrabalhadorId() {
        return trabalhadorId;
    }

    public void setTrabalhadorId(String trabalhadorId) {
        this.trabalhadorId = trabalhadorId;
    }

    public Boolean getPresenca() {
        return presenca;
    }

    public void setPresenca(int presenca) {
        if(presenca == 1){
            this.presenca = true;
        }else{
            this.presenca = false;
        }

    }

    public Integer getQuantidadeFeita() {
        return quantidadeFeita;
    }

    public void setQuantidadeFeita(Integer quantidadeFeita) {
        this.quantidadeFeita = quantidadeFeita;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getTask_date() {
        return LocalDate.parse(task_date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setTask_date(Date task_date) {
//        this.task_date = task_date;
        if (task_date != null) {
            // Converte java.util.Date para java.time.LocalDate
            this.task_date = task_date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate().toString();
        } else {
            this.task_date = null; // Ou defina um valor padrão, se necessário
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}