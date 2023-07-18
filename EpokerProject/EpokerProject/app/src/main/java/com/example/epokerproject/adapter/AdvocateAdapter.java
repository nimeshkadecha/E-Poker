package com.example.epokerproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epokerproject.R;
import com.example.epokerproject.interfaces.ItemClickListener;

public class AdvocateAdapter extends RecyclerView.Adapter<AdvocateAdapter.ViewHolder>{
    ItemClickListener itemClickListener;

    public AdvocateAdapter(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_advocate,parent,false);
        // return holder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btnNotify.setOnClickListener(v -> {

            itemClickListener.onClick(position);

        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button btnNotify;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btnNotify = itemView.findViewById(R.id.btnNotify);
        }
    }
}
