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
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private TextView register;
    //    Keep user LOGED IN
    public static final String SHARED_PREFS = "sharedPrefs";
    private ProgressBar lodingPB;

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
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        int netNotificationCount = 0;

        boolean net = checkConnection();
        if(net){
            alreadyLogin();
        }else if(netNotificationCount == 0){
            Toast.makeText(this, "Please Check your Internet connection", Toast.LENGTH_SHORT).show();
            netNotificationCount++;
        }

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
                boolean net = checkConnection();
                if(net){

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

                                if (emails.equals(dbEmail) ) {
                                    if(passwords.equals(dbPassword)){
                                        int approvel =Integer.parseInt(String.valueOf(map.get("Approved")));
                                        //                                Log.d("ENimesh", "Email matched = " + dbEmail);
                                        if (approvel == 1) {
                                            Toast.makeText(MainActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                                            lodingPB.setVisibility(View.INVISIBLE);
                                            Intent goToLogin = new Intent(MainActivity.this,advocate.class);
                                            goToLogin.putExtra("email",dbEmail);
                                            String licence = String.valueOf(map.get("Licence"));
                                            goToLogin.putExtra("licence",licence);

                                            SharedPreferences sp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putString("Login","true");
                                            editor.putString("UserName",dbEmail);
                                            editor.putString("Licence",licence);
                                            editor.apply();

                                            startActivity(goToLogin);
                                            finish();

                                        } else if(approvel == 0) {
                                            lodingPB.setVisibility(View.INVISIBLE);
                                            createNotificationNotApproved();
                                        }else{
                                            lodingPB.setVisibility(View.INVISIBLE);
                                            createNotificationNotRejected();
                                        }
                                    }else{
                                        lodingPB.setVisibility(View.INVISIBLE);
                                        Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    lodingPB.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                            Log.d("ENimesh", "value = " + snapshot.getValue());
                                Map map = (Map) snapshot.getValue();
                                String dbEmail = (String) map.get("Email");
                                String dbPassword = String.valueOf(map.get("Password"));
//                            Log.d("ENimesh", "Email matched = " + dbEmail);
//                            Log.d("ENimesh", "Email matched = " + dbPassword);

                                if (emails.equals(dbEmail) ) {
                                    if(passwords.equals(dbPassword)){
                                        int approvel =Integer.parseInt(String.valueOf(map.get("Approved")));
                                        //                                Log.d("ENimesh", "Email matched = " + dbEmail);
                                        if (approvel == 1) {
                                            Toast.makeText(MainActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                                            lodingPB.setVisibility(View.INVISIBLE);
                                            Intent goToLogin = new Intent(MainActivity.this,advocate.class);
                                            goToLogin.putExtra("email",dbEmail);
                                            String licence = String.valueOf(map.get("Licence"));
                                            goToLogin.putExtra("licence",licence);

                                            SharedPreferences sp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putString("Login","true");
                                            editor.putString("UserName",dbEmail);
                                            editor.putString("Licence",licence);
                                            editor.apply();

                                            startActivity(goToLogin);
                                            finish();

                                        } else if(approvel == 0) {
                                            lodingPB.setVisibility(View.INVISIBLE);
                                            createNotificationNotApproved();
                                        }else{
                                            lodingPB.setVisibility(View.INVISIBLE);
                                            createNotificationNotRejected();
                                        }
                                    }else{
                                        lodingPB.setVisibility(View.INVISIBLE);
                                        Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    lodingPB.setVisibility(View.INVISIBLE);
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
//                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        lodingPB.setVisibility(View.INVISIBLE);
                        if(emails.length() == 0){
                            Toast.makeText(MainActivity.this, "Please Enter E-Mail", Toast.LENGTH_SHORT).show();
                        }
                        else if(passwords.length() == 0){
                            Toast.makeText(MainActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                        }else if(!ev){
                            Toast.makeText(MainActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                        }else if(!pv){
                            Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    lodingPB.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void alreadyLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String checkLogin = sharedPreferences.getString("Login", "");
        String username = sharedPreferences.getString("UserName","");
        String Licence = sharedPreferences.getString("Licence","");
        if(Objects.equals(checkLogin, "true")) {
            Intent goToLogin = new Intent(MainActivity.this,advocate.class);
            goToLogin.putExtra("email",username);
            goToLogin.putExtra("licence",Licence);
            startActivity(goToLogin);
            finish();
        }
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
        String CHANNEL_ID = "com.nimeshkadecha.e_pokerWeb";

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
        String CHANNEL_ID = "com.nimeshkadecha.e_pokerWeb";
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
        String CHANNEL_ID = "com.nimeshkadecha.e_pokerWeb";
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