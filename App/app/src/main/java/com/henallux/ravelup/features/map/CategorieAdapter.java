package com.henallux.ravelup.features.map;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.henallux.ravelup.R;
import com.henallux.ravelup.model.CategoryModel;

import java.util.ArrayList;

public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.MyViewHolder>{
    private ArrayList<CategoryModel> dataset;
    private ArrayList<Long> idCategories;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public View Layout;
        public Switch switchCategorie;
        public MyViewHolder(View v){
            super(v);
            Layout= v;
            switchCategorie = v.findViewById(R.id.switchCategorie);
        }
    }

    public CategorieAdapter(ArrayList<CategoryModel> myDataset){
        dataset= myDataset;
    }

    public CategorieAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v =(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cat_menu_map,parent,false);
        MyViewHolder vh= new MyViewHolder(v);

        return vh;
    }

    public void onBindViewHolder(MyViewHolder holder, final int position){
        holder.switchCategorie.setText(dataset.get(position).getLibelle());
        idCategories = new ArrayList<>();
        holder.switchCategorie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    idCategories.add(dataset.get(position).getId());
                } else {
                    idCategories.remove(dataset.get(position).getId());
                }
            }
        });
    }

    public int getItemCount(){
        return dataset.size();
    }

    public ArrayList<Long> getIdCategories(){
        return idCategories;
    }
}
