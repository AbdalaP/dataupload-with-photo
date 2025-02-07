package com.example.sch_agro.util;

import androidx.annotation.NonNull;

public class CatActividades {

    private String nombre;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String activity_name) {
        this.nombre = activity_name;
    }

    @NonNull
    public String toString() {
        return nombre;
    }
}
