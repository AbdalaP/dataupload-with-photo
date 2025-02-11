package com.example.sch_agro.DTO;

import java.time.LocalDate;

public class IntervaloDTO {
    private String dataInicio;
    private String dataFim;

    public IntervaloDTO(String dataInicio, String dataFim) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

}
