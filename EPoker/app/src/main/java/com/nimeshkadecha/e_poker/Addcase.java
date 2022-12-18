package com.nimeshkadecha.e_poker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PrimitiveIterator;

public class Addcase extends AppCompatActivity {

    private EditText cnr , room,date,AdvocateLic,AdvocateMob;
    private Button add;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcase);
        cnr = findViewById(R.id.Ccnr);
        room = findViewById(R.id.Croom);
        date = findViewById(R.id.Cdate);
        AdvocateLic = findViewById(R.id.Clic);
        AdvocateMob = findViewById(R.id.CcontactNumber);

        add = findViewById(R.id.addCaseB);
        //        Generating and formating date --------------------------
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        date.setText(formattedDate);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cnrS = cnr.getText().toString();
                String roomS = room.getText().toString();
                String dateS = date.getText().toString();
                String licS = AdvocateLic.getText().toString();
                String mobS = AdvocateMob.getText().toString();
                if(cnrS.isEmpty()){
                    Toast.makeText(Addcase.this, "Fill CNR Number Detail", Toast.LENGTH_SHORT).show();
                }else if (roomS.isEmpty()){
                    Toast.makeText(Addcase.this, "Fill Room Detail", Toast.LENGTH_SHORT).show();
                }else if (dateS.isEmpty()){
                    Toast.makeText(Addcase.this, "Fill Date Detail", Toast.LENGTH_SHORT).show();
                }else if (licS.isEmpty()){
                    Toast.makeText(Addcase.this, "Fill Advocate Licence Detail", Toast.LENGTH_SHORT).show();
                }else if (mobS.isEmpty()){
                    Toast.makeText(Addcase.this, "Fill Advocate Mobile Number Detail", Toast.LENGTH_SHORT).show();
                }
                else{
                    Map<String,Object> Ccase = new HashMap<>();
                    Ccase.put("CNR",cnrS);
                    Ccase.put("Room",roomS);
                    Ccase.put("Date",dateS);
                    Ccase.put("ALicence",licS);
                    Ccase.put("AMobile",mobS);
                    Ccase.put("CaseCondition",0);

                    db.collection("case").document(cnrS)
                            .set(Ccase)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Addcase.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Addcase.this, "Failed to load", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }
}