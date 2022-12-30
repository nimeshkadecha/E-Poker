package com.nimeshkadecha.e_pokerWeb;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class advocate extends AppCompatActivity {


    private RecyclerView AdvocateRec;

    private TextView statust;

    private ProgressBar pb;

    private Spinner spinner;

    private Button logOut, roomCase,search,cancle;

    private EditText searchet;

    public static final String SHARED_PREFS = "sharedPrefs";

    private ArrayList Acnr, Aroom, Adate, Alic, Amobile, ACstatus;

    AdvocateCaseAdapter A_adapter;

    String[] shorting = {"Today", "All", "Called", "Completed", "New"};

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

        //        Generating and formating date --------------------------
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
//        Log.d("ENimesh", "Date =" + formattedDate);
////        ----------------------------------------------------

//        Removing Suport bar / top line containing name
        Objects.requireNonNull(getSupportActionBar()).hide();

        createNotificationChannel();

        statust = findViewById(R.id.statustv);

        pb = findViewById(R.id.PlodingAdvocate);
        pb.setVisibility(View.VISIBLE);

        roomCase = findViewById(R.id.roomCase);
        roomCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoRoomCase = new Intent(advocate.this, roomCase.class);
                startActivity(gotoRoomCase);
            }
        });

        if (checkConnection()) {
            statust.setText("");
            statust.setVisibility(View.INVISIBLE);
        } else {
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
        A_adapter = new AdvocateCaseAdapter(this, Acnr, Aroom, Adate, Alic, Amobile, ACstatus);
        AdvocateRec.setAdapter(A_adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        AdvocateRec.setLayoutManager(linearLayoutManager);
//        AdvocateRec.setLayoutManager(new LinearLayoutManager(this));

        //        Working with Dropdown ----------------------------------------------------
        final String[] customize = new String[1];

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(advocate.this, android.R.layout.simple_spinner_item, shorting);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String value = adapterView.getItemAtPosition(position).toString();
                customize[0] = value;
                Acnr.removeAll(Acnr);
                Aroom.removeAll(Aroom);
                Adate.removeAll(Adate);
                Alic.removeAll(Alic);
                Amobile.removeAll(Amobile);
                ACstatus.removeAll(ACstatus);

                DatabaseReference reference;
                reference = FirebaseDatabase.getInstance().getReference();

                reference.child("case").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Map map = (Map) snapshot.getValue();
//                Log.d("ENimesh","length = " + map.size());
                        String check_licence = String.valueOf(map.get("ALicence"));
                        int condition = Integer.parseInt(String.valueOf(map.get("CaseCondition")));
                        String date = String.valueOf(map.get("Date"));
//                        Log.d("ENimesh", "Date2 =" + date);
                        if (check_licence.equals(licence)) {
                            if (customize[0].equals("Today")) {
                                if (date.equals(formattedDate)) {
                                    Acnr.add(map.get("CNR"));
                                    Aroom.add(map.get("Room"));
                                    Adate.add(map.get("Date"));
                                    Alic.add(map.get("ALicence"));
                                    Amobile.add(map.get("AMobile"));
                                    ACstatus.add(map.get("CaseCondition"));
                                    if (condition == 1) {
                                        callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                                    }
                                    A_adapter.notifyDataSetChanged();
                                    statust.setVisibility(View.INVISIBLE);
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            } else if (customize[0].equals("All")) {
                                Acnr.add(map.get("CNR"));
                                Aroom.add(map.get("Room"));
                                Adate.add(map.get("Date"));
                                Alic.add(map.get("ALicence"));
                                Amobile.add(map.get("AMobile"));
                                ACstatus.add(map.get("CaseCondition"));
                                if (condition == 1) {
                                    callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                                }
                                A_adapter.notifyDataSetChanged();
                                statust.setVisibility(View.INVISIBLE);
                                pb.setVisibility(View.INVISIBLE);
                            } else if (customize[0].equals("Called")) {
                                if (condition == 1) {
                                    Acnr.add(map.get("CNR"));
                                    Aroom.add(map.get("Room"));
                                    Adate.add(map.get("Date"));
                                    Alic.add(map.get("ALicence"));
                                    Amobile.add(map.get("AMobile"));
                                    ACstatus.add(map.get("CaseCondition"));
//                            Calling notification
                                    callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));

                                    A_adapter.notifyDataSetChanged();
                                    statust.setVisibility(View.INVISIBLE);
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            } else if (customize[0].equals("Completed")) {
                                if (condition == 2) {
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
                            } else if (customize[0].equals("New")) {
                                if (condition == 0) {
                                    Acnr.add(map.get("CNR"));
                                    Aroom.add(map.get("Room"));
                                    Adate.add(map.get("Date"));
                                    Alic.add(map.get("ALicence"));
                                    Amobile.add(map.get("AMobile"));
                                    ACstatus.add(map.get("CaseCondition"));
                                    if (condition == 1) {
                                        callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                                    }
                                    A_adapter.notifyDataSetChanged();
                                    statust.setVisibility(View.INVISIBLE);
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                Acnr.add(map.get("CNR"));
                                Aroom.add(map.get("Room"));
                                Adate.add(map.get("Date"));
                                Alic.add(map.get("ALicence"));
                                Amobile.add(map.get("AMobile"));
                                ACstatus.add(map.get("CaseCondition"));
                                if (condition == 1) {
                                    callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                                }
                                A_adapter.notifyDataSetChanged();
                                statust.setVisibility(View.INVISIBLE);
                                pb.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Map map = (Map) snapshot.getValue();
                        String check_licence = String.valueOf(map.get("ALicence"));
                        String date = String.valueOf(map.get("Date"));
                        int indexx = Acnr.indexOf(map.get("CNR"));
                        int condition = Integer.parseInt(String.valueOf(map.get("CaseCondition")));

                        if (check_licence.equals(licence)) {
                            if (customize[0].equals("Today")) {
                                if (date.equals(formattedDate)) {
                                    Acnr.remove(indexx);
                                    Aroom.remove(indexx);
                                    Adate.remove(indexx);
                                    Alic.remove(indexx);
                                    ACstatus.remove(indexx);

                                    Acnr.add(map.get("CNR"));
                                    Aroom.add(map.get("Room"));
                                    Adate.add(map.get("Date"));
                                    Alic.add(map.get("ALicence"));
                                    Amobile.add(map.get("AMobile"));
                                    ACstatus.add(map.get("CaseCondition"));
                                    if (condition == 1) {
                                        callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                                    }
                                    A_adapter.notifyDataSetChanged();

                                    statust.setVisibility(View.INVISIBLE);
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            } else if (customize[0].equals("All")) {
                                Acnr.remove(indexx);
                                Aroom.remove(indexx);
                                Adate.remove(indexx);
                                Alic.remove(indexx);
                                ACstatus.remove(indexx);

                                Acnr.add(map.get("CNR"));
                                Aroom.add(map.get("Room"));
                                Adate.add(map.get("Date"));
                                Alic.add(map.get("ALicence"));
                                Amobile.add(map.get("AMobile"));
                                ACstatus.add(map.get("CaseCondition"));
                                if (condition == 1) {
                                    callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                                }
                                A_adapter.notifyDataSetChanged();

                                statust.setVisibility(View.INVISIBLE);
                                pb.setVisibility(View.INVISIBLE);
                            } else if (customize[0].equals("called")) {
                                if (condition == 1) {
                                    Acnr.remove(indexx);
                                    Aroom.remove(indexx);
                                    Adate.remove(indexx);
                                    Alic.remove(indexx);
                                    ACstatus.remove(indexx);

                                    Acnr.add(map.get("CNR"));
                                    Aroom.add(map.get("Room"));
                                    Adate.add(map.get("Date"));
                                    Alic.add(map.get("ALicence"));
                                    Amobile.add(map.get("AMobile"));
                                    ACstatus.add(map.get("CaseCondition"));
//                            Always call
                                    callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));

                                    A_adapter.notifyDataSetChanged();

                                    statust.setVisibility(View.INVISIBLE);
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            } else if (customize[0].equals("Completed")) {
                                if (condition == 2) {
                                    Acnr.remove(indexx);
                                    Aroom.remove(indexx);
                                    Adate.remove(indexx);
                                    Alic.remove(indexx);
                                    ACstatus.remove(indexx);

                                    Acnr.add(map.get("CNR"));
                                    Aroom.add(map.get("Room"));
                                    Adate.add(map.get("Date"));
                                    Alic.add(map.get("ALicence"));
                                    Amobile.add(map.get("AMobile"));
                                    ACstatus.add(map.get("CaseCondition"));
                                    if (condition == 1) {
                                        callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                                    }
                                    A_adapter.notifyDataSetChanged();

                                    statust.setVisibility(View.INVISIBLE);
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            } else if (customize[0].equals("New")) {
                                if (condition == 0) {
                                    Acnr.remove(indexx);
                                    Aroom.remove(indexx);
                                    Adate.remove(indexx);
                                    Alic.remove(indexx);
                                    ACstatus.remove(indexx);

                                    Acnr.add(map.get("CNR"));
                                    Aroom.add(map.get("Room"));
                                    Adate.add(map.get("Date"));
                                    Alic.add(map.get("ALicence"));
                                    Amobile.add(map.get("AMobile"));
                                    ACstatus.add(map.get("CaseCondition"));
                                    if (condition == 1) {
                                        callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                                    }
                                    A_adapter.notifyDataSetChanged();

                                    statust.setVisibility(View.INVISIBLE);
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                Acnr.remove(indexx);
                                Aroom.remove(indexx);
                                Adate.remove(indexx);
                                Alic.remove(indexx);
                                ACstatus.remove(indexx);

                                Acnr.add(map.get("CNR"));
                                Aroom.add(map.get("Room"));
                                Adate.add(map.get("Date"));
                                Alic.add(map.get("ALicence"));
                                Amobile.add(map.get("AMobile"));
                                ACstatus.add(map.get("CaseCondition"));
                                if (condition == 1) {
                                    callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                                }
                                A_adapter.notifyDataSetChanged();

                                statust.setVisibility(View.INVISIBLE);
                                pb.setVisibility(View.INVISIBLE);
                            }
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        ------------------------------------------------------------------------


//        Search work ------------------------------------------------

        search = findViewById(R.id.SearchCNRbtn);
        cancle = findViewById(R.id.cancleSearch);
        searchet = findViewById(R.id.CNRSearchET);

        cancle.setVisibility(View.INVISIBLE);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchString = searchet.getText().toString();
                if(searchString.equals("")){
                    searchet.setError("Enter CNR number to search");
                }else{
                    Acnr.removeAll(Acnr);
                    Aroom.removeAll(Aroom);
                    Adate.removeAll(Adate);
                    Alic.removeAll(Alic);
                    Amobile.removeAll(Amobile);
                    ACstatus.removeAll(ACstatus);
                    DatabaseReference referenceCNR ;
                    referenceCNR = FirebaseDatabase.getInstance().getReference();
                    referenceCNR.child("case").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Map map = (Map) snapshot.getValue();
                            String check_licence = String.valueOf(map.get("ALicence"));
                            String dbCNR = String.valueOf(map.get("CNR"));
                            int condition = Integer.parseInt(String.valueOf(map.get("CaseCondition")));
                            if (check_licence.equals(licence)) {
                                if(searchString.equals(dbCNR)){
                                    Acnr.add(map.get("CNR"));
                                    Aroom.add(map.get("Room"));
                                    Adate.add(map.get("Date"));
                                    Alic.add(map.get("ALicence"));
                                    Amobile.add(map.get("AMobile"));
                                    ACstatus.add(map.get("CaseCondition"));
                                    if (condition == 1) {
                                        callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                                    }
                                    A_adapter.notifyDataSetChanged();
                                    statust.setVisibility(View.INVISIBLE);
                                    pb.setVisibility(View.INVISIBLE);

                                    cancle.setVisibility(View.VISIBLE);
                                    search.setVisibility(View.INVISIBLE);
                                }
                            }

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancle.setVisibility(View.INVISIBLE);
                search.setVisibility(View.VISIBLE);

                DatabaseReference referencecancle;
                referencecancle = FirebaseDatabase.getInstance().getReference();

                referencecancle.child("case").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Map map = (Map) snapshot.getValue();
                        String check_licence = String.valueOf(map.get("ALicence"));
                        int condition = Integer.parseInt(String.valueOf(map.get("CaseCondition")));
                        if (check_licence.equals(licence)) {
                            Acnr.add(map.get("CNR"));
                            Aroom.add(map.get("Room"));
                            Adate.add(map.get("Date"));
                            Alic.add(map.get("ALicence"));
                            Amobile.add(map.get("AMobile"));
                            ACstatus.add(map.get("CaseCondition"));
                            if (condition == 1) {
                                callNotification(String.valueOf(map.get("CNR")), String.valueOf(map.get("Room")));
                            }
                            A_adapter.notifyDataSetChanged();
                            statust.setVisibility(View.INVISIBLE);
                            pb.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
//        _-------------------------------------------------searchwork end---------

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statust.setText("");
                statust.setVisibility(View.INVISIBLE);
                SharedPreferences sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Login", "false");
                editor.putString("UserName", "");
                editor.putString("Licence", "");
                editor.apply();

                Intent logOUT = new Intent(advocate.this, MainActivity.class);
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

    private void callNotification(String cnr, String room) {
        Random r = new Random();
        int id = r.nextInt(99);
        String CHANNEL_ID = "com.nimeshkadecha.e_pokerWeb";
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.toolbarlogo)
                .setContentTitle("Call for CNR = " + cnr)
                .setContentText("call room = " + room)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent).setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());
    }
}