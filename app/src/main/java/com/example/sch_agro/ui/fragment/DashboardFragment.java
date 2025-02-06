package com.example.sch_agro.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sch_agro.R;
import com.example.sch_agro.util.DatabaseHelper;

public class DashboardFragment extends Fragment {

    private TextView trabalhadoresText;
    private TextView atividadesText;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate o layout do fragmento
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializando as views
        trabalhadoresText = view.findViewById(R.id.trabalhadores_count);
        atividadesText = view.findViewById(R.id.atividades_count);
        dbHelper = new DatabaseHelper(requireContext());

        // Atualiza os contadores quando a view for criada
        atualizarContadores();
    }

    public void atualizarContadores() {
        int totalTrabalhadores = dbHelper.getCount("trabalhadores");
        int totalAtividades = dbHelper.getCount("activity");
        trabalhadoresText.setText(String.valueOf(totalTrabalhadores));
        atividadesText.setText(String.valueOf(totalAtividades));
    }
}
