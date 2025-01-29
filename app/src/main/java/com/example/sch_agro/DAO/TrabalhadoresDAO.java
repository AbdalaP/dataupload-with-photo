package com.example.sch_agro.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sch_agro.Model.Trabalhadores;
import com.example.sch_agro.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrabalhadoresDAO {
    private final DatabaseHelper dbHelper;

    public TrabalhadoresDAO(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public boolean insertTrabalhador(String empresa, String nome, String docid, String idade, String genero, String telefone, String tipo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("empresa", empresa);
        values.put("nome", nome);
        values.put("docid", docid);
        values.put("idade", idade);
        values.put("genero", genero);
        values.put("telefone", telefone);
        values.put("tipo", tipo);
        return db.insert("trabalhadores", null, values) != -1;
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
                trabalhador.setDocid(cursor.getString(cursor.getColumnIndexOrThrow("docid")));
                trabalhador.setIdade(cursor.getString(cursor.getColumnIndexOrThrow("idade")));
                trabalhador.setGenero(cursor.getString(cursor.getColumnIndexOrThrow("genero")));
                trabalhador.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
                trabalhador.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow("image")));
                trabalhador.setTipo(cursor.getString(cursor.getColumnIndexOrThrow("tipo")));
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
        values.put("empresa", trabalhador.getEmpresa());
        values.put("nome", trabalhador.getNome());
        values.put("docid", trabalhador.getDocid());
        values.put("idade", trabalhador.getIdade());
        values.put("genero", trabalhador.getGenero());
        values.put("telefone", trabalhador.getTelefone());
        values.put("tipo", trabalhador.getTipo());
        values.put("image", trabalhador.getImage()); // Adicionando imagem (caso necessário)
        values.put("registration_date", trabalhador.getRegistrationDate().getTime()); // Convertendo Date para timestamp
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

}
