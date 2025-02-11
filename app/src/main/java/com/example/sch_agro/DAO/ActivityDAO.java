package com.example.sch_agro.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

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

    public boolean insertActivity(String empresa, String activityName, String person, String target, String categoria, String userlogged) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Verifica se já existe um registro com os mesmos dados
        String query = "SELECT COUNT(*) FROM activity WHERE empresa = ? AND category_act = ? AND activity_name = ? AND person = ? AND valor = ? AND userlog = ?";
        Cursor cursor = db.rawQuery(query, new String[]{empresa, categoria, activityName, person, target, userlogged});

        boolean exists = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                exists = cursor.getInt(0) > 0;
            }
            cursor.close();
        }

        // Se não existir, insere a nova atividade
        if (!exists) {
            ContentValues values = new ContentValues();
            values.put("empresa", empresa);
            values.put("category_act", categoria);
            values.put("activity_name", activityName);
            values.put("person", person);
            values.put("valor", target);
            values.put("userlog", userlogged);
            values.put("isSynced", 1);

            return db.insert("activity", null, values) != -1;
        }

        return false; // Retorna false se a atividade já existir
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        values.put("isSynced", activity.isSynced() ? 1 : 0); // Convertendo boolean para int

        // Condição de atualização (com base no ID da atividade)
        String whereClause = "activity_id = ?";
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

    public boolean activityExists(String designacao, String empresa) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM activity WHERE activity_name = ? AND empresa = ?";
        Cursor cursor = db.rawQuery(query, new String[]{designacao, empresa});

        boolean exists = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                exists = cursor.getInt(0) > 0;
            }
            cursor.close();
        }
        db.close();
        return exists;
    }

}