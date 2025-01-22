package com.example.sch_agro.ui.fragment;

import static com.example.sch_agro.R.mipmap.ic_launcher;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sch_agro.R;
import com.example.sch_agro.databinding.FragmentAddUserBinding;
import com.example.sch_agro.ui.activity.MainActivity;
import com.example.sch_agro.util.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddUserFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    FragmentAddUserBinding binding;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText name,docid,idade,telefone,gender,id;
    Spinner spinner1,spinner2;

    String doid;
    Button Submit,edit;
    ImageView image;
    RadioButton male,female;
    RadioGroup radioGroup;


    String userGender = "";
    String checked;

    public static final int CAMERA_REQUEST=100;
    public static final int STORAGE_REQUEST=101;
    String cameraPermission[];
    String storagePermission[];




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

            //


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_add_user, container, false);
       // binding= AddUserFragment.inflate(inflater, container, false);



        binding= FragmentAddUserBinding.inflate(inflater, container, false);

        databaseHelper = new DatabaseHelper(super.getContext());



        binding.Submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                sqLiteDatabase = databaseHelper.getWritableDatabase();

                try {
                if (name.length() == 0 || docid.length() == 0 || idade.length() == 0 || userGender.isEmpty() || telefone.length() == 0 || image.equals(ic_launcher)) {

                    Toast.makeText(AddUserFragment.super.getContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }else if(spinner2.getItemAtPosition(spinner2.getSelectedItemPosition()).toString().equals("[Tipo de Trabalhador…]")||spinner1.getItemAtPosition(spinner1.getSelectedItemPosition()).toString().equals("[Escolhe Empresa…]")){
                    Toast.makeText(AddUserFragment.super.getContext(), "Empresa ou tipo de trabalhador nao seleciondo!", Toast.LENGTH_SHORT).show();
                }
                // image.callOnClick(); this function one day i have to investigate so that when submit button is clicked it opens camera and take image and save image together with the data.


                else{
                        Boolean checdocid = databaseHelper.checkdocid(String.valueOf(docid));

                        //   databaseHelper.RadioButtonClicked();
                        if (checdocid == false) {

                            ContentValues cv = new ContentValues();
                            cv.put("empresa", spinner1.getItemAtPosition(spinner1.getSelectedItemPosition()).toString());
                            cv.put("nome", name.getText().toString());
                            cv.put("docid", docid.getText().toString());
                            cv.put("idade", idade.getText().toString());
                            cv.put("genero", userGender.toString());
                            cv.put("telefone", telefone.getText().toString());
                            cv.put("tipo", spinner2.getItemAtPosition(spinner2.getSelectedItemPosition()).toString());
                            cv.put("image", ImageViewToBy(image));

                            long result = sqLiteDatabase.insert("trabalhadores", null, cv);


                            if (result == -1) {

                                Toast.makeText(AddUserFragment.super.getContext(), "Dados nao inseridos!", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(AddUserFragment.super.getContext(), "Dados inseridos com Sucesso!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(AddUserFragment.super.getActivity(), MainActivity.class);
                                startActivity(intent);
                                name.setText("");
                                docid.setText("");
                                idade.setText("");
                                gender.setText("");
                                telefone.setText("");
                                spinner1.setSelection(0);
                                spinner2.setSelection(0);
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

/*
this funcion is only used in the useredit activity and not here.
        //////////button edit or inserting new data task
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // sqLiteDatabase=databaseHelper.getReadableDatabase();

                ContentValues cv =new ContentValues();
                // cv.put("id",id);
                cv.put("image",ImageViewToBy(image));//is diferente imageviewtobyte original.
                cv.put("name",name.getText().toString());
                cv.put("docid",docid.getText().toString());
                cv.put("telefone",telefone.getText().toString());
                //cv.put("act",act.getText().toString());
                cv.put("act",spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
                cv.put("user_id",id.getText().toString());

                sqLiteDatabase=databaseHelper.getWritableDatabase();

                if (spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString()!="[Escolhe Actividade...]"){
                    long recedit = sqLiteDatabase.insert(task, null, cv);
                    if (recedit!=-1){
                        Toast.makeText(AddUserFragment.super.getContext(), "Data Inserted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        image.setImageResource(ic_launcher);
                        name.setText("");
                        Submit.setVisibility(View.GONE);
                        edit.setVisibility(View.VISIBLE);

                    } else{
                        Toast.makeText(AddUserFragment.super.getContext(), "Something is wrong", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(AddUserFragment.super.getContext(), "Escolhe Actividade", Toast.LENGTH_SHORT).show();
                }
            }
        });

 */

        return binding.getRoot();
    }
    

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        databaseHelper= new DatabaseHelper(super.getContext());
        //editData();
        //insertData();
        spinner1 = view.findViewById(R.id.spinner1);
        spinner2 = view.findViewById(R.id.spinner2);
        name= (EditText)view.findViewById(R.id.nome);
        docid= (EditText)view.findViewById(R.id.docid);
        idade= (EditText)view.findViewById(R.id.idade);
        male = (RadioButton)view.findViewById(R.id.male);
        female = (RadioButton)view.findViewById(R.id.female);
        telefone= (EditText)view.findViewById(R.id.telefone);
        id= (EditText)view.findViewById(R.id.user_id);
        userGender=userGender.toString();
        image = (ImageView)view.findViewById(R.id.edtimage);
        Submit = (Button)view.findViewById(R.id.Submit);
        edit = (Button)view.findViewById(R.id.btn_edit);
        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
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
    }

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

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(requireActivity(),R.array.Empresa, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(),R.array.tipo_trabalahador, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(this);






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
            idade.setVisibility(View.GONE);
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





//isto nao estava no primeiro ver bem

}