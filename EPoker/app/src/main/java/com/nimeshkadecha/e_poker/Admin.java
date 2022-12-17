package com.nimeshkadecha.e_poker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {

    RecyclerView Rview;

    ArrayList<String> name, email, number, lic;

    myadapter adapter;

    FirebaseFirestore db;

    Button Caseb;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Caseb = findViewById(R.id.btnCase);
        Caseb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });


        name = new ArrayList<>();
        email = new ArrayList<>();
        number = new ArrayList<>();
        lic = new ArrayList<>();

        Rview = findViewById(R.id.recview);
        adapter = new myadapter(this, name, email, number, lic);
        Rview.setAdapter(adapter);
        Rview.setLayoutManager(new LinearLayoutManager(this));

        addData();
    }

    private void addData() {
        db = FirebaseFirestore.getInstance();
        db.collection("advocate").whereEqualTo("Approved",0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                name.add(document.getString("Name"));
                                email.add(document.getString("Email"));
                                number.add(document.getString("Mobile"));
                                lic.add(document.getString("Licence"));
//                                Log.d("ENimesh", String.valueOf(name));
//                                Log.d("ENimesh", String.valueOf(email));
//                                Log.d("ENimesh", String.valueOf(number));
//                                Log.d("ENimesh", String.valueOf(lic));
                            }
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(Admin.this, "Failed to load", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}