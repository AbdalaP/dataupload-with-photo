package com.example.sch_agro.Configuration;

import android.content.Context;

import com.example.sch_agro.DAO.ActivityDAO;
import com.example.sch_agro.DAO.TaskGebaDAO;
import com.example.sch_agro.DAO.ControleActividadeDAO;
import com.example.sch_agro.DAO.TrabalhadoresDAO;
import com.example.sch_agro.DAO.UserDAO;
import com.example.sch_agro.util.DatabaseHelper;

public class DatabaseInstance {
    private static DatabaseInstance instance;
    private final DatabaseHelper databaseHelper;

    private DatabaseInstance(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public static synchronized DatabaseInstance getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseInstance(context);
        }
        return instance;
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    // Métodos para acessar as tabelas
    public UserDAO userDao() {
        return new UserDAO(databaseHelper);
    }

    public ActivityDAO activityDao() {
        return new ActivityDAO(databaseHelper);
    }

    public TrabalhadoresDAO trabalhadorDao() {
        return new TrabalhadoresDAO(databaseHelper);
    }

    public TaskGebaDAO taskGebaDao() {
        return new TaskGebaDAO(databaseHelper);
    }

    public ControleActividadeDAO taskSanDao() {
        return new ControleActividadeDAO(databaseHelper);
    }
}

