package com.example.sch_agro.DTO;

public class AtividadeDTO {
    private String empresa;
    private String designacao;
    private String responsavel;
    private Integer objetivo;

    public AtividadeDTO() {
    }

    public AtividadeDTO(String empresa, String designacao, String responsavel, Integer objetivo) {
        this.empresa = empresa;
        this.designacao = designacao;
        this.responsavel = responsavel;
        this.objetivo = objetivo;
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

    public Integer getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Integer objetivo) {
        this.objetivo = objetivo;
    }
}
