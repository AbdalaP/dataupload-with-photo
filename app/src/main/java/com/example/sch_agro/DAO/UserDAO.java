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

    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", password);
        return db.insert("users", null, values) != -1;
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
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
        Cursor cursor = db.rawQuery("SELECT * FROM users", null); // Consulta para obter usuários não sincronizados
        List<User> userList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                User user = new User();

                // Preenchendo os campos da classe User
//                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow("password")));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
//                user.setSynced(cursor.getInt(cursor.getColumnIndexOrThrow("isSynced")) == 1); // Convertendo de int para boolean

                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close(); // Fechando o cursor para liberar recursos
        return userList;
    }

    public void update(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Adicione os valores a serem atualizados
        contentValues.put("email", user.getEmail());
//        contentValues.put("password", user.getPassword());

        // Condição de atualização
        String whereClause = "email = ?";
        String[] whereArgs = {user.getEmail()}; // Supondo que o email é a chave primária

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
