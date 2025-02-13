package com.example.sch_agro.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sch_agro.DTO.ActivityCount;
import com.example.sch_agro.R;
import com.example.sch_agro.util.DatabaseHelper;

import java.util.List;

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

        dbHelper = new DatabaseHelper(super.getContext());

        // Atualiza os contadores quando a view for criada
        atualizarContadores();
        populateTable();
    }

    public void atualizarContadores() {
        int totalTrabalhadores = dbHelper.getCount("trabalhadores");
        int totalAtividades = dbHelper.getCount("activity");
        trabalhadoresText.setText(String.valueOf(totalTrabalhadores));
        atividadesText.setText(String.valueOf(totalAtividades));
    }

    public void populateTable() {
        TableLayout tableLayout = getView().findViewById(R.id.tableLayout); // Obtém a referência à tabela no Fragment
        List<ActivityCount> activityCounts = dbHelper.getActivityCount(); // Obtém os dados do banco

        for (ActivityCount activity : activityCounts) {
            TableRow tableRow = new TableRow(requireContext()); // Corrigido para usar Context do Fragment
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // Criar a coluna Categoria
            TextView categoryTextView = new TextView(requireContext());
            categoryTextView.setLayoutParams(new TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1));
            categoryTextView.setText(activity.getActivityName());
            categoryTextView.setTextSize(16);
            categoryTextView.setGravity(Gravity.CENTER);
            categoryTextView.setPadding(8, 8, 8, 8);

            // Criar a coluna Quantidade
            TextView quantityTextView = new TextView(requireContext());
            quantityTextView.setLayoutParams(new TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1));
            quantityTextView.setText(String.valueOf(activity.getTotalTrabalhadores()));
            quantityTextView.setTextSize(16);
            quantityTextView.setGravity(Gravity.CENTER);
            quantityTextView.setPadding(8, 8, 8, 8);

            // Adicionar as colunas à linha
            tableRow.addView(categoryTextView);
            tableRow.addView(quantityTextView);

            // Adicionar a linha à tabela
            tableLayout.addView(tableRow);

            // Adicionar um divisor entre as linhas
            View divider = new View(requireContext());
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, 2);
            divider.setLayoutParams(params);
            divider.setBackgroundColor(getResources().getColor(R.color.lavender));
            tableLayout.addView(divider);

        }
    }

}
