package com.example.sch_agro.DTO;

import java.time.LocalDate;

public class TrabalhadorResponseDTO {
    private Long id;
    private String nome;
    private String documentoIdentificacao;
    private String dataNascimento;
    private Integer idade;
    private String genero;
    private String telefone;
    private String imagem;
    private String dataRegistro;
    private String empresaNome;
    private String atividade;

    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TrabalhadorResponseDTO() {
    }

    public TrabalhadorResponseDTO(Long id, String nome, String documentoIdentificacao, String dataNascimento, Integer idade, String genero, String telefone, String imagem, String dataRegistro, String empresaNome, String atividade) {
        this.id = id;
        this.nome = nome;
        this.documentoIdentificacao = documentoIdentificacao;
        this.dataNascimento = dataNascimento;
        this.idade = idade;
        this.genero = genero;
        this.telefone = telefone;
        this.imagem = imagem;
        this.dataRegistro = dataRegistro;
        this.empresaNome = empresaNome;
        this.atividade = atividade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
