package com.nimeshkadecha.e_pokerWeb;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;

public class AdvocateCaseAdapter extends RecyclerView.Adapter<AdvocateCaseAdapter.viewHolder> {
    private Context context;
    private ArrayList acnr,aroom,adate,alic,amobile,astatus;

    public AdvocateCaseAdapter(Context context, ArrayList acnr, ArrayList aroom, ArrayList adate, ArrayList alic, ArrayList amobile, ArrayList astatus) {
        this.context = context;
        this.acnr = acnr;
        this.aroom = aroom;
        this.adate = adate;
        this.alic = alic;
        this.amobile = amobile;
        this.astatus = astatus;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.advocate_case_list,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.ACcnrtv.setText(String.valueOf(acnr.get(position)));
        holder.ACroomtv.setText(String.valueOf(aroom.get(position)));
        holder.ACdatetv.setText(String.valueOf(adate.get(position)));
        holder.AAlictv.setText(String.valueOf(alic.get(position)));
        holder.AAmobtv.setText(String.valueOf(amobile.get(position)));
        int x = Integer.parseInt(String.valueOf(astatus.get(position)));

        if(x == 0){
            holder.newimg.setVisibility(View.VISIBLE);
            holder.calledimg.setVisibility(View.INVISIBLE);
            holder.completeimg.setVisibility(View.INVISIBLE);
            holder.warning.setVisibility(View.INVISIBLE);
        }else if(x == 1){
            holder.newimg.setVisibility(View.INVISIBLE);
            holder.calledimg.setVisibility(View.VISIBLE);
            holder.completeimg.setVisibility(View.INVISIBLE);
            holder.warning.setVisibility(View.INVISIBLE);
        }else if (x == 2){
            holder.newimg.setVisibility(View.INVISIBLE);
            holder.calledimg.setVisibility(View.INVISIBLE);
            holder.completeimg.setVisibility(View.VISIBLE);
            holder.warning.setVisibility(View.INVISIBLE);
        } else if (x == 3) {
            holder.newimg.setVisibility(View.INVISIBLE);
            holder.calledimg.setVisibility(View.INVISIBLE);
            holder.completeimg.setVisibility(View.INVISIBLE);
            holder.warning.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return acnr.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView ACcnrtv,ACroomtv,ACdatetv,AAlictv,AAmobtv;
        ImageView newimg,calledimg,completeimg,warning;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ACcnrtv = itemView.findViewById(R.id.ACcnr);
            ACroomtv = itemView.findViewById(R.id.ACroom);
            ACdatetv = itemView.findViewById(R.id.ACdate);
            AAlictv = itemView.findViewById(R.id.AAlic);
            AAmobtv = itemView.findViewById(R.id.AAmob);

            newimg = itemView.findViewById(R.id.imgNew);
            calledimg = itemView.findViewById(R.id.imgcall);
            completeimg = itemView.findViewById(R.id.imgcompleted);
            warning = itemView.findViewById(R.id.warning);

            newimg.setVisibility(View.INVISIBLE);
            calledimg.setVisibility(View.INVISIBLE);
            completeimg.setVisibility(View.INVISIBLE);
            warning.setVisibility(View.INVISIBLE);
        }
    }

}

