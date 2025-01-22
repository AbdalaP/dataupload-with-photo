package com.example.sch_agro.util;

import android.database.sqlite.SQLiteDatabase;
import android.view.View;

public class Test {

    static DatabaseHelper databaseHelper;
    static SQLiteDatabase sqLiteDatabase;

    public static void exportDatabase(View.OnClickListener context, String dbName) {
        /*

        sqLiteDatabase=databaseHelper.getWritableDatabase();

       // String currentDBPath = sqLiteDatabase.getPath();
        File dbFile = getDatabasePath("databaseName");
        File exportDir = new File(Environment.getExternalStorageDirectory(), "Downloads");
        File file = new File(exportDir, dbFile.getName());

        try {
            FileUtils.copyFile(dbFile, file);
            // Database exported successfully
        } catch (IOException e) {
            e.printStackTrace();
            // Handle export failure
        }

         */
    }


}
