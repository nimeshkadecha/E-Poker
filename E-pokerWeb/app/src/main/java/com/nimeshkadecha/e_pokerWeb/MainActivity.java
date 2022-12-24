package com.nimeshkadecha.e_pokerWeb;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private TextView register;

    private ProgressBar lodingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

//        Finding email and password
        email = findViewById(R.id.Lemail);
        password = findViewById(R.id.Lpassword);

//        Register textview
        login = findViewById(R.id.Llogin);
        register = findViewById(R.id.Lregister);

//        Loading screen
        lodingPB = findViewById(R.id.Ploding);

//        On click listener for Register text view and goin t oregister activity
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToRegister = new Intent(MainActivity.this, register.class);

                startActivity(goToRegister);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lodingPB.setVisibility(View.VISIBLE);
                String emails = email.getText().toString();
                String passwords = password.getText().toString();

                boolean ev = EmailValidation(emails); // validating email by calling method
                boolean pv = passwordValidation(passwords); // validating Password by calling method

//                        If both are ture then execut this method
                if (ev && pv) {
                    DatabaseReference reference;
                    reference = FirebaseDatabase.getInstance().getReference();

                    reference.child("advocate").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                            Log.d("ENimesh", "value = " + snapshot.getValue());
                            Map map = (Map) snapshot.getValue();
                            String dbEmail = (String) map.get("Email");
                            String dbPassword = String.valueOf(map.get("Password"));
//                            Log.d("ENimesh", "Email matched = " + dbEmail);
//                            Log.d("ENimesh", "Email matched = " + dbPassword);

                            if (emails.equals(dbEmail) && passwords.equals(dbPassword)) {
//                                Log.d("ENimesh", "Email matched = " + dbEmail);
                                if (map.get("Approved").equals("1")) {
                                    Toast.makeText(MainActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                                    lodingPB.setVisibility(View.INVISIBLE);
                                    Intent goToLogin = new Intent(MainActivity.this,advocate.class);
                                    goToLogin.putExtra("email",dbEmail);
                                    startActivity(goToLogin);

                                } else if(map.get("Approved").equals("0")) {
                                    lodingPB.setVisibility(View.INVISIBLE);
                                    createNotificationNotApproved();
                                }else{
                                    lodingPB.setVisibility(View.INVISIBLE);
                                    createNotificationNotRejected();
                                }

                            }else{
                                lodingPB.setVisibility(View.INVISIBLE);
//                                if(!emails.equals(dbEmail)){
//                                    Toast.makeText(MainActivity.this, "Wrong Email", Toast.LENGTH_SHORT).show();
//                                }else{
//                                    Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
//                                }
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            lodingPB.setVisibility(View.VISIBLE);
//                            Log.d("ENimesh", "value = " + snapshot.getValue());
                            Map map = (Map) snapshot.getValue();
                            String dbEmail = (String) map.get("Email");
                            String dbPassword = String.valueOf(map.get("Password"));
//                            Log.d("ENimesh", "Email matched = " + dbEmail);
//                            Log.d("ENimesh", "Email matched = " + dbPassword);

                            if (emails.equals(dbEmail) && passwords.equals(dbPassword)) {
//                                Log.d("ENimesh", "Email matched = " + dbEmail);
                                if (map.get("Approved").equals("1")) {
                                    Toast.makeText(MainActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                                    lodingPB.setVisibility(View.INVISIBLE);
                                    Intent goToLogin = new Intent(MainActivity.this,advocate.class);
                                    goToLogin.putExtra("email",dbEmail);
                                    startActivity(goToLogin);

                                } else if(map.get("Approved").equals("0")) {
                                    lodingPB.setVisibility(View.INVISIBLE);
                                    createNotificationNotApproved();
                                }else{
                                    lodingPB.setVisibility(View.INVISIBLE);
                                    createNotificationNotRejected();
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
                            lodingPB.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public boolean EmailValidation(String email) {
        String emailinput = email;
        if (!emailinput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailinput).matches()) {
            return true;
        } else {

            return false;
        }
    }
    //    Code for verify email Ends------------------------------------------------------------

    //    Code for validation Password starts---------------------------------------------------
    public boolean passwordValidation(String password) {
        String passwordInput = password;
        if (!passwordInput.isEmpty() && passwordInput.length() > 6) {
            return true;
        } else {
            return false;
        }
    }
    //    Creating Notification
    private void createNotificationChannel() {
        String CHANNEL_ID = "com.nimeshkadecha.e_poker";

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Account Verification";
            String description = "Status of your request";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotificationNotApproved() {
        Random r = new Random();
        int id = r.nextInt(99);
        String CHANNEL_ID = "com.nimeshkadecha.e_poker";
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.toolbarlogo).setContentTitle("Account is not verified").setContentText("your account is not confirmed by admin,\n try again after sometime").setPriority(NotificationCompat.PRIORITY_MAX)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent).setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());
    }

    private void createNotificationNotRejected() {
        Random r = new Random();
        int id = r.nextInt(99);
        String CHANNEL_ID = "com.nimeshkadecha.e_poker";
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.toolbarlogo).setContentTitle("Account is Rejected").setContentText("your account is Rejected").setPriority(NotificationCompat.PRIORITY_MAX)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent).setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());
    }
}