package com.nimeshkadecha.e_poker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class caseAdapter extends RecyclerView.Adapter<caseAdapter.myViewHolder> {
    private Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList cnr,room,date,lic,mobile;

    public caseAdapter(Context context, ArrayList cnr, ArrayList room, ArrayList date, ArrayList lic, ArrayList mobile) {
        this.context = context;
        this.cnr = cnr;
        this.room = room;
        this.date = date;
        this.lic = lic;
        this.mobile = mobile;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.caserow,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.cnrtv.setText(String.valueOf(cnr.get(position)));
        holder.roomtv.setText(String.valueOf(room.get(position)));
        holder.datetv.setText(String.valueOf(date.get(position)));
        holder.licencetv.setText(String.valueOf(lic.get(position)));
        holder.numbertv.setText(String.valueOf(mobile.get(position)));
    }

    @Override
    public int getItemCount() {
        return cnr.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView cnrtv,roomtv,datetv,licencetv,numbertv;

        Button call,complete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            cnrtv = itemView.findViewById(R.id.CCcnr);
            roomtv = itemView.findViewById(R.id.CCroom);
            datetv = itemView.findViewById(R.id.CCdate);
            licencetv = itemView.findViewById(R.id.CAlic);
            numbertv = itemView.findViewById(R.id.CAmob);

            call = itemView.findViewById(R.id.Ccall);
            complete = itemView.findViewById(R.id.Ccomplete);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean c = callcase(String.valueOf(cnr.get(getAdapterPosition())));
                }
            });

            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean complet = completeCase(String.valueOf(cnr.get(getAdapterPosition())));
                }
            });

        }
    }
    boolean completeCase(String cnr){
        final boolean[] check = {false};

        Map <String,Object> call = new HashMap<>();
        call.put("CaseCondition",2);

        db.collection("case").whereEqualTo("CaseCondition",1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot Document : task.getResult()){
                                String CNr = String.valueOf(Document.getString("CNR"));
                                if (CNr.equals(cnr)){
                                    db.collection("case").document(cnr)
                                            .update(call)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    check[0] = true;
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    check[0] = false;
                                                }
                                            });
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Enimesh", String.valueOf(e));
                        check[0] = false;
                    }
                });
        return check[0];
    }
    boolean callcase(String cnr){
        final boolean[] check = {false};

        Map <String,Object> call = new HashMap<>();
        call.put("CaseCondition",1);

        db.collection("case").whereEqualTo("CaseCondition",0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot Document : task.getResult()){
                                String CNr = String.valueOf(Document.getString("CNR"));
                                if (CNr.equals(cnr)){
                                    db.collection("case").document(cnr)
                                            .update(call)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    check[0] = true;
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    check[0] = false;
                                                }
                                            });
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Enimesh", String.valueOf(e));
                        check[0] = false;
                    }
                });
        return check[0];
    }
}