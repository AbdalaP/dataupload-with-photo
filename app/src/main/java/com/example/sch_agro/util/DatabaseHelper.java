package com.example.sch_agro.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.sch_agro.DTO.ActivityCount;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String databaseName = "SchAgro.db";
    public static final String testimage ="testimage";
    public static final String trabalhadores ="trabalhadores";
    public static final String taskgeba ="taskgeba";
    public static final String controle_actividade ="controle_actividade";
    public static final String users ="users";
    public static final String activity ="activity";

    byte[] imageInBytes;
    private Object Context;
    Context context;
    public DatabaseHelper(@Nullable Context context) {
        super(context, "SchAgro.db", null, 3);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("CREATE TABLE users (userid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "nome TEXT,email TEXT, username TEXT, " +
                "password TEXT,role TEXT," +
                "user_date TEXT DEFAULT CURRENT_TIMESTAMP, isSynced BOOLEAN DEFAULT 0)");

        MyDatabase.execSQL("CREATE TABLE activity (" +
                "activity_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "empresa TEXT NOT NULL, " +
                "activity_name TEXT NOT NULL UNIQUE, " +
                "person TEXT NOT NULL, " +
                "category_act TEXT NOT NULL, " +
                "meta INTEGER, " +
                "valor INTEGER NOT NULL, " +
                "userlog TEXT, " +
                "act_date TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "isSynced BOOLEAN DEFAULT 0)");

        MyDatabase.execSQL("CREATE TABLE trabalhadores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "empresa TEXT NOT NULL, " +
                "nome TEXT NOT NULL, " +
                "docid TEXT NOT NULL UNIQUE, " +
                "data_nascimento TEXT NOT NULL, " +
                "genero TEXT NOT NULL, " +
                "telefone TEXT NOT NULL, " +
                "image BLOB NOT NULL, " +
                "activity_id TEXT NOT NULL, " +
                "status TEXT, " +
                "userlog TEXT, " +
                "registration_date TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "isSynced BOOLEAN DEFAULT 0)");

        MyDatabase.execSQL("CREATE TABLE testimage (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "name TEXT, " +
                "image BLOB, " +
                "email TEXT, " +
                "isSynced BOOLEAN DEFAULT 0)");

        MyDatabase.execSQL("CREATE TABLE taskgeba (" +
                "task_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "docid TEXT NOT NULL, " +
                "telefone TEXT NOT NULL, " +
                "act TEXT NOT NULL, " +
                "block TEXT NOT NULL, " +
                "image BLOB NOT NULL, " +
                "task_date TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "user_id TEXT NOT NULL, " +
                "isSynced BOOLEAN DEFAULT 0)");

        MyDatabase.execSQL("CREATE TABLE controle_actividade (" +
                "ctrID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "activity_id TEXT NOT NULL, " +
                "trabalhador_id TEXT NOT NULL, " +
                "target INTEGER, " +
                "faltas TEXT, " +
                "userlog TEXT, " +
                "task_date TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "isSynced BOOLEAN DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists activity");
        MyDB.execSQL("drop Table if exists trabalhadores");
        MyDB.execSQL("drop Table if exists testimage");
        MyDB.execSQL("drop Table if exists taskgeba");
        MyDB.execSQL("drop Table controle_actividade");

    }


// Select All Data
public List<String> getAllLabels(){
    List<String> list = new ArrayList<String>();

    // Select All Query
   // String selectQuery = "SELECT  * FROM " + activity;
    String selectQuery = ("SELECT * FROM activity where empresa= 'GEBA'");


    //Cursor cursor2 = db.rawQuery("SELECT Weight FROM <MYTABLE> WHERE ID=" + lastID, null);
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
        do {
            list.add(cursor.getString(2));//adding 2nd column data
        } while (cursor.moveToNext());
    }
    // closing connection

    // returning lables
    return list;
}




    public List<String> getAllData(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        // String selectQuery = "SELECT  * FROM " + activity;
        String selectQuery = ("SELECT * FROM activity where empresa= 'SAN'");



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(2));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection

        // returning lables
        return list;
    }



    public Boolean insertData(String nome, String email,String username, String password,String role) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", hashedPassword);
        contentValues.put("role", role);
        long result = MyDatabase.insert("users", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
    public Boolean checkusername(String username) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkEmailPassword1(String username, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where username = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkEmailPassword(String username, String password) {
        SQLiteDatabase MyDatabase = null;
        Cursor cursor = null;

        try {
            MyDatabase = this.getReadableDatabase();
            cursor = MyDatabase.rawQuery("SELECT password FROM users WHERE username = ?", new String[]{username});

            if (cursor.moveToFirst()) {
                String hashedPassword = cursor.getString(0);
                return BCrypt.checkpw(password, hashedPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (MyDatabase != null) {
                MyDatabase.close();
            }
        }

        return false;
    }

    public Boolean insertactivity(String nome, String spinner, String spinner2, String person, String valor, String meta, String userlogged) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("empresa", spinner);
        contentValues.put("category_act", spinner2);
        contentValues.put("activity_name", nome);
        contentValues.put("person", person);
        contentValues.put("meta", meta);
        contentValues.put("valor", valor);
        contentValues.put("userlog", userlogged);
        long result = MyDatabase.insert("activity", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkactivity(String activity_name) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from activity where activity_name = ?", new String[]{activity_name});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public Boolean checkdocid(String docid) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from trabalhadores where docid = ?", new String[]{docid});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor populatespinner() {
        try {
            SQLiteDatabase MyDatabase = this.getWritableDatabase();
            Cursor filas = MyDatabase.rawQuery("SELECT activity_name FROM activity", null);
            if (filas.moveToFirst()){
                return filas;
            }else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }



    public boolean checkStatus() {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select id from trabalhadores where status = 'Inativo'", null);
        if (cursor.getCount() > 0) {
           // return Boolean.valueOf(username);
            return true;
        } else {
            return false;
        }
    }

    public int getCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public List<ActivityCount> getActivityCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ActivityCount> activityList = new ArrayList<>();

        String query = "SELECT a.activity_name, COALESCE(COUNT(t.activity_id), 0) AS total_trabalhadores " +
                "FROM activity AS a " +
                "LEFT JOIN trabalhadores AS t ON t.activity_id = a.activity_name " +
                "GROUP BY a.activity_name";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String activityName = cursor.getString(0);
                int totalTrabalhadores = cursor.getInt(1);
                activityList.add(new ActivityCount(activityName, totalTrabalhadores));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return activityList;
    }

}
