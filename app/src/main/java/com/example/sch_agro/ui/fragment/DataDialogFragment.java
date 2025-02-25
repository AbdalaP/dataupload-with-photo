package com.example.sch_agro.ui.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.example.sch_agro.Configuration.ApiClient;
import com.example.sch_agro.DTO.IntervaloDTO;
import com.example.sch_agro.R;
import com.example.sch_agro.Services.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataDialogFragment extends DialogFragment {
    private static final int STORAGE_PERMISSION_CODE = 1001;
    private static final int REQUEST_CODE_PICK_DIRECTORY = 1001;

    private TextInputEditText txtStartDate, txtEndDate;
    private Button btnBaixar;
    private Boolean actividade;
    public DataDialogFragment(Boolean actividade) {
        this.actividade = actividade;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext() != null ? getContext() : requireActivity());  // Substituído requireContext() por getContext() com verificação

        dialog.setContentView(R.layout.dialog_export_act);

        // Inicializando componentes
        initViews(dialog);

        // Eventos de clique para DatePickers
        setDatePickers();

        // Botão de baixar
        setDownloadButton(dialog);

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    // Inicializa as views
    private void initViews(Dialog dialog) {
        txtStartDate = dialog.findViewById(R.id.txtStartDate);
        txtEndDate = dialog.findViewById(R.id.txtEndDate);

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

            // Verifica permissões antes de processar os dados
            if (checkStoragePermission()) {
                // Captura os dados para processamento
                processarDados(startDate, endDate);
            } else {
                requestStoragePermission();
            }

            // Não fecha o modal diretamente aqui, mantendo-o aberto durante o download
        });
    }

//    private boolean checkStoragePermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                // Exibe a mensagem pedindo permissão caso não tenha
//                Toast.makeText(getContext(), "Por favor, conceda permissões de armazenamento para continuar.", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private void requestStoragePermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // Solicita permissões de armazenamento
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//        }
//    }

    // Verifica permissões de armazenamento
    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // Para Android 9 ou inferior, verifica WRITE_EXTERNAL_STORAGE
            return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        // Para Android 10+, não precisa verificar WRITE_EXTERNAL_STORAGE, pois o SAF é usado
        return true;
    }

    // Solicita permissões de armazenamento se necessário
    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            // Em Android 10+, redireciona o usuário para escolher um diretório para salvar o arquivo
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_CODE_PICK_DIRECTORY);
        }
    }

    private void showDatePickerDialog(TextInputEditText textView) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext() != null ? getContext() : requireActivity(),
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
    private void processarDados(String startDate, String endDate) {
        if(actividade){
            fazerDownloadAtividadesExcel(startDate, endDate);
        }else{
            fazerDownloadTrabalhadoresExcel(startDate, endDate);
        }
    }

    final WeakReference<DataDialogFragment> fragmentRef = new WeakReference<>(this);

    private void salvarArquivoPDF(ResponseBody responseBody, String nome) {
        try {
            // Cria uma ContentValues para o arquivo
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, nome + ".pdf");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            // Salva o arquivo no diretório de Downloads
            Uri uri = requireContext().getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);

            if (uri != null) {
                try (OutputStream outputStream = requireContext().getContentResolver().openOutputStream(uri)) {
                    InputStream inputStream = responseBody.byteStream();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    Toast.makeText(getContext(), "Relatório baixado com sucesso!", Toast.LENGTH_SHORT).show();
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("Download", "Erro ao salvar o arquivo", e);
                    mostrarErro("Erro ao salvar o arquivo: " + e.getMessage());
                }
            } else {
                Log.e("Download", "Erro ao criar URI para o arquivo");
            }
        } catch (Exception e) {
            mostrarErro("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fazerDownloadAtividadesExcel(String startDate, String endDate) {
        try {
            // Formatação das datas
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate dataInicio = LocalDate.parse(startDate, inputFormatter);
            LocalDate dataFim = LocalDate.parse(endDate, inputFormatter);

            String dataInicioFormatada = dataInicio.format(outputFormatter);
            String dataFimFormatada = dataFim.format(outputFormatter);

            IntervaloDTO intervaloDTO = new IntervaloDTO(dataInicioFormatada, dataFimFormatada);
            ApiService service = ApiClient.getClient().create(ApiService.class);

            // Referência fraca ao Fragment
            final WeakReference<DataDialogFragment> fragmentRef = new WeakReference<>(this);

            service.getAtividadesExcel(intervaloDTO)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            DataDialogFragment fragment = fragmentRef.get();
                            if (fragment != null && fragment.isAdded()) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Log.d("Download", "Arquivo Excel recebido, tamanho: " + response.body().contentLength());
                                    fragment.salvarArquivoExcel(response.body(), "actividades");
                                } else {
                                    Log.e("Download", "Resposta falhou: " + response.code());
                                    fragment.mostrarErro("Erro ao gerar relatório");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            DataDialogFragment fragment = fragmentRef.get();
                            if (fragment != null && fragment.isAdded()) {
                                fragment.mostrarErro("Erro de conexão: " + t.getMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            if (isAdded()) {
                System.out.println("Erro ao processar datas: " + e.getMessage());
                mostrarErro("Erro ao processar datas: " + e.getMessage());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fazerDownloadTrabalhadoresExcel(String startDate, String endDate) {
        try {
            // Formatação das datas
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate dataInicio = LocalDate.parse(startDate, inputFormatter);
            LocalDate dataFim = LocalDate.parse(endDate, inputFormatter);

            String dataInicioFormatada = dataInicio.format(outputFormatter);
            String dataFimFormatada = dataFim.format(outputFormatter);

            IntervaloDTO intervaloDTO = new IntervaloDTO(dataInicioFormatada, dataFimFormatada);
            ApiService service = ApiClient.getClient().create(ApiService.class);

            // Referência fraca ao Fragment
            final WeakReference<DataDialogFragment> fragmentRef = new WeakReference<>(this);

            service.getTrabalhadoresExcel(intervaloDTO)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            DataDialogFragment fragment = fragmentRef.get();
                            if (fragment != null && fragment.isAdded()) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Log.d("Download", "Arquivo Excel recebido, tamanho: " + response.body().contentLength());
                                    fragment.salvarArquivoExcel(response.body(), "trabalhadores");
                                } else {
                                    Log.e("Download", "Resposta falhou: " + response.code());
                                    fragment.mostrarErro("Erro ao gerar relatório");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            DataDialogFragment fragment = fragmentRef.get();
                            if (fragment != null && fragment.isAdded()) {
                                fragment.mostrarErro("Erro de conexão: " + t.getMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            if (isAdded()) {
                System.out.println("Erro ao processar datas: " + e.getMessage());
                mostrarErro("Erro ao processar datas: " + e.getMessage());
            }
        }
    }

    private void salvarArquivoExcel(ResponseBody responseBody, String nome) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, nome + ".xlsx");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = requireContext().getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);

            if (uri != null) {
                try (OutputStream outputStream = requireContext().getContentResolver().openOutputStream(uri)) {
                    InputStream inputStream = responseBody.byteStream();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    Toast.makeText(getContext(), "Relatório Excel baixado com sucesso!", Toast.LENGTH_SHORT).show();
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("Download", "Erro ao salvar o arquivo", e);
                    mostrarErro("Erro ao salvar o arquivo: " + e.getMessage());
                }
            } else {
                Log.e("Download", "Erro ao criar URI para o arquivo");
                mostrarErro("Não foi possível criar o local para salvar o arquivo");
            }
        } catch (Exception e) {
            mostrarErro("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }


    private void mostrarErro(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }
}
