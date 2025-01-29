package com.example.sch_agro.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sch_agro.Model.TaskSan;
import com.example.sch_agro.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskSanDAO {
    private final DatabaseHelper dbHelper;

    public TaskSanDAO(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public boolean insertTask(String name, String act, String feita, int valorDia, byte[] image, String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("act", act);
        values.put("feita", feita);
        values.put("valorDia", valorDia);
        values.put("image", image);
        values.put("user_id", userId);
        return db.insert("tasksan", null, values) != -1;
    }

    @SuppressLint("Range")
    public List<String> getAllTasks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tasksan", null);
        List<String> tasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                tasks.add(cursor.getString(cursor.getColumnIndex("name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    public List<TaskSan> getUnsyncedTasks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tasksan WHERE isSynced = 0", null); // "isSynced = 0" para booleano no SQLite
        List<TaskSan> tasks = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                TaskSan task = new TaskSan();

                // Preenchendo os campos da classe TaskSan
                task.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow("taskId")));
                task.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                task.setAct(cursor.getString(cursor.getColumnIndexOrThrow("act")));
                task.setFeita(cursor.getString(cursor.getColumnIndexOrThrow("feita")));
                task.setValorDia(cursor.getInt(cursor.getColumnIndexOrThrow("valorDia")));
                task.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow("image")));
                task.setTaskDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("taskDate")))); // Convertendo timestamp para Date
                task.setUserId(cursor.getString(cursor.getColumnIndexOrThrow("userId")));
                task.setSynced(cursor.getInt(cursor.getColumnIndexOrThrow("isSynced")) == 1); // Convertendo de inteiro para booleano

                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tasks;
    }

    public void update(TaskSan task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Preenchendo os valores que serão atualizados
        values.put("name", task.getName());
        values.put("act", task.getAct());
        values.put("feita", task.getFeita());
        values.put("valorDia", task.getValorDia());
        values.put("image", task.getImage()); // Adicionando imagem (caso necessário)
        values.put("user_id", task.getUserId());
        values.put("taskDate", task.getTaskDate().getTime()); // Convertendo Date para timestamp
        values.put("isSynced", task.isSynced() ? 1 : 0); // Convertendo boolean para int

        // Condição de atualização (com base no taskId do task)
        String whereClause = "taskId = ?";
        String[] whereArgs = {String.valueOf(task.getTaskId())};

        // Executando a atualização
        int rowsUpdated = db.update("tasksan", values, whereClause, whereArgs);

        if (rowsUpdated > 0) {
            System.out.println("TaskSan atualizada com sucesso.");
        } else {
            System.out.println("Erro ao atualizar a TaskSan. Verifique se o taskId é válido.");
        }

        db.close(); // Fecha a conexão com o banco de dados
    }

}
