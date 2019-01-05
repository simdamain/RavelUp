package com.henallux.ravelup.features.ravel;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.henallux.ravelup.R;
import com.henallux.ravelup.models.TrajetModel;

import java.util.ArrayList;

public class TrajetAdapter extends RecyclerView.Adapter<TrajetAdapter.MyViewHolder> implements View.OnClickListener,View.OnLongClickListener{
    private ArrayList<TrajetModel> dataset;
    private Integer idTrajet;




    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public View Layout;
        public TextView trajet;
        public MyViewHolder(View v){
            super(v);
            Layout= v;
            trajet = v.findViewById(R.id.trajet_recyclerViewTrajet);
        }
    }

    public TrajetAdapter(ArrayList<TrajetModel> myDataset){
        dataset= myDataset;
    }

    public TrajetAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.list_trajet_description_activity,parent,false);
        TrajetAdapter.MyViewHolder vh= new TrajetAdapter.MyViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(TrajetAdapter.MyViewHolder holder, final int position){
        holder.trajet.setText(dataset.get(position).descriptionRecyclerView());

    }
    public int getItemCount(){
        return dataset.size();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
