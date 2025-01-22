package com.example.sch_agro.util;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String databaseName = "SchAgro.db";
    public static final String testimage ="testimage";
    public static final String trabalhadores ="trabalhadores";
    public static final String taskgeba ="taskgeba";
    public static final String tasksan ="tasksan";
    public static final String users ="users";
    public static final String activity ="activity";

    byte[] imageInBytes;
    private Object Context;
    Context context;
    public DatabaseHelper(@Nullable Context context) {
        super(context, "SchAgro.db", null, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("create Table users(email TEXT primary key, password TEXT)");

        MyDatabase.execSQL("CREATE TABLE activity (id INTEGER PRIMARY KEY autoincrement NOT NULL, empresa TEXT NOT NULL, activity_name TEXT NOT NULL UNIQUE,person TEXT NOT NULL,target TEXT NOT NULL, " +
                "registration_date TEXT DEFAULT CURRENT_TIMESTAMP)");

        MyDatabase.execSQL("CREATE TABLE trabalhadores ("+
                "id INTEGER PRIMARY KEY autoincrement NOT NULL,"+
                "empresa TEXT NOT NULL,"+
                "nome TEXT NOT NULL,"+
                "docid TEXT NOT NULL UNIQUE,"+
                "idade TEXT NOT NULL,"+
                "genero TEXT NOT NULL,"+
                "telefone TEXT,"+
                "image BLOG,"+
                "tipo TEXT NOT NULL,"+
                "registration_date TEXT DEFAULT CURRENT_TIMESTAMP)");

        MyDatabase.execSQL("create Table testimage(id INTEGER PRIMARY KEY autoincrement NOT NULL, name Text, image BLOG, email Text)");

        try {
    MyDatabase.execSQL("create Table taskgeba(task_id INTEGER PRIMARY KEY autoincrement NOT NULL, " +
            "name Text NOT NULL, docid Text NOT NULL,telefone Text NOT NULL," +
            "act Text NOT NULL,block Text NOT NULL,image BLOG NOT NULL,task_date TEXT DEFAULT CURRENT_TIMESTAMP,user_id Text NOT NULL)");

            MyDatabase.execSQL("create Table tasksan(task_id INTEGER PRIMARY KEY autoincrement NOT NULL, " +
                    "name Text NOT NULL, act Text NOT NULL," +
                    "feita Text NOT NULL,valorDia INT DEFAULT 193,image BLOG NOT NULL,task_date TEXT DEFAULT CURRENT_TIMESTAMP,user_id Text NOT NULL)");
}catch (Exception error){
    Log.e(TAG, "The exception caught while executing the process. (error1)");
    error.printStackTrace();
}
    }
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists activity");
        MyDB.execSQL("drop Table if exists trabalhadores");
        MyDB.execSQL("drop Table if exists testimage");
        MyDB.execSQL("drop Table taskgeba");
        MyDB.execSQL("drop Table tasksan");

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

/*
public List<String> getAllLabels(){
    List<String> list = new ArrayList<String>();

    // Select All Query
    String selectQuery = "SELECT  * FROM " + activity;

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
 */

    public Boolean insertData(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
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

    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public Boolean insertactivity(String nome, String spinner,String person,String target) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("empresa", spinner);
        contentValues.put("activity_name", nome);
        contentValues.put("person", person);
        contentValues.put("target", target);
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

    public Boolean insertregistration(String nome, String docid, String idade, String telefone, String genderid) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);
        contentValues.put("docid", docid);
        contentValues.put("idade", idade);
        contentValues.put("telefone", telefone);
       contentValues.put("genero", genderid);

        long result = MyDatabase.insert("trabalhadores", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;

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


}
