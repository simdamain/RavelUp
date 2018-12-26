package com.henallux.ravelup.features.map;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.henallux.ravelup.R;

import java.util.ArrayList;

public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.MyViewHolder>{
    private ArrayList<String> dataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public View Layout;
        public Switch switchCategorie;
        public MyViewHolder(View v){
            super(v);
            Layout= v;
            switchCategorie = v.findViewById(R.id.switchCategorie);
        }
    }

    public CategorieAdapter(ArrayList<String> myDataset){
        dataset= myDataset;
    }

    public CategorieAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v =(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cat_menu_map,parent,false);
        MyViewHolder vh= new MyViewHolder(v);

        return vh;
    }

    public void onBindViewHolder(MyViewHolder holder,int position){
        holder.switchCategorie.setText(dataset.get(position));
    }

    public int getItemCount(){
        return dataset.size();
    }
}
