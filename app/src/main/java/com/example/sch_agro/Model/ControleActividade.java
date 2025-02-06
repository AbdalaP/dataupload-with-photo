package com.example.sch_agro.Model;

import java.util.Date;

public class ControleActividade {
    private int id;
    private String atividadeId;
    private String trabalhadorId;
    private Boolean presenca;
    private Integer quantidadeFeita;
    private Date task_date;
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

    public Date getTask_date() {
        return task_date;
    }

    public void setTask_date(Date task_date) {
        this.task_date = task_date;
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