package com.example.sch_agro.util;

import androidx.annotation.NonNull;

public class CatActividades {

    private int id;
    private String nombre;
    private String item;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = String.valueOf(item);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String toString() {
        return nombre;
    }

}
