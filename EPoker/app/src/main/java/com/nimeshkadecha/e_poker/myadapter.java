package com.nimeshkadecha.e_poker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class myadapter extends RecyclerView.Adapter<myadapter.MyViewHolder> {
    private Context context;
    private ArrayList name , mobile ,email,lic;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        holder.lictv.setText(String.valueOf(lic.get(position)));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        Button aprov,reject;
        TextView name , email , mobile, lictv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cname);
            email = itemView.findViewById(R.id.cemail);
            mobile = itemView.findViewById(R.id.cnumber);
            lictv = itemView.findViewById(R.id.clic);
            aprov = itemView.findViewById(R.id.approvebtn);
            reject = itemView.findViewById(R.id.rejectbtn);

            aprov.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View view) {
                    boolean d = approveUser(String.valueOf(lic.get(getAdapterPosition())));
                }
            });

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean check = rejectUser(String.valueOf(lic.get(getAdapterPosition())));
                }
            });
        }
    }

    boolean rejectUser(String licc){
        final boolean[] check = {false};
        Map<String,Object> reject = new HashMap<>();
        reject.put("Approved",2);
        db.collection("advocate").whereEqualTo("Approved",0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                db.collection("advocate").document(licc)
                                        .update(reject)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                check[0] = true;
                                                Log.d("ENimesh","con "+check[0]);
                                                myadapter.this.notifyDataSetChanged();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                check[0] = false;
                                                Log.d("ENimesh","False1 ?");
                                            }
                                        });
                            }
                        }
                        else{
                            check[0]=false;
                            Log.d("ENimesh","False2 ?");
                        }
                    }

                });
        return check[0];
    }

    boolean approveUser(String licc){
        final boolean[] check = {false};
        Log.d("ENimesh",licc);
        Map <String ,Object> aprove = new HashMap<>();
        aprove.put("Approved",1);

        db.collection("advocate").whereEqualTo("Approved",0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                String licnum = document.getString("Licence");
                                if(licnum.equals(licc)){
                                    db.collection("advocate").document(licc)
                                            .update(aprove)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    check[0]=true;
                                                    Log.d("ENimesh","Seccess");
                                                    myadapter.this.notifyDataSetChanged();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    check[0]=false;
                                                    Log.d("ENimesh","fail to update");
                                                }
                                            });
                                }
                                else {
                                    Log.d("ENimesh","Failed to compare");
                                }
                            }
                        }else{
                            check[0]=false;
                            Log.d("ENimesh","fail to success");

                        }
                    }
                });
        return check[0];
    }


}
