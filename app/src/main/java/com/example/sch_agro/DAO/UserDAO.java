package com.example.sch_agro.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sch_agro.Model.User;
import com.example.sch_agro.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

// DAO para a tabela "users"
public class UserDAO {
    private final DatabaseHelper dbHelper;

    public UserDAO(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public boolean insertUser(String nome, String email, String username, String password, String role) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Verifica se já existe um usuário com o mesmo e-mail ou nome de usuário
        String query = "SELECT COUNT(*) FROM users WHERE email = ? OR username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, username});

        boolean exists = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                exists = cursor.getInt(0) > 0;
            }
            cursor.close();
        }

        // Se não existir, insere o novo usuário
        if (!exists) {
            ContentValues values = new ContentValues();
            values.put("nome", nome);
            values.put("email", email);
            values.put("username", username);
            values.put("password", password);
            values.put("role", role);
            values.put("isSynced", 1);

            return db.insert("users", null, values) != -1;
        }

        return false; // Retorna false se o usuário já existir
    }


    public boolean userExists(String email, String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM users WHERE email = ? OR username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, username});

        boolean exists = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                exists = cursor.getInt(0) > 0;
            }
            cursor.close();
        }
        return exists;
    }

    public boolean checkEmailAndPassword(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean valid = cursor.getCount() > 0;
        cursor.close();
        return valid;
    }

    public List<User> getUnsyncedUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE isSynced = 0", null); // Consulta para obter usuários não sincronizados
        List<User> userList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                User user = new User();

                // Preenchendo os campos da classe User
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("userid")));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                user.setNomeCompleto(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
                user.setUser_date(cursor.getString(cursor.getColumnIndexOrThrow("user_date")));
                user.setSynced(cursor.getInt(cursor.getColumnIndexOrThrow("isSynced")) == 1); // Convertendo de int para boolean

                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close(); // Fechando o cursor para liberar recursos
        db.close();
        return userList;
    }

    public void update(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Adicione os valores a serem atualizados
        contentValues.put("isSynced", user.isSynced() ? 1 : 0);

        // Condição de atualização
        String whereClause = "userid = ?";
        String[] whereArgs = {String.valueOf(user.getId())}; // Supondo que o email é a chave primária

        // Executa a atualização
        int rowsUpdated = db.update("users", contentValues, whereClause, whereArgs);

        if (rowsUpdated > 0) {
            Log.d("DatabaseHelper", "Usuário atualizado com sucesso.");
        } else {
            Log.d("DatabaseHelper", "Erro ao atualizar o usuário.");
        }

        db.close(); // Fecha a conexão com o banco de dados
    }

}
