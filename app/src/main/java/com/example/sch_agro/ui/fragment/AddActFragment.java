package com.example.sch_agro.ui.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sch_agro.R;
import com.example.sch_agro.databinding.FragmentAddActBinding;
import com.example.sch_agro.ui.activity.MainActivity;
import com.example.sch_agro.util.DatabaseHelper;


public class AddActFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    FragmentAddActBinding binding;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    Spinner spinner1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding= FragmentAddActBinding.inflate(inflater, container, false);
        databaseHelper = new DatabaseHelper(super.getContext());

        binding.adicionarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String spinner= spinner1.getItemAtPosition(spinner1.getSelectedItemPosition()).toString();
                String nome = binding.name.getText().toString();
                String pessoa = binding.pessoaResponsavel.getText().toString();
                String target = binding.target.getText().toString();
                if (nome.equals("") || pessoa.equals("")||target.equals("")) {
                    Toast.makeText(AddActFragment.super.getContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }else if(spinner1.getItemAtPosition(spinner1.getSelectedItemPosition()).toString().equals("[Escolhe Empresa…]")){
                    Toast.makeText(AddActFragment.super.getContext(), "Por favor selectione a empresa!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean checkactivity = databaseHelper.checkactivity(nome);
                    if (checkactivity == false) {
                        Boolean insert = databaseHelper.insertactivity(nome, spinner,pessoa,target);
                        if (insert == true) {
                            Toast.makeText(AddActFragment.super.getContext(), "Dados inseridos com Sucesso!", Toast.LENGTH_SHORT).show();
                            spinner1.setSelection(0);
                            pessoa.equals("");
                            nome.equals("");
                            target.equals("");
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(AddActFragment.super.getContext(), "Erro ao Inserir!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddActFragment.super.getContext(), "User already exists! Please login", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner1 = view.findViewById(R.id.spinner1);
        loadSpinnerData();
    }

    private void loadSpinnerData() {
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(requireActivity(), R.array.Empresa, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}