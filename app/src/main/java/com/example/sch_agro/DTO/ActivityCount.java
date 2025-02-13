package com.example.sch_agro.DTO;

public class ActivityCount {
    private String activityName;
    private int totalTrabalhadores;

    public ActivityCount(String activityName, int totalTrabalhadores) {
        this.activityName = activityName;
        this.totalTrabalhadores = totalTrabalhadores;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getTotalTrabalhadores() {
        return totalTrabalhadores;
    }
}

