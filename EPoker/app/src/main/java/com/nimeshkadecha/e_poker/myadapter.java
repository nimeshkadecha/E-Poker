package com.nimeshkadecha.e_poker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myadapter extends RecyclerView.Adapter<myadapter.MyViewHolder> {
    private Context context;
    private ArrayList name , mobile ,email,lic;

    public myadapter(Context context, ArrayList name, ArrayList email, ArrayList mobile, ArrayList lic) {
        this.context = context;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.lic = lic;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.singlerow,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(String.valueOf(name.get(position)));
        holder.email.setText(String.valueOf(email.get(position)));
        holder.mobile.setText(String.valueOf(mobile.get(position)));
        holder.lic.setText(String.valueOf(lic.get(position)));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name , email , mobile, lic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cname);
            email = itemView.findViewById(R.id.cemail);
            mobile = itemView.findViewById(R.id.cnumber);
            lic = itemView.findViewById(R.id.clic);
        }
    }
}
