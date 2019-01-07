package com.henallux.ravelup.features.ravel;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.henallux.ravelup.R;
import com.henallux.ravelup.model.TrajetModel;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> implements View.OnClickListener,View.OnLongClickListener{

    private ArrayList<TrajetModel> dataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public View Layout;
        TextView trip;
        MyViewHolder(View v){
            super(v);
            Layout= v;
            trip = v.findViewById(R.id.trajet_recyclerViewTrajet);
        }
    }

    TripAdapter(ArrayList<TrajetModel> myDataset){
        dataset= myDataset;
    }

    public TripAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.list_trajet_description_activity,parent,false);
        return new MyViewHolder(v);
    }

    public void onBindViewHolder(TripAdapter.MyViewHolder holder, final int position){
        holder.trip.setText(dataset.get(position).descriptionRecyclerView());

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
