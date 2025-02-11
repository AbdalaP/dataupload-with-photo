package com.example.sch_agro.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.sch_agro.Model.Trabalhadores;
import com.example.sch_agro.util.DatabaseHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrabalhadoresDAO {
    private final DatabaseHelper dbHelper;

    public TrabalhadoresDAO(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public boolean insertTrabalhador(String empresa, String nome, String docid, String data_nascimento,
                                     String genero, String telefone, String atividade, String userlogged, byte[] imageBytes) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Verifica se o trabalhador já existe com base no docid
        String query = "SELECT COUNT(*) FROM trabalhadores WHERE docid = ?";
        Cursor cursor = db.rawQuery(query, new String[]{docid});

        boolean exists = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                exists = cursor.getInt(0) > 0;
            }
            cursor.close();
        }

        // Se não existir, insere o novo trabalhador
        if (!exists) {
            ContentValues values = new ContentValues();
            values.put("empresa", empresa);
            values.put("nome", nome);
            values.put("docid", docid);
            values.put("data_nascimento", data_nascimento);
            values.put("genero", genero);
            values.put("telefone", telefone);
            values.put("activity_id", atividade);
            values.put("userlog", userlogged);
            values.put("isSynced", 1);
            values.put("image", imageBytes); // Armazena a imagem em formato BLOB

            long result = db.insert("trabalhadores", null, values);
            db.close();
            return result != -1;
        }

        return false; // Retorna false se o trabalhador já existir
    }



    public boolean checkDocId(String docid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM trabalhadores WHERE docid = ?", new String[]{docid});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    @SuppressLint("Range")
    public List<String> getAllTrabalhadores() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM trabalhadores", null);
        List<String> trabalhadores = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                trabalhadores.add(cursor.getString(cursor.getColumnIndex("nome")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return trabalhadores;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Trabalhadores> getUnsyncedTrabalhadores() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM trabalhadores WHERE isSynced = 0", null); // "isSynced = 0" para booleano no SQLite
        List<Trabalhadores> trabalhadoresList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Trabalhadores trabalhador = new Trabalhadores();

                // Preenchendo os campos da classe Trabalhadores
                trabalhador.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                trabalhador.setEmpresa(cursor.getString(cursor.getColumnIndexOrThrow("empresa")));
                trabalhador.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                trabalhador.setDocumentoIdentificacao(cursor.getString(cursor.getColumnIndexOrThrow("docid")));
                trabalhador.setDataNascimento(cursor.getString(cursor.getColumnIndexOrThrow("data_nascimento")));
                trabalhador.setGenero(cursor.getString(cursor.getColumnIndexOrThrow("genero")));
                trabalhador.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
                trabalhador.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow("image")));
                trabalhador.setAtividade(cursor.getString(cursor.getColumnIndexOrThrow("activity_id")));
                trabalhador.setRegistrationDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("registration_date")))); // Convertendo timestamp para Date
                trabalhador.setSynced(cursor.getInt(cursor.getColumnIndexOrThrow("isSynced")) == 1); // Convertendo de int para boolean

                trabalhadoresList.add(trabalhador);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return trabalhadoresList;
    }

    public void update(Trabalhadores trabalhador) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Preenchendo os valores que serão atualizados
        values.put("isSynced", trabalhador.isSynced() ? 1 : 0); // Convertendo boolean para int

        // Condição de atualização (com base no ID do trabalhador)
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(trabalhador.getId())};

        // Executando a atualização
        int rowsUpdated = db.update("trabalhadores", values, whereClause, whereArgs);

        if (rowsUpdated > 0) {
            System.out.println("Trabalhador atualizado com sucesso.");
        } else {
            System.out.println("Erro ao atualizar o trabalhador. Verifique se o ID é válido.");
        }

        db.close(); // Fecha a conexão com o banco de dados
    }

    public boolean trabalhadorExists(String docid, String empresa) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM trabalhadores WHERE docid = ? AND empresa = ?";
        Cursor cursor = db.rawQuery(query, new String[]{docid, empresa});

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
