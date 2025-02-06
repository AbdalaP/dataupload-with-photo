package com.example.sch_agro.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sch_agro.Model.Activity;
import com.example.sch_agro.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityDAO {
    private final DatabaseHelper dbHelper;

    public ActivityDAO(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public boolean insertActivity(String empresa, String activityName, String person, String target) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("empresa", empresa);
        values.put("activity_name", activityName);
        values.put("person", person);
        values.put("target", target);
        return db.insert("activity", null, values) != -1;
    }

    public boolean checkActivity(String activityName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM activity WHERE activity_name = ?", new String[]{activityName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    @SuppressLint("Range")
    public List<String> getActivitiesByEmpresa(String empresa) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM activity WHERE empresa = ?", new String[]{empresa});
        List<String> activities = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                activities.add(cursor.getString(cursor.getColumnIndex("activity_name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return activities;
    }

    public List<Activity> getUnsyncedActivities() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM activity WHERE isSynced = 0", null); // "isSynced = 0" para registros não sincronizados
        List<Activity> activityList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Activity activity = new Activity();

                // Preenchendo os campos da classe Activity
                activity.setId(cursor.getInt(cursor.getColumnIndexOrThrow("activity_id")));
                activity.setEmpresa(cursor.getString(cursor.getColumnIndexOrThrow("empresa")));
                activity.setDesignacao(cursor.getString(cursor.getColumnIndexOrThrow("activity_name")));
                activity.setResponsavel(cursor.getString(cursor.getColumnIndexOrThrow("person")));
                activity.setTipoValidacao(cursor.getString(cursor.getColumnIndexOrThrow("category_act")));
                activity.setUser(cursor.getString(cursor.getColumnIndexOrThrow("userlog")));
                activity.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow("valor")));
                activity.setRegistrationDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("act_date")))); // Convertendo timestamp para Date
                activity.setSynced(cursor.getInt(cursor.getColumnIndexOrThrow("isSynced")) == 1); // Convertendo de int para boolean

                activityList.add(activity);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return activityList;
    }

    public void update(Activity activity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Preenchendo os valores que serão atualizados
        values.put("empresa", activity.getEmpresa());
        values.put("activity_name", activity.getDesignacao());
        values.put("person", activity.getResponsavel());
        values.put("category_act", activity.getTipoValidacao());
        values.put("valor", activity.getValor());
        values.put("userlog", activity.getUser());
        values.put("act_date", activity.getRegistrationDate().getTime()); // Convertendo Date para timestamp
        values.put("isSynced", activity.isSynced() ? 1 : 0); // Convertendo boolean para int

        // Condição de atualização (com base no ID da atividade)
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(activity.getId())};

        // Executando a atualização
        int rowsUpdated = db.update("activity", values, whereClause, whereArgs);

        if (rowsUpdated > 0) {
            System.out.println("Atividade atualizada com sucesso.");
        } else {
            System.out.println("Erro ao atualizar a atividade. Verifique se o ID é válido.");
        }

        db.close(); // Fecha a conexão com o banco de dados
    }

}