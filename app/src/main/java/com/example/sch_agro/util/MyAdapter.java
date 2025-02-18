package com.example.sch_agro.util;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sch_agro.R;
import com.example.sch_agro.ui.activity.UserActivoInativo;
import com.example.sch_agro.ui.activity.UserEditSan;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {
Context context;
int singledata;
List<Model> modelArrayList;
List<Model>modelArrayListFull;
SQLiteDatabase sqLiteDatabase;

//generate constractor

    public MyAdapter(Context context, int singledata, ArrayList<Model> modelArrayList, SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.singledata = singledata;
        this.modelArrayListFull = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.modelArrayList= new ArrayList<>(modelArrayListFull);
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.singledata,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        final Model model=modelArrayList.get(position);
        byte[]image=model.getProavatar();
        Bitmap bitmap= BitmapFactory.decodeByteArray(image,0,image.length);
        holder.imageavatar.setImageBitmap(bitmap);
        holder.txtid.setText(model.getTrabalhador_id());
        holder.txtname.setText(model.getnome());

        //flow menu
        holder.flowmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.flowmenu);
                popupMenu.inflate(R.menu.flow_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId=item.getItemId();
                        if (itemId==R.id.activar_desativar){
                            Bundle bundle=new Bundle();
                            bundle.putByteArray("image",model.getProavatar());
                            bundle.putString("name",model.getnome());
                            bundle.putString("trabalhador_id",model.getTrabalhador_id());
                            Intent intent=new Intent(context, UserActivoInativo.class);
                            intent.putExtra("userdata",bundle);
                            context.startActivity(intent);

                        } else if (itemId==R.id.Edit_menuSan) {
                            Bundle bundle=new Bundle();
                            bundle.putByteArray("image",model.getProavatar());
                            bundle.putString("name",model.getnome());
                            bundle.putString("trabalhador_id",model.getTrabalhador_id());
                            bundle.putString("activity_id",model.getActivity_id());
                            Intent intent=new Intent(context, UserEditSan.class);
                            intent.putExtra("userdata",bundle);
                            context.startActivity(intent);


                        }else {
                            Toast.makeText(context, "Escolha Errada!", Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                });

                //display menu
                popupMenu.show();
            }
        });

    }
//for search data
    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

//for search data
    @Override
    public Filter getFilter() {
        return modelFilter;
    }
//for search data in recycle view
    private Filter modelFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           ArrayList<Model> filteredModelList= new ArrayList<>();

           if(constraint==null || constraint.length()==0){
               filteredModelList.addAll(modelArrayListFull);
           }else {
               String filterPattern = constraint.toString().toLowerCase().trim();
               for (Model models: modelArrayListFull){
                   if (models.getnome().toLowerCase().contains(filterPattern)||models.getTrabalhador_id().toLowerCase().contains(filterPattern))
                       filteredModelList.add(models);
               }
           }
           FilterResults results = new FilterResults();
           results.values = filteredModelList;
           results.count = filteredModelList.size();
           return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            modelArrayList.clear();
            modelArrayList.addAll((ArrayList)results.values);
            notifyDataSetChanged();

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageavatar;
        TextView txtname,txtid;
        ImageButton flowmenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageavatar=(ImageView)itemView.findViewById(R.id.viewavatar);
            txtid=(TextView)itemView.findViewById(R.id.txt_id);
            txtname=(TextView)itemView.findViewById(R.id.txt_name);
            flowmenu=(ImageButton)itemView.findViewById(R.id.flowmenu);
        }
    }
}
