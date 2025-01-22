package com.example.sch_agro.ui.fragment;

import static com.example.sch_agro.util.DatabaseHelper.trabalhadores;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sch_agro.R;
import com.example.sch_agro.util.DatabaseHelper;
import com.example.sch_agro.util.Model;
import com.example.sch_agro.util.MyAdapter;

import java.util.ArrayList;

public class ViewFarmerFragment extends Fragment {


    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerview;
    MyAdapter myAdapter;
    SearchView searchView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewFarmerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment testFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewFarmerFragment newInstance(String param1, String param2) {
        ViewFarmerFragment fragment = new ViewFarmerFragment();
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
        return inflater.inflate(R.layout.fragment_viewfarmer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(super.getContext());
        displyData();
        recyclerview = view.findViewById(R.id.rv);
        searchView = view.findViewById(R.id.searchView);
        //findId();

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(myAdapter);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //filter(newText);
                myAdapter.getFilter().filter(newText);

                return false;
            }
        });
    }

    private void displyData() {
        sqLiteDatabase=databaseHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select* from "+ trabalhadores,null);

        ArrayList<Model> models=new ArrayList<>();
        while (cursor.moveToNext()){

            String id=cursor.getString(0);
            String name=cursor.getString(2);
            String docid=cursor.getString(3);
            String telefone=cursor.getString(6);
            byte[]image=cursor.getBlob(7);
            models.add(new Model(id,name,image,docid,telefone));
        }
        cursor.close();
        myAdapter=new MyAdapter(getContext(), R.layout.singledata,models,sqLiteDatabase);

    }

    private void findId() {
        recyclerview= recyclerview.findViewById(R.id.rv);
        searchView = searchView.findViewById(R.id.searchView);
        searchView.clearFocus();
    }

}