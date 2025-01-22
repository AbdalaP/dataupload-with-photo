package com.example.sch_agro.ui.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sch_agro.databinding.FragmentRelatoriosBinding;
import com.example.sch_agro.util.DatabaseHelper;

import java.io.File;
import java.io.FileWriter;

import au.com.bytecode.opencsv.CSVWriter;

public class RelatoriosFragment extends Fragment {
    FragmentRelatoriosBinding binding;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
/*
    public RelatoriosFragment(){

        //require empty public constract
    }

 */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding= FragmentRelatoriosBinding.inflate(inflater, container, false);

        binding.btnActExport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                exportDB();
            }
        });
        binding.btntrabExport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                exportTB();
            }
        });
        binding.btnActivityExport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                exportAct();

            }
        });
        binding.btnabout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(RelatoriosFragment.super.getContext(), "Relatorio ainda nao disponivel!", Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }

    private void exportDB() {

       // sqLiteDatabase=databaseHelper.getWritableDatabase();
        DatabaseHelper dbhelper = new DatabaseHelper(getContext());

        //File exportDir = Environment.getExternalStorageDirectory();
        File exportDir =  new File(Environment.getExternalStorageDirectory(), "Download");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "Ficheiro de Horas.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM task",null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1),curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),curCSV.getColumnName(5),curCSV.getString(6),curCSV.getString(7)};
                csvWrite.writeNext(arrStr);
                Toast.makeText(RelatoriosFragment.super.getContext(), "Ficheiro das Horas baixado com Sucesso", Toast.LENGTH_SHORT).show();
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("RelatoriosFragment", sqlEx.getMessage(), sqlEx);
        }

    }

    private void exportTB() {

        // sqLiteDatabase=databaseHelper.getWritableDatabase();
        DatabaseHelper dbhelper = new DatabaseHelper(getContext());

        //File exportDir = Environment.getExternalStorageDirectory();
        File exportDir =  new File(Environment.getExternalStorageDirectory(), "Download");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "Ficheiro de Trabalhadores.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM trabalhadores",null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1),curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),curCSV.getString(5),curCSV.getColumnName(6),curCSV.getString(7)};
                csvWrite.writeNext(arrStr);
                Toast.makeText(RelatoriosFragment.super.getContext(), "Ficheiro de Trabalhadores baixado com Sucesso", Toast.LENGTH_SHORT).show();
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("RelatoriosFragment", sqlEx.getMessage(), sqlEx);
        }

    }


    private void exportAct() {

        // sqLiteDatabase=databaseHelper.getWritableDatabase();
        DatabaseHelper dbhelper = new DatabaseHelper(getContext());

        //File exportDir = Environment.getExternalStorageDirectory();
        File exportDir =  new File(Environment.getExternalStorageDirectory(), "Download");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "Actividades.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM activity",null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1),curCSV.getString(2),curCSV.getString(3)};
                csvWrite.writeNext(arrStr);
                Toast.makeText(RelatoriosFragment.super.getContext(), "Ficheiro de Actividades baixado com Sucesso", Toast.LENGTH_SHORT).show();
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("Registo", sqlEx.getMessage(), sqlEx);
        }

    }





}