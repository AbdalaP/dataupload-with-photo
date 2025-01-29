package com.example.sch_agro.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sch_agro.Model.TaskGeba;
import com.example.sch_agro.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskGebaDAO {
    private final DatabaseHelper dbHelper;

    public TaskGebaDAO(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public boolean insertTask(String name, String docid, String telefone, String act, String block, byte[] image, String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("docid", docid);
        values.put("telefone", telefone);
        values.put("act", act);
        values.put("block", block);
        values.put("image", image);
        values.put("user_id", userId);
        return db.insert("taskgeba", null, values) != -1;
    }

    @SuppressLint("Range")
    public List<String> getAllTasks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM taskgeba", null);
        List<String> tasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                tasks.add(cursor.getString(cursor.getColumnIndex("name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    @SuppressLint("Range")
    public List<TaskGeba> getUnsyncedTasks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM taskgeba WHERE isSynced = 0", null); // "isSynced = false" pode não funcionar dependendo do tipo de dado no banco (use 0 para boolean no SQLite).
        List<TaskGeba> tasks = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                TaskGeba task = new TaskGeba();
                task.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow("taskId")));
                task.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                task.setDocid(cursor.getString(cursor.getColumnIndexOrThrow("docid")));
                task.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
                task.setAct(cursor.getString(cursor.getColumnIndexOrThrow("act")));
                task.setBlock(cursor.getString(cursor.getColumnIndexOrThrow("block")));
                task.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow("image")));
                task.setTaskDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("taskDate")))); // Convertendo de timestamp para Date
                task.setUserId(cursor.getString(cursor.getColumnIndexOrThrow("userId")));
                task.setSynced(cursor.getInt(cursor.getColumnIndexOrThrow("isSynced")) == 1);
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tasks;
    }

    public void update(TaskGeba task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Preenchendo os valores que serão atualizados
        values.put("name", task.getName());
        values.put("docid", task.getDocid());
        values.put("telefone", task.getTelefone());
        values.put("act", task.getAct());
        values.put("block", task.getBlock());
        values.put("image", task.getImage()); // Adicionando imagem (caso necessário)
        values.put("user_id", task.getUserId());
        values.put("taskDate", task.getTaskDate().getTime()); // Convertendo Date para timestamp
        values.put("isSynced", task.isSynced() ? 1 : 0); // Convertendo boolean para int

        // Condição de atualização (com base no taskId do task)
        String whereClause = "taskId = ?";
        String[] whereArgs = {String.valueOf(task.getTaskId())};

        // Executando a atualização
        int rowsUpdated = db.update("taskgeba", values, whereClause, whereArgs);

        if (rowsUpdated > 0) {
            System.out.println("TaskGeba atualizada com sucesso.");
        } else {
            System.out.println("Erro ao atualizar a TaskGeba. Verifique se o taskId é válido.");
        }

        db.close(); // Fecha a conexão com o banco de dados
    }

}