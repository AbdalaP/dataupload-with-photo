package com.example.sch_agro.util;

public class Model{


    private byte[] proavatar;
    private String nome;
    private String activity_id,trabalhador_id;
    private String telefone;

    //constructor
    public Model(String trabalhador_id,String nome,byte[]proavatar,String activity_id){
        this.trabalhador_id=trabalhador_id;
        this.nome =nome;
        this.proavatar =proavatar;
        this.activity_id =activity_id;
    }

    //getter and setter method


    public String getTrabalhador_id() {
        return trabalhador_id;
    }

    public void setTrabalhador_id(String trabalhador_id) {
        this.trabalhador_id = trabalhador_id;
    }

    public String getnome() {
        return nome;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getActivity_id() {
        return activity_id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public byte[] getProavatar() {
        return proavatar;
    }

    public void setProavatar(byte[] proavatar) {
        this.proavatar = proavatar;
    }


}
