package com.nimeshkadecha.e_pokerWeb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class advocate extends AppCompatActivity {


    private RecyclerView AdvocateRec;

    private TextView statust;

    private ProgressBar pb;

    private Button logOut;

    private ArrayList Acnr, Aroom, Adate, Alic, Amobile,ACstatus;

    AdvocateCaseAdapter A_adapter;

    String b = "E-Poker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advocate);

        statust = findViewById(R.id.statustv);

        pb=findViewById(R.id.PlodingAdvocate);
        pb.setVisibility(View.VISIBLE);

        logOut = findViewById(R.id.logoutAdvocate);

        //        Getting user email from intent
        Bundle b = getIntent().getExtras();
        String user = b.getString("user");

        final String[] licence = new String[1];

        Acnr = new ArrayList<>();
        Aroom = new ArrayList<>();
        Adate = new ArrayList<>();
        Alic = new ArrayList<>();
        Amobile = new ArrayList<>();
        ACstatus = new ArrayList<>();

        AdvocateRec = findViewById(R.id.AdvocateRec);
        A_adapter = new AdvocateCaseAdapter(this, Acnr, Aroom, Adate, Alic, Amobile,ACstatus);
        AdvocateRec.setAdapter(A_adapter);
        AdvocateRec.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("case").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Acnr.removeAll(Acnr);
                Aroom.removeAll(Aroom);
                Adate.removeAll(Adate);
                Alic.removeAll(Alic);
                Amobile.removeAll(Amobile);
                ACstatus.removeAll(ACstatus);
                Map map = (Map) snapshot.getValue();
                Acnr.add(map.get("CNR"));
                Aroom.add(map.get("Room"));
                Adate.add(map.get("Date"));
                Alic.add(map.get("ALicence"));
                Amobile.add(map.get("AMobile"));
                ACstatus.add(map.get("CaseCondition"));
                A_adapter.notifyDataSetChanged();

                statust.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Acnr.removeAll(Acnr);
                Aroom.removeAll(Aroom);
                Adate.removeAll(Adate);
                Alic.removeAll(Alic);
                Amobile.removeAll(Amobile);
                ACstatus.removeAll(ACstatus);
                Map map = (Map) snapshot.getValue();
                Log.d("ENimesh","cnr ="+map.get("CNR"));
                Acnr.add(map.get("CNR"));
                Aroom.add(map.get("Room"));
                Adate.add(map.get("Date"));
                Alic.add(map.get("ALicence"));
                Amobile.add(map.get("AMobile"));
                ACstatus.add(map.get("CaseCondition"));
                A_adapter.notifyDataSetChanged();

                statust.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(advocate.this, "ERROR", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
            }
        });
    }
}