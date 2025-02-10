package com.example.sch_agro.ui.fragment;

import static com.example.sch_agro.R.mipmap.ic_launcher;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sch_agro.R;
import com.example.sch_agro.databinding.FragmentAddUserBinding;
import com.example.sch_agro.ui.activity.MainActivity;
import com.example.sch_agro.ui.activity.Session;
import com.example.sch_agro.util.CatActividades;
import com.example.sch_agro.util.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddUserFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    FragmentAddUserBinding binding;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText name,docid,telefone,gender,id;
    TextView data_nascimento;
    DatePickerDialog datePickerDialog;
    Spinner spinner1,spinner_tipoAct;

    String doid;
    Button Submit,edit;
    ImageView image;
    RadioButton male,female;
    RadioGroup radioGroup;

    TextView tvDate;
    String userGender = "";
    String checked;

    public static final int CAMERA_REQUEST=100;
    public static final int STORAGE_REQUEST=101;
    String cameraPermission[];
    String storagePermission[];

    int year_x,month_x,day_x;
    static final int DILOG_ID=0;



    Uri mImageCaptureUri;
    File mFileTemp;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Intent intent;

    public AddUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddUserFragment newInstance(String param1, String param2) {
        AddUserFragment fragment = new AddUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_add_user, container, false);
       // binding= AddUserFragment.inflate(inflater, container, false);


        Session session = new Session(AddUserFragment.super.requireActivity());
        binding= FragmentAddUserBinding.inflate(inflater, container, false);

        databaseHelper = new DatabaseHelper(super.getContext());



        binding.Submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                sqLiteDatabase = databaseHelper.getWritableDatabase();

                try {
                if (name.length() == 0 || docid.length() == 0 || data_nascimento.length() == 0 || userGender.isEmpty() || telefone.length() == 0 || image.equals(ic_launcher)) {

                    Toast.makeText(AddUserFragment.super.getContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }else if(spinner_tipoAct.getItemAtPosition(spinner_tipoAct.getSelectedItemPosition()).toString().equals("[Escolhe Actividade…]")||spinner1.getItemAtPosition(spinner1.getSelectedItemPosition()).toString().equals("[Escolhe Empresa…]")){
                    Toast.makeText(AddUserFragment.super.getContext(), "Empresa ou tipo de trabalhador nao seleciondo!", Toast.LENGTH_SHORT).show();
                }
                // image.callOnClick(); this function one day i have to investigate so that when submit button is clicked it opens camera and take image and save image together with the data.


                else{
                        Boolean checdocid = databaseHelper.checkdocid(String.valueOf(docid));

                        //   databaseHelper.RadioButtonClicked();
                        if (checdocid == false) {
                            String thisUser = session.getKeyUserId();
                            ContentValues cv = new ContentValues();
                            cv.put("empresa", spinner1.getItemAtPosition(spinner1.getSelectedItemPosition()).toString());
                            cv.put("nome", name.getText().toString());
                            cv.put("docid", docid.getText().toString());
                            cv.put("data_nascimento", data_nascimento.getText().toString());
                            cv.put("genero", userGender.toString());
                            cv.put("telefone", telefone.getText().toString());
                            cv.put("activity_id", spinner_tipoAct.getItemAtPosition(spinner_tipoAct.getSelectedItemPosition()).toString());
                            cv.put("image", ImageViewToBy(image));
                            cv.put("userlog",thisUser);

                            long result = sqLiteDatabase.insert("trabalhadores", null, cv);


                            if (result == -1) {

                                Toast.makeText(AddUserFragment.super.getContext(), "Dados nao inseridos!", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(AddUserFragment.super.getContext(), "Dados inseridos com Sucesso!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(AddUserFragment.super.getActivity(), MainActivity.class);
                                startActivity(intent);
                                name.setText("");
                                docid.setText("");
                                data_nascimento.setText("");
                                gender.setText("");
                                telefone.setText("");
                                spinner1.setSelection(0);
                                spinner_tipoAct.setSelection(0);
                                image.setImageResource(ic_launcher);

                            }
                        } else {
                            Toast.makeText(AddUserFragment.super.getContext(), "O Trabalhador ja foi registado no sistema!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception error1){
                    Toast.makeText(AddUserFragment.super.getContext(), "Tira foto ao produtor", Toast.LENGTH_SHORT).show();
                }
            }

        });

        return binding.getRoot();


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final java.util.Calendar cal = java.util.Calendar.getInstance();
        year_x=cal.get(java.util.Calendar.YEAR);
        month_x=cal.get(java.util.Calendar.MONTH);
        day_x=cal.get(java.util.Calendar.DAY_OF_MONTH);

      //  showDialogontextclic();

        databaseHelper= new DatabaseHelper(super.getContext());
        //editData();
        //insertData();
        spinner1 = view.findViewById(R.id.spinner1);
        spinner_tipoAct = view.findViewById(R.id.spinner_tipoAct);
        name= (EditText)view.findViewById(R.id.nome);
        docid= (EditText)view.findViewById(R.id.docid);
        data_nascimento= (TextView) view.findViewById(R.id.data_nascimento);
        male = (RadioButton)view.findViewById(R.id.male);
        female = (RadioButton)view.findViewById(R.id.female);
        telefone= (EditText)view.findViewById(R.id.telefone);
        id= (EditText)view.findViewById(R.id.user_id);
        userGender=userGender.toString();
        image = (ImageView)view.findViewById(R.id.edtimage);
        Submit = (Button)view.findViewById(R.id.Submit);
        edit = (Button)view.findViewById(R.id.btn_edit);
        spinner1.setOnItemSelectedListener(this);
        spinner_tipoAct.setOnItemSelectedListener(this);
       // loadSpinnerData(); only useful in editactivity
        //image= findViewById(R.id.edtimage);


        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

            //load spinner fucnion call
        loadSpinnerData();

        radioGroup = (RadioGroup)view.findViewById(R.id.radiogp);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId==R.id.female) {
                        userGender = "female";
                } else if (checkedId== R.id.male) {
                        userGender = "male";
                }
            }

        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int image = 0;
                if(image==0){
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                        Toast.makeText(AddUserFragment.super.getContext(), "No camera permission", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddUserFragment.super.getContext(), "Something is Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // perform click event on edit text

        List<CatActividades> listaCategorias = llenarCategorias();
        ArrayAdapter<CatActividades> arrayAdapter = new ArrayAdapter<>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listaCategorias);
        spinner_tipoAct.setAdapter(arrayAdapter);

        spinner_tipoAct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) adapterView.getChildAt(0)).setTextSize(18);

                String nombreCat = ((CatActividades) adapterView.getSelectedItem()).getNombre();

               // Toast.makeText(AddUserFragment.super.getActivity(), idCat + " - " + nombreCat, Toast.LENGTH_LONG).show();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }

        });

        }





    protected Dialog onCreateDialog(int id){
        if(id==DILOG_ID)
            return new DatePickerDialog(AddUserFragment.super.getActivity(),dpickerListner,year_x,month_x,day_x);
        return null;

    }

    private DatePickerDialog.OnDateSetListener dpickerListner= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x=year;
            month_x=month+1;
            day_x=dayOfMonth;
            // Toast.makeText(Registo.this, year_x +"/"+month_x+"/"+day_x,Toast.LENGTH_SHORT).show();

            data_nascimento.setText(year_x +"/"+month_x+"/"+day_x);

        }
    };
/*
    public void showDialogontextclic(){

        data_nascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  Registo r = new Registo();
              //  r.showDialogontextclic();

            }
        });
    }

 */


    private void requestCameraPermission() {requestPermissions(cameraPermission,CAMERA_REQUEST);}

    private boolean checkCameraPermission() {
        boolean result= ContextCompat.checkSelfPermission(super.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(super.getContext(), Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        return  result && result1;
    }

    /*
    Load spinner not required in the user registration level
    private void loadSpinnerData() {
        List<String> labels = databaseHelper.getAllLabels();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(super.getContext(),
                R.layout.color_spinner_layout,
                labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        dataAdapter.add("[Escolhe Actividade...]");
        spinner.setSelection(dataAdapter.getCount()); //set the hint the default selection so it appears on launch.
        spinner.setOnItemSelectedListener(this);
    }

     */

    private void insertData() {

        //
    }

    public static byte[] ImageViewToBy(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[]bytes=stream.toByteArray();
        return bytes;
    }

    private void loadSpinnerData() {


               //sqLiteDatabase = databaseHelper.getWritableDatabase();

       // Cursor cursor = sqLiteDatabase("Select activity_name from activity", null);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(requireActivity(),R.array.Empresa, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);



        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(),R.array.tipo_actividade, android.R.layout.simple_spinner_item);
      //  adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
       // spinner_tipoAct.setAdapter(adapter);
       // spinner_tipoAct.setOnItemSelectedListener(this);

    }

    private void editData() {
        if(getActivity().getIntent().getBundleExtra("userdata")!=null){
            Bundle bundle=getActivity().getIntent().getBundleExtra("userdata");
            byte[]bytes=bundle.getByteArray("image");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            image.setImageBitmap(bitmap);
            id.setText(bundle.getString("user_id"));
            name.setText(bundle.getString("name"));
            docid.setText(bundle.getString("docid"));
            telefone.setText(bundle.getString("telefone"));
            //spinner.setText(bundle.getString("act"));
            Submit.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            data_nascimento.setVisibility(View.GONE);
            male.setVisibility(View.GONE);
            female.setVisibility(View.GONE);
            spinner1.setVisibility(View.VISIBLE);
            id.setVisibility(View.VISIBLE);
            // EditText name = (EditText) findViewById(R.id.nome);
            name.setEnabled(false);
            image.setEnabled(false);
            id.setEnabled(false);
            docid.setEnabled(false);
            telefone.setEnabled(false);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if (grantResults.length>0){
                    boolean camera_accepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted){
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,100);
                    }else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,100);
                    }
                }
            }
            break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);
            // image.setImageIcon(Icon.createWithBitmap(bitmap));
            // image.getDrawable();//novo

        }else {
            Toast.makeText(AddUserFragment.super.getContext(), "something is shit", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




//load spinner with cursor.
    @SuppressLint("Range")
    private List<CatActividades> llenarCategorias(){
        List<CatActividades> listaCat = new ArrayList<>();
        DatabaseHelper dbCategorias = new DatabaseHelper(AddUserFragment.super.getActivity());
        Cursor cursor = dbCategorias.populatespinner();
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    CatActividades cat = new CatActividades();
                    cat.setNombre(cursor.getString(cursor.getColumnIndex("activity_name")));
                    listaCat.add(cat);
                } while (cursor.moveToNext());
            }
        }
        dbCategorias.close();

        return listaCat;
    }

//isto nao estava no primeiro ver bem

}