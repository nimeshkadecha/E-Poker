package com.nimeshkadecha.e_poker;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminCase extends AppCompatActivity {
    private Button addCase;

    private RecyclerView caseRecycle;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList cnr, room, date, lic, mob;

    private ProgressBar pb ;

    caseAdapter Cadapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_case);

        pb = findViewById(R.id.PlodingADMINCase);
        pb.setVisibility(View.VISIBLE);

        boolean net = checkConnection();

        addCase = findViewById(R.id.addCBtn);
        addCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAddcase = new Intent(AdminCase.this, Addcase.class);
                pb.setVisibility(View.INVISIBLE);
                startActivity(goToAddcase);
            }
        });

        if (net){
            cnr = new ArrayList<>();
            room = new ArrayList<>();
            date = new ArrayList<>();
            lic = new ArrayList<>();
            mob = new ArrayList<>();

            caseRecycle = findViewById(R.id.caseRec);
            Cadapter = new caseAdapter(this, cnr, room, date, lic, mob);
            caseRecycle.setAdapter(Cadapter);
            caseRecycle.setLayoutManager(new LinearLayoutManager(this));

            db.collection("case")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            pb.setVisibility(View.VISIBLE);
                            cnr.removeAll(cnr);
                            room.removeAll(room);
                            date.removeAll(date);
                            lic.removeAll(lic);
                            mob.removeAll(mob);
                            for (QueryDocumentSnapshot document : value) {
                                String cc = String.valueOf(document.get("CaseCondition"));
                                int CaseCon = Integer.parseInt(cc);
                                if (CaseCon < 2) {
                                    cnr.add(document.getString("CNR"));
                                    room.add(document.getString("Room"));
                                    date.add(document.getString("Date"));
                                    lic.add(document.getString("ALicence"));
                                    mob.add(document.getString("AMobile"));
                                    Cadapter.notifyDataSetChanged();
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    });
        }else{
            pb.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }
}