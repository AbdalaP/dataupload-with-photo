package com.example.sch_agro.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Activity {
    private int id;
    private String empresa;
    private String designacao;
    private String responsavel;
    private Double valor;
    private String tipoValidacao;
    private String registrationDate;

    private String user;
    private boolean isSynced;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDesignacao() {
        return designacao;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getTipoValidacao() {
        return tipoValidacao;
    }

    public void setTipoValidacao(String tipoValidacao) {
        this.tipoValidacao = tipoValidacao;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getRegistrationDate() {
        return LocalDate.parse(registrationDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setRegistrationDate(Date registrationDate) {
        if (registrationDate != null) {
            // Converte java.util.Date para java.time.LocalDate
            this.registrationDate = registrationDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate().toString();
        } else {
            this.registrationDate = null; // Ou defina um valor padrão, se necessário
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
