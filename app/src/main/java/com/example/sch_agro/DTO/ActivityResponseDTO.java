package com.example.sch_agro.DTO;

import java.time.LocalDate;

public class ActivityResponseDTO {
    private Long id;
    private String empresa;
    private String designacao;
    private String responsavel;
    private Double valor;
    private String tipo;
    private String dataRegistro;
    private Integer metaDiaria;

    public ActivityResponseDTO() {
    }

    public ActivityResponseDTO(Long id, String empresa, String designacao, String responsavel, Double valor, String tipo, String dataRegistro, Integer metaDiaria) {
        this.id = id;
        this.empresa = empresa;
        this.designacao = designacao;
        this.responsavel = responsavel;
        this.valor = valor;
        this.tipo = tipo;
        this.dataRegistro = dataRegistro;
        this.metaDiaria = metaDiaria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Integer getMetaDiaria() {
        return metaDiaria;
    }

    public void setMetaDiaria(Integer metaDiaria) {
        this.metaDiaria = metaDiaria;
    }
}
