package com.nimeshkadecha.e_pokerWeb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;

public class advocate extends AppCompatActivity {


    private RecyclerView AdvocateRec;

    private TextView statust;

    private ProgressBar pb;

    private Button logOut;

    public static final String SHARED_PREFS = "sharedPrefs";

    private ArrayList Acnr, Aroom, Adate, Alic, Amobile,ACstatus;

    AdvocateCaseAdapter A_adapter;

    String b = "E-Poker";
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
        setContentView(R.layout.activity_advocate);

        boolean net = checkConnection();

        createNotificationChannel();

        statust = findViewById(R.id.statustv);

        pb=findViewById(R.id.PlodingAdvocate);
        pb.setVisibility(View.VISIBLE);

        if (checkConnection()){
            statust.setText("");
            statust.setVisibility(View.INVISIBLE);
        }else{
            pb.setVisibility(View.INVISIBLE);
            statust.setVisibility(View.VISIBLE);
            statust.setText("No Internet");
        }

        logOut = findViewById(R.id.logoutAdvocate);

        //        Getting user email from intent
        Bundle b = getIntent().getExtras();
        String user = b.getString("user");
        String licence = b.getString("licence");

        Acnr = new ArrayList<>();
        Aroom = new ArrayList<>();
        Adate = new ArrayList<>();
        Alic = new ArrayList<>();
        Amobile = new ArrayList<>();
        ACstatus = new ArrayList<>();

        AdvocateRec = findViewById(R.id.AdvocateRec);
        A_adapter = new AdvocateCaseAdapter(this, Acnr, Aroom, Adate, Alic, Amobile,ACstatus);
        AdvocateRec.setAdapter(A_adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        AdvocateRec.setLayoutManager(linearLayoutManager);
//        AdvocateRec.setLayoutManager(new LinearLayoutManager(this));


        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("case").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Map map = (Map) snapshot.getValue();
                String check_licence = String.valueOf(map.get("ALicence"));
                int condition = Integer.parseInt(String.valueOf(map.get("CaseCondition")));
                if(check_licence.equals(licence) && condition == 0){
                    Acnr.add(map.get("CNR"));
                    Aroom.add(map.get("Room"));
                    Adate.add(map.get("Date"));
                    Alic.add(map.get("ALicence"));
                    Amobile.add(map.get("AMobile"));
                    ACstatus.add(map.get("CaseCondition"));
                    if(condition == 1){
                        callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                    }
                    A_adapter.notifyDataSetChanged();

                    statust.setVisibility(View.INVISIBLE);
                    pb.setVisibility(View.INVISIBLE);
                }
                if(check_licence.equals(licence) && condition == 1){
                    Acnr.add(map.get("CNR"));
                    Aroom.add(map.get("Room"));
                    Adate.add(map.get("Date"));
                    Alic.add(map.get("ALicence"));
                    Amobile.add(map.get("AMobile"));
                    ACstatus.add(map.get("CaseCondition"));
                    if(condition == 1){
                        callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                    }
                    A_adapter.notifyDataSetChanged();

                    statust.setVisibility(View.INVISIBLE);
                    pb.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Map map = (Map) snapshot.getValue();
                String check_licence = String.valueOf(map.get("ALicence"));
                if(check_licence.equals(licence)){
                    int indexx = Acnr.indexOf(map.get("CNR"));

                    Acnr.remove(indexx);
                    Aroom.remove(indexx);
                    Adate.remove(indexx);
                    Alic.remove(indexx);
                    ACstatus.remove(indexx);
//                    Acnr.indexOf(map.get("CNR"));
                    Acnr.add(map.get("CNR"));
                    Aroom.add(map.get("Room"));
                    Adate.add(map.get("Date"));
                    Alic.add(map.get("ALicence"));
                    Amobile.add(map.get("AMobile"));
                    ACstatus.add(map.get("CaseCondition"));
                    int condition = Integer.parseInt(String.valueOf(map.get("CaseCondition")));
                    if(condition == 1){
                        callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                    }

                    A_adapter.notifyDataSetChanged();

                    statust.setVisibility(View.INVISIBLE);
                    pb.setVisibility(View.INVISIBLE);
                }
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

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statust.setText("");
                statust.setVisibility(View.INVISIBLE);
                SharedPreferences sp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Login","false");
                editor.putString("UserName","");
                editor.putString("Licence","");
                editor.apply();

                Intent logOUT = new Intent(advocate.this,MainActivity.class);
                startActivity(logOUT);
                finish();

            }
        });

    }
    //    Creating Notification
    private void createNotificationChannel() {
        String CHANNEL_ID = "com.nimeshkadecha.e_pokerWeb";

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Poker Notification";
            String description = "notify user if case is called";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void callNotification(String cnr,String room) {
        Random r = new Random();
        int id = r.nextInt(99);
        String CHANNEL_ID = "com.nimeshkadecha.e_pokerWeb";
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.toolbarlogo)
                .setContentTitle("Call for CNR = "+cnr)
                .setContentText("call room = "+ room)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent).setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());
    }
}