package com.example.sch_agro.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.sch_agro.Configuration.ApiClient;
import com.example.sch_agro.DTO.IntervaloDTO;
import com.example.sch_agro.R;
import com.example.sch_agro.Services.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExportDialogFragment extends DialogFragment {
    private static final int STORAGE_PERMISSION_CODE = 1001;

    private TextInputEditText txtStartDate, txtEndDate;
    private RadioGroup radioGroupCategorias, radioGroupTipo;
    private RadioButton radioMetas, radioPresencas, radioResumo, radioDetalhado;
    private Button btnBaixar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_export);

        // Inicializando componentes
        initViews(dialog);

        // Eventos de clique para DatePickers
        setDatePickers();

        // Botão de baixar
        setDownloadButton(dialog);

        return dialog;
    }

    // Inicializa as views
    private void initViews(Dialog dialog) {
        txtStartDate = dialog.findViewById(R.id.txtStartDate);
        txtEndDate = dialog.findViewById(R.id.txtEndDate);

        radioGroupCategorias = dialog.findViewById(R.id.radioGroupCategorias);
        radioGroupTipo = dialog.findViewById(R.id.radioGroupTipo);

        radioMetas = dialog.findViewById(R.id.radioMetas);
        radioPresencas = dialog.findViewById(R.id.radioPresencas);
        radioResumo = dialog.findViewById(R.id.radioResumo);
        radioDetalhado = dialog.findViewById(R.id.radioDetalhado);

        btnBaixar = dialog.findViewById(R.id.btnBaixar);
    }

    // Configura os listeners dos DatePickers
    private void setDatePickers() {
        txtStartDate.setOnClickListener(v -> showDatePickerDialog(txtStartDate));
        txtEndDate.setOnClickListener(v -> showDatePickerDialog(txtEndDate));
    }

    // Configura o listener do botão de baixar
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDownloadButton(Dialog dialog) {
        btnBaixar.setOnClickListener(v -> {
            String startDate = txtStartDate.getText().toString();
            String endDate = txtEndDate.getText().toString();
            String categoriaSelecionada = getSelectedCategoria();
            String tipoSelecionado = getSelectedTipo();

            // Captura os dados para processamento
            processarDados(startDate, endDate, categoriaSelecionada, tipoSelecionado);

            dismiss(); // Fecha o modal
        });
    }

    // Método para obter a categoria selecionada
    private String getSelectedCategoria() {
        int selectedId = radioGroupCategorias.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = getDialog().findViewById(selectedId);
        return selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";
    }

    // Método para obter o tipo selecionado
    private String getSelectedTipo() {
        int selectedId = radioGroupTipo.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = getDialog().findViewById(selectedId);
        return selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";
    }

    // Método para abrir o DatePickerDialog
    private void showDatePickerDialog(TextInputEditText textView) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    textView.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Processamento dos dados selecionados
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void processarDados(String startDate, String endDate, String categoria, String tipo) {
        // Aqui você pode enviar os dados para a API ou processar no app
        System.out.println("Dados Selecionados:");
        System.out.println("Data Inicial: " + startDate);
        System.out.println("Data Final: " + endDate);
        System.out.println("Categoria: " + categoria);
        System.out.println("Tipo: " + tipo);

        if(categoria == "Metas") {
            fazerDownloadRelatorio(startDate, endDate, categoria, tipo);
        }else{
            fazerDownloadRelatorioPresencas(startDate, endDate, categoria, tipo);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fazerDownloadRelatorio(String startDate, String endDate, String categoria, String tipo) {
        try {
            // Converte as strings de data para o formato adequado (yyyy-MM-dd)
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate dataInicio = LocalDate.parse(startDate, inputFormatter);
            LocalDate dataFim = LocalDate.parse(endDate, inputFormatter);

            String dataInicioFormatada = dataInicio.format(outputFormatter);
            String dataFimFormatada = dataFim.format(outputFormatter);

            IntervaloDTO intervaloDTO = new IntervaloDTO(dataInicioFormatada, dataFimFormatada);

            ApiService service = ApiClient.getClient().create(ApiService.class);

            // Mantemos uma referência fraca ao Fragment
            final WeakReference<ExportDialogFragment> fragmentRef = new WeakReference<>(this);

            service.gerarRelatorioAtividadesPdf(intervaloDTO)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ExportDialogFragment fragment = fragmentRef.get();
                            if (fragment != null && fragment.isAdded()) {
                                if (response.isSuccessful() && response.body() != null) {
                                    fragment.salvarArquivoPDF(response.body());
                                } else {
                                    fragment.mostrarErro("Erro ao gerar relatório");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            ExportDialogFragment fragment = fragmentRef.get();
                            if (fragment != null && fragment.isAdded()) {
                                fragment.mostrarErro("Erro de conexão: " + t.getMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            if (isAdded()) {
                mostrarErro("Erro ao processar datas: " + e.getMessage());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fazerDownloadRelatorioPresencas(String startDate, String endDate, String categoria, String tipo) {
        try {
            // Converte as strings de data para o formato adequado (yyyy-MM-dd)
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate dataInicio = LocalDate.parse(startDate, inputFormatter);
            LocalDate dataFim = LocalDate.parse(endDate, inputFormatter);

            String dataInicioFormatada = dataInicio.format(outputFormatter);
            String dataFimFormatada = dataFim.format(outputFormatter);

            IntervaloDTO intervaloDTO = new IntervaloDTO(dataInicioFormatada, dataFimFormatada);

            ApiService service = ApiClient.getClient().create(ApiService.class);

            // Mantemos uma referência fraca ao Fragment
            final WeakReference<ExportDialogFragment> fragmentRef = new WeakReference<>(this);

            service.gerarRelatorioMotoristasPdf(intervaloDTO)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ExportDialogFragment fragment = fragmentRef.get();
                            if (fragment != null && fragment.isAdded()) {
                                if (response.isSuccessful() && response.body() != null) {
                                    fragment.salvarArquivoPDF(response.body());
                                } else {
                                    fragment.mostrarErro("Erro ao gerar relatório");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            ExportDialogFragment fragment = fragmentRef.get();
                            if (fragment != null && fragment.isAdded()) {
                                fragment.mostrarErro("Erro de conexão: " + t.getMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            if (isAdded()) {
                mostrarErro("Erro ao processar datas: " + e.getMessage());
            }
        }
    }

    private void salvarArquivoPDF(ResponseBody body) {
        if (!isAdded() || getContext() == null) {
            return;
        }
        try {
            // Solicita permissão de escrita se necessário
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE);
                return;
            }

            // Cria o diretório de downloads se não existir
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs();
            }

            // Cria o arquivo
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File pdfFile = new File(downloadsDir, "relatorio_" + timeStamp + ".pdf");

            // Escreve o conteúdo do PDF
            InputStream inputStream = body.byteStream();
            OutputStream outputStream = new FileOutputStream(pdfFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            // Notifica o usuário
            mostrarSucesso("Arquivo salvo em: " + pdfFile.getAbsolutePath());

            // Atualiza a galeria para mostrar o novo arquivo
            MediaScannerConnection.scanFile(requireContext(),
                    new String[]{pdfFile.getAbsolutePath()}, null, null);

        } catch (IOException e) {
            mostrarErro("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    private void mostrarSucesso(String mensagem) {
        if (isAdded() && getContext() != null) {
            Activity activity = getActivity();
            if (activity != null && !activity.isFinishing()) {
                activity.runOnUiThread(() -> {
                    Toast.makeText(getContext(), mensagem, Toast.LENGTH_LONG).show();
                });
            }
        }
    }

    private void mostrarErro(String mensagem) {
        if (isAdded() && getContext() != null) {
            Activity activity = getActivity();
            if (activity != null && !activity.isFinishing()) {
                activity.runOnUiThread(() -> {
                    Toast.makeText(getContext(), mensagem, Toast.LENGTH_LONG).show();
                });
            }
        }
    }
}