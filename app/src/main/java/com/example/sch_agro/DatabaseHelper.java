package com.example.sch_agro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import androidx.annotation.Nullable;

import static com.example.sch_agro.Registo.ImageViewToBy;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String databaseName = "SchAgro.db";

    byte[] imageInBytes;
    private Object Context;
    Context context;
    public DatabaseHelper(@Nullable Context context) {
        super(context, "SchAgro.db", null, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("create Table users(email TEXT primary key, password TEXT)");

        MyDatabase.execSQL("CREATE TABLE activity (id INTEGER PRIMARY KEY autoincrement NOT NULL, activity_name TEXT NOT NULL UNIQUE,person TEXT NOT NULL, " +
                "registration_date TEXT DEFAULT CURRENT_TIMESTAMP)");

        MyDatabase.execSQL("CREATE TABLE trabalhadores ("+
                "id INTEGER PRIMARY KEY autoincrement NOT NULL,"+
                "nome TEXT NOT NULL,"+
                "docid TEXT NOT NULL UNIQUE,"+
                "idade TEXT NOT NULL,"+
                "genero TEXT NOT NULL,"+
                "telefone TEXT,"+
                "photo TEXT,"+
                "registration_date TEXT DEFAULT CURRENT_TIMESTAMP)");
        MyDatabase.execSQL("create Table testimage(id INTEGER PRIMARY KEY autoincrement NOT NULL, name Text, image BLOG)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists activity");
        MyDB.execSQL("drop Table if exists trabalhadores");
        MyDB.execSQL("drop Table if exists testimage");

    }

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


    public Boolean insertactivity(String nome,String person) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("activity_name", nome);
        contentValues.put("person", person);
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


   // RadioGroup radioGroup;

    //int genderID = radioGroup.getCheckedRadioButtonId();


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



    //test for inserting image
    public Boolean insertimage(String name, byte[] image) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("image",image);
        long result = MyDatabase.insert("testimage", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean save(byte[] pp){
try {
ContentValues cv = new ContentValues();

cv.put("image",pp);
SQLiteDatabase MyDatabase = this.getWritableDatabase();
MyDatabase.insert("testimage", null, cv);
   return  true;
}catch (Exception e){
e.printStackTrace();
return  false;
}

    }






}
