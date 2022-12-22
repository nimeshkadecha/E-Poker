package com.nimeshkadecha.e_poker;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class advocate extends AppCompatActivity {

    private RecyclerView AdvocateRec;

    private TextView statust;

    private ProgressBar pb;

    private Button logOut;

    public static final String SHARED_PREFS = "sharedPrefs";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        statust = findViewById(R.id.statustv);

        pb=findViewById(R.id.PlodingAdvocate);
        pb.setVisibility(View.VISIBLE);

        logOut = findViewById(R.id.logoutAdvocate);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statust.setText("");
                statust.setVisibility(View.INVISIBLE);

                SharedPreferences sp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Login","false");
                editor.putString("UserName","");
                editor.apply();

                Intent logOUT = new Intent(advocate.this,MainActivity.class);
                startActivity(logOUT);
                finish();

            }
        });

        if (checkConnection()){
            statust.setText("");
            statust.setVisibility(View.INVISIBLE);
        }else{
            pb.setVisibility(View.INVISIBLE);
            statust.setVisibility(View.VISIBLE);
            statust.setText("No Internet");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "E-poker";
            String description = "poker notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(b, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

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

        db.collection("advocate").whereEqualTo("Email", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                licence[0] = document.getString("Licence");
                                Log.d("Enimesh", "Licence number of user is = " + licence[0]);
                                db.collection("case").whereEqualTo("ALicence", licence[0])
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {pb.setVisibility(View.VISIBLE);
                                                Acnr.removeAll(Acnr);
                                                Aroom.removeAll(Aroom);
                                                Adate.removeAll(Adate);
                                                Alic.removeAll(Alic);
                                                Amobile.removeAll(Amobile);
                                                ACstatus.removeAll(ACstatus);
                                                for (QueryDocumentSnapshot doc : value){
                                                    Acnr.add(doc.getString("CNR"));
                                                    Aroom.add(doc.getString("Room"));
                                                    Adate.add(doc.getString("Date"));
                                                    Alic.add(doc.getString("ALicence"));
                                                    Amobile.add(doc.getString("AMobile"));
                                                    ACstatus.add(doc.get("CaseCondition"));
                                                    int x =Integer.parseInt(String.valueOf(doc.get("CaseCondition")));
                                                    if(x==1){
                                                        callNotify(String.valueOf(doc.getString("CNR")),String.valueOf(doc.getString("Room")));
                                                    }
                                                    A_adapter.notifyDataSetChanged();
                                                }
                                                pb.setVisibility(View.INVISIBLE);
                                            }
                                        });
                            }
                        } else {
                            pb.setVisibility(View.INVISIBLE);
                            Toast.makeText(advocate.this, "Nothing to Show", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    void callNotify(String cnr, String room){
        Random rnd = new Random();
        int id = rnd.nextInt(99);

        Log.d("ENimesh","CNR == "+cnr);
        Log.d("ENimesh","Room == "+room);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(advocate.this, b)
                .setSmallIcon(R.drawable.message)
                .setContentTitle("Call for CNR = "+cnr)
                .setContentText("call room = "+ room)
                .setPriority(NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(advocate.this, advocate.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(advocate.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builderr = new NotificationCompat.Builder(advocate.this, b)
                .setSmallIcon(R.drawable.message)
                .setContentTitle("Call for CNR = "+cnr)
                .setContentText("call room = "+ room)
                .setPriority(NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setPriority(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(advocate.this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"Call");
//        builder.setContentTitle("Call Notification");
//        builder.setContentText("CNR Number =" + cnr);
//        builder.setContentText("in Room no = "+room);
//        builder.setAutoCancel(true);
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//        managerCompat.notify(1,builder.build());
    }
}