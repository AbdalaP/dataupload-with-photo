package com.example.sch_agro.util;

import static com.example.sch_agro.util.DatabaseHelper.taskgeba;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    private Context ctx;
    public ExportDatabaseCSVTask(){
        this.ctx = ctx;
    }
    private final ProgressDialog dialog = new ProgressDialog(ctx);

    @Override
    public void onPreExecute() {
        this.dialog.setMessage("Exporting database...");
        this.dialog.show();
    }

    @Override
    protected Boolean doInBackground(final String... args) {
        sqLiteDatabase=databaseHelper.getWritableDatabase();
       // String currentDBPath = "/data/"+ "/data/"+"com.example.sch_agro" +"/databases/SchAgro.db";

        String currentDBPath = ctx.getDatabasePath(taskgeba).getAbsolutePath();


        File dbFile = ctx.getDatabasePath(currentDBPath);
        System.out.println(dbFile);  // displays the data base path in your logcat
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");

        if (!exportDir.exists()) { exportDir.mkdirs(); }

        File file = new File(exportDir, "myfile.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            Cursor curCSV = sqLiteDatabase.rawQuery("select * from " + taskgeba,null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                String[] arrStr =null;
                String[] mySecondStringArray = new String[curCSV.getColumnNames().length];
                for(int i=0;i<curCSV.getColumnNames().length;i++)
                {
                    mySecondStringArray[i] =curCSV.getString(i);
                }
                csvWrite.writeNext(mySecondStringArray);
            }
            csvWrite.close();
            curCSV.close();
            return true;
        } catch (IOException e) {
            Log.e(String.valueOf(ctx), e.getMessage(), e);
           // Toast.makeText(ctx, "shitttttt", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    protected void onPostExecute(final Boolean success) {
        if (this.dialog.isShowing()) { this.dialog.dismiss(); }
        if (success) {
            Toast.makeText(ctx, "Export successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ctx, "Export failed", Toast.LENGTH_SHORT).show();
        }
    }
}