package com.example.sch_agro.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class Trabalhadores {
    private int id;
    private String empresa;
    private String nome;
    private String documentoIdentificacao;
    private String dataNascimento;
    private String genero;
    private String telefone;
    private byte[] image;
    private String atividade;
    private String registrationDate;

    private String estado;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumentoIdentificacao() {
        return documentoIdentificacao;
    }

    public void setDocumentoIdentificacao(String documentoIdentificacao) {
        this.documentoIdentificacao = documentoIdentificacao;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDataNascimento(String dataNascimento) {
        if (dataNascimento.matches("\\d{4}-\\d{2}-\\d{2}")) {
            // Se já estiver no formato yyyy-MM-dd, não há necessidade de conversão
            this.dataNascimento = dataNascimento;
        } else {
            try {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d-M-yyyy");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDate data = LocalDate.parse(dataNascimento, inputFormatter);
                this.dataNascimento = data.format(outputFormatter);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Formato de data inválido: " + dataNascimento);
            }
        }
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        if(genero == "male"){
            this.genero = "MASCULINO";
        }
        else {
            this.genero = "FEMININO";
        }
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
