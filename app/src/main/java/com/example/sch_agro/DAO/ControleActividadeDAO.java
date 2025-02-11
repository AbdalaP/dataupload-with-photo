package com.example.sch_agro.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.sch_agro.Model.ControleActividade;
import com.example.sch_agro.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControleActividadeDAO {
    private final DatabaseHelper dbHelper;

    public ControleActividadeDAO(DatabaseHelper dbHelper) {
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
        return db.insert("controle_actividade", null, values) != -1;
    }

    @SuppressLint("Range")
    public List<String> getAllTasks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM controle_actividade", null);
        List<String> tasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                tasks.add(cursor.getString(cursor.getColumnIndex("name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<com.example.sch_agro.Model.ControleActividade> getUnsyncedTasks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM controle_actividade WHERE isSynced = 0", null); // "isSynced = 0" para booleano no SQLite
        List<com.example.sch_agro.Model.ControleActividade> tasks = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                ControleActividade task = new ControleActividade();

                // Preenchendo os campos da classe TaskSan
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ctrID")));
                task.setAtividadeId(cursor.getString(cursor.getColumnIndexOrThrow("activity_id")));
                task.setTrabalhadorId(getTrabalhadorNomeById(db, cursor.getString(cursor.getColumnIndexOrThrow("trabalhador_id"))));
                task.setQuantidadeFeita(cursor.getInt(cursor.getColumnIndexOrThrow("target")));
                task.setPresenca(cursor.getInt(cursor.getColumnIndexOrThrow("faltas")));
                task.setUser(cursor.getString(cursor.getColumnIndexOrThrow("userlog")));
                task.setTask_date(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("task_date")))); // Convertendo timestamp para Date
                task.setSynced(cursor.getInt(cursor.getColumnIndexOrThrow("isSynced")) == 1); // Convertendo de inteiro para booleano

                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tasks;
    }

    private String getTrabalhadorNomeById(SQLiteDatabase db, String trabalhadorId) {
        // Consulta para buscar o nome do trabalhador com base no id
        Cursor cursor = db.rawQuery("SELECT nome FROM trabalhadores WHERE id = ?", new String[]{trabalhadorId});
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndexOrThrow("nome"));
        }
        cursor.close();
        return ""; // Retorna "Desconhecido" se não encontrar o trabalhador
    }

    public void update(ControleActividade task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Preenchendo os valores que serão atualizados
        values.put("isSynced", task.isSynced() ? 1 : 0); // Convertendo boolean para int

        // Condição de atualização (com base no taskId do task)
        String whereClause = "ctrID = ?";
        String[] whereArgs = {String.valueOf(task.getId())};

        // Executando a atualização
        int rowsUpdated = db.update("controle_actividade", values, whereClause, whereArgs);

        if (rowsUpdated > 0) {
            System.out.println("TaskSan atualizada com sucesso.");
        } else {
            System.out.println("Erro ao atualizar a TaskSan. Verifique se o taskId é válido.");
        }

        db.close(); // Fecha a conexão com o banco de dados
    }

}
