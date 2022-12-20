package com.nimeshkadecha.e_poker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {

    private RecyclerView Rview;

    private ArrayList<String> name, email, number, lic;

    public myadapter adapter;

    private ProgressBar pb;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button Caseb;
    //    Verifying internet is ON
    boolean checkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo net = manager.getActiveNetworkInfo();

        if (net == null) {
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        pb = findViewById(R.id.PlodingADMIN);
        pb.setVisibility(View.VISIBLE);

        Caseb = findViewById(R.id.btnCase);
        Caseb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCases = new Intent( Admin.this,AdminCase.class);
                pb.setVisibility(View.INVISIBLE);
                startActivity(goToCases);
            }
        });

        boolean net = checkConnection();

        if (net){
            name = new ArrayList<>();
            email = new ArrayList<>();
            number = new ArrayList<>();
            lic = new ArrayList<>();

            Rview = findViewById(R.id.recview);
            adapter = new myadapter(this, name, email, number, lic);
            Rview.setAdapter(adapter);
            Rview.setLayoutManager(new LinearLayoutManager(this));

//        addData();

            db.collection("advocate").whereEqualTo("Approved",0)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            pb.setVisibility(View.VISIBLE);
                            name.removeAll(name);
                            email.removeAll(email);
                            number.removeAll(number);
                            lic.removeAll(lic);
                            for (QueryDocumentSnapshot document : value){
                                name.add(document.getString("Name"));
                                email.add(document.getString("Email"));
                                number.add(document.getString("Mobile"));
                                lic.add(document.getString("Licence"));
                                adapter.notifyDataSetChanged();
                            }
                            pb.setVisibility(View.INVISIBLE);
                        }
                    });
        }
        else{
            pb.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }
}