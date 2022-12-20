package com.nimeshkadecha.e_poker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private TextView register;

    private ProgressBar lodingPB;

    FirebaseFirestore db;

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

//        Finding email and password
        email = findViewById(R.id.Lemail);
        password = findViewById(R.id.Lpassword);

//        Register textview
        login = findViewById(R.id.Llogin);
        register = findViewById(R.id.Lregister);

//        Loading screen
        lodingPB = findViewById(R.id.Ploding);

//        Firestore database instance
        db = FirebaseFirestore.getInstance();

//        On click listener for Register text view and goin t oregister activity
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToRegister = new Intent(MainActivity.this, Register.class);

                startActivity(goToRegister);
            }
        });

//        On click Login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lodingPB.setVisibility(View.VISIBLE);
//                Checking internet is available or not
                boolean checkNet = checkConnection();
                if (checkNet){
//                    if internet is available then finding strings
                    String emails = email.getText().toString();
                    String passwords = password.getText().toString();
                    //TODO : add SQLite database so that now when user is approved they get notified that request is either approved or reject ::User sqlite to store register information ::: then use querySnapshotListener to fetch data if approved == 1 and lic == the one store in sqlite then create notification

//                    If user name and password are match then go to ADMIN ACTIVITY
                    if (emails.equals("Admin") && passwords.equals("123")) {
                        lodingPB.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(MainActivity.this, Admin.class);
                        startActivity(intent);
                    } else {
//                        Else check User email and password
                        boolean ev = EmailValidation(emails); // validating email by calling method
                        boolean pv = passwordValidation(passwords); // validating Password by calling method

//                        If both are ture then execut this method
                        if (ev && pv) {
//                            advocate data is stored in
                            /*
                                Format how data is stored in FIRESTORE

                                Collection : - advocate
                                document : - (licence number of advocate ) to prevent overWriting
                                field   :- Approved : - (status)
                                        :- Email :- (Email)
                                        :- Licence :- (Licence-number)
                                        :- Mobile :- (Mobile number)
                                        :- Name :- (Name)
                                        :- Password :- (Password)

                             */
//                            Going to advocate collection
                            db.collection("advocate")
//                                    Where condition to find EMAIL of that user
                                    .whereEqualTo("Email", emails)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
//                                                If we find that EMAIL then
                                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                                    Check APPROVED field
                                                    String test = String.valueOf(document.get("Approved"));
//                                                    Match Email and password for verification
                                                    if (emails.equals(document.getString("Email")) && passwords.equals(document.getString("Password"))) {
                                                        if (test.equals("1")) {
                                                            lodingPB.setVisibility(View.INVISIBLE);
//                                                            If approved then it let log in else create notification
                                                            Toast.makeText(MainActivity.this, "Successfull Login", Toast.LENGTH_SHORT).show();
                                                            Intent advocateHome = new Intent(MainActivity.this,advocate.class);
                                                            advocateHome.putExtra("user",document.getString("Email"));
                                                            startActivity(advocateHome);
                                                        } else {
//                                                            Calling notification code to generate notification
                                                            createNotification();
                                                        }
                                                    }else{
                                                        lodingPB.setVisibility(View.INVISIBLE);
//                                                        If password is worng
                                                        Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            } else {
                                                lodingPB.setVisibility(View.INVISIBLE);
//                                                if complete listener failed
                                                Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else {
                            lodingPB.setVisibility(View.INVISIBLE);
//                            if email or password method return flalse the check this
                            if(emails.isEmpty() && passwords.isEmpty()){
//                                If both are empty
                                Toast.makeText(MainActivity.this, "Enter E-mail and Password", Toast.LENGTH_SHORT).show();
                            }else if (!ev){
                                Toast.makeText(MainActivity.this, "Invalid E-mail", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }else{
                    lodingPB.setVisibility(View.INVISIBLE);
//                    If there is not internet then show this
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//    Validating EMAIL
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

    //    Creating Notification
    private void createNotificationChannel() {
        String CHANNEL_ID = "com.nimeshkadecha.e_poker";

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Account Verification";
            String description = "your account is not confirmed by admin,\n try again after sometime";
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
        Random r = new Random();
        int id = r.nextInt(99);
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
        notificationManager.notify(id, builder.build());
    }
}