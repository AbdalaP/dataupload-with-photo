package com.example.sch_agro.util;

public class Model{

    private String id;
    private byte[] proavatar;
    private String username;
    private String docid;
    private String telefone;



    //constructor
    public Model(String id,String username,byte[]proavatar,String docid,String telefone){
        this.id=id;
        this.username =username;
        this.telefone =telefone;
        this.proavatar =proavatar;
        this.docid =docid;


    }

    //getter and setter method


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getDocid() {
        return docid;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public byte[] getProavatar() {
        return proavatar;
    }

    public void setProavatar(byte[] proavatar) {
        this.proavatar = proavatar;
    }
}
