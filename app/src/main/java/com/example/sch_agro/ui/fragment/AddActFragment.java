package com.example.sch_agro.ui.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sch_agro.R;
import com.example.sch_agro.Services.ApiService;
import com.example.sch_agro.databinding.FragmentAddActBinding;
import com.example.sch_agro.ui.activity.MainActivity;
import com.example.sch_agro.ui.activity.Session;
import com.example.sch_agro.util.DatabaseHelper;


public class AddActFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    FragmentAddActBinding binding;
    EditText meta;
    ApiService apiService;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    Spinner spinner1,spinner_tipo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Session session = new Session(AddActFragment.super.requireActivity());

        binding= FragmentAddActBinding.inflate(inflater, container, false);
        databaseHelper = new DatabaseHelper(super.getContext());

      //  apiService = ApiClient.getClient().create(ApiService.class);
        binding.adicionarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String thisUser = session.getKeyUserId();
                String spinner = spinner1.getItemAtPosition(spinner1.getSelectedItemPosition()).toString();
                String spinner2 = spinner_tipo.getItemAtPosition(spinner_tipo.getSelectedItemPosition()).toString();
                String nome = binding.name.getText().toString();
                String pessoa = binding.pessoaResponsavel.getText().toString();
                String meta = binding.meta.getText().toString();
                String valor = binding.valor.getText().toString();
                  if (spinner_tipo.getItemAtPosition(spinner_tipo.getSelectedItemPosition()).toString().equals("PRESENÇA")) {
                    View c = getView().findViewById(R.id.meta);
                    c.setVisibility(View.GONE);
                }
                //System.out.println(spinner);
                if (nome.equals("") || pessoa.equals("") || valor.equals("")) {
                    Toast.makeText(AddActFragment.super.getContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else if (spinner1.getItemAtPosition(spinner1.getSelectedItemPosition()).toString().equals("[Escolhe Empresa…]") || spinner_tipo.getItemAtPosition(spinner_tipo.getSelectedItemPosition()).toString().equals("[Escolhe Categoria…]")) {
                    Toast.makeText(AddActFragment.super.getContext(), "Empresa ou funcao de actividade nao seleciondo!", Toast.LENGTH_SHORT).show();
                }
                else{

                    Boolean checkactivity = databaseHelper.checkactivity(nome);
                    if (!checkactivity) {
                        Boolean insert = databaseHelper.insertactivity(nome, spinner, spinner2, pessoa, meta, valor, thisUser);
                        if (insert) {
                            spinner1.setSelection(0);
                            spinner_tipo.setSelection(0);
                            nome.equals("");
                            valor.equals("");
                            meta.equals("");
                            pessoa.equals("");

                            Toast.makeText(AddActFragment.super.getContext(), "Dados inseridos com Sucesso!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getContext(), MainActivity.class); //pode-se registar varias actividades nao precisa sair
                            startActivity(intent);
/*
                            AtividadeDTO dto = new AtividadeDTO(
                                    spinner1.getItemAtPosition(spinner1.getSelectedItemPosition()).toString(),
                                    nome,
                                    pessoa,
                                    125);
                            apiService.creatActivity(dto).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        System.out.println(":::::: Atividade cadastrada :::::");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    System.out.println(t.getMessage());
                                }
                            });

 */


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
        spinner_tipo = view.findViewById(R.id.spinner_tipo);
        loadSpinnerData();
    }

    private void loadSpinnerData() {
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(requireActivity(), R.array.Empresa, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(requireActivity(), R.array.categoria, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner_tipo.setAdapter(adapter2);
        spinner_tipo.setOnItemSelectedListener(this);


    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
if(spinner_tipo.getItemAtPosition(spinner_tipo.getSelectedItemPosition()).toString().equals("PRESENÇA")){
            View c = getView().findViewById(R.id.meta);
            c.setVisibility(View.GONE);

        }else{
    View c = getView().findViewById(R.id.meta);
    c.setVisibility(View.VISIBLE);
}
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}