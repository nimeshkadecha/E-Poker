package com.nimeshkadecha.e_poker;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private TextView register;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        email = findViewById(R.id.Lemail);
        password = findViewById(R.id.Lpassword);

        login = findViewById(R.id.Llogin);
        register = findViewById(R.id.Lregister);

        db = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToRegister = new Intent(MainActivity.this, Register.class);

                startActivity(goToRegister);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emails = email.getText().toString();
                String passwords = password.getText().toString();
                //TODO : add SQLite database so that now when user is approved they get notified that request is either approved or reject ::User sqlite to store register information ::: then use querySnapshotListener to fetch data if approved == 1 and lic == the one store in sqlite then create notification
                if (emails.equals("AdminNimesh") && passwords.equals("Nimesh123")) {
                    Intent intent = new Intent(MainActivity.this, Admin.class);
                    startActivity(intent);

                } else {
                    boolean ev = EmailValidation(emails);
                    boolean pv = passwordValidation(passwords);
                    final boolean[] loginCheck = {false};

                    if (ev && pv) {
                        db.collection("advocate")
                                .whereEqualTo("Email", emails)
//                                .whereEqualTo("password",passwords)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
//                                            Toast.makeText(MainActivity.this, "DATA fetched", Toast.LENGTH_SHORT).show();
                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                Log.d("ENimesh", document.getData()+"&& id = "+document.getId());
//                                                Log.d("ENimesh", "Email == " + document.getString("Email"));
//                                                Log.d("ENimesh", "Email == " + document.getString("Password"));
//                                                Log.d("ENimesh", "Email == "+document.getData());
                                                String test = String.valueOf(document.get("Approved"));
//                                                Log.d("ENimesh", "tapproved !!!!!!!!!!! == "+document.get("Approved"));
//                                                Log.d("ENimesh", "test == "+test);

                                                if (emails.equals(document.getString("Email")) && passwords.equals(document.getString("Password"))) {
                                                    if (test.equals("1")) {
                                                        loginCheck[0] = true;
                                                        Toast.makeText(MainActivity.this, "Successfull Login", Toast.LENGTH_SHORT).show();
                                                        Intent advocateHome = new Intent(MainActivity.this,advocate.class);
                                                        startActivity(advocateHome);

                                                    } else {
                                                        createNotification();
                                                    }

                                                }

                                            }
//                                            Log.d("ENimesh", String.valueOf(task.getResult()));
                                        } else {
                                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Invalid Email / Password", Toast.LENGTH_SHORT).show();
                    }
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

    //    Code for validation Password starts------------------------------------------------------------
    public boolean passwordValidation(String password) {
        String passwordInput = password;
        if (!passwordInput.isEmpty() && passwordInput.length() > 6) {
            return true;
        } else {
            return false;
        }
    }

    private void createNotificationChannel() {
        String CHANNEL_ID = "com.nimeshkadecha.e_poker";

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Demo";
            String description = "Demo Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotification() {
        String CHANNEL_ID = "com.nimeshkadecha.e_poker";
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.toolbarlogo)
                .setContentTitle("Account is not verified")
                .setContentText("your account is not confirmed by admin,\n try again after sometime")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(100, builder.build());
    }
}