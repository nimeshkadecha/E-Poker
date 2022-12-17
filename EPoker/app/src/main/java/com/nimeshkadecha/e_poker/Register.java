package com.nimeshkadecha.e_poker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText name, email, password, mobile, licence;
    private TextView ALogin;
    private Button Register;

    private FirebaseFirestore db;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.rName);
        email = findViewById(R.id.remail);
        db = FirebaseFirestore.getInstance();
        password = findViewById(R.id.rpassword);
        mobile = findViewById(R.id.rcontactNumber);
        licence = findViewById(R.id.rlic);

        ALogin = findViewById(R.id.AlreadyUserTV);

        ALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToLogin = new Intent(Register.this, MainActivity.class);
                startActivity(goToLogin);
                finish();
            }
        });

        Register = findViewById(R.id.register);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameS = name.getText().toString();
                String emailS = email.getText().toString();
                String passwordS = password.getText().toString();
                String mobileS = mobile.getText().toString();
                String licenceS = licence.getText().toString();


                if (nameS.length() == 0) {
                    Toast.makeText(Register.this, "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (emailS.length() == 0) {
                    Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (passwordS.length() == 0) {
                    Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else if (mobileS.length() == 0) {
                    Toast.makeText(Register.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (licenceS.length() == 0) {
                    Toast.makeText(Register.this, "Enter Licence Number", Toast.LENGTH_SHORT).show();
                } else {
                    if (emailS.length() != 0 && passwordS.length() != 0) {
                        boolean ev = EmailValidation(emailS);
                        boolean pv = passwordValidation(passwordS);
                        if (!ev) {
                            Toast.makeText(Register.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                        } else if (!pv) {
                            Toast.makeText(Register.this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
                        }

                        if (ev && pv) {
                            Map<String, Object> userdata = new HashMap<>();
                            userdata.put("Name", nameS);
                            userdata.put("Email", emailS);
                            userdata.put("Password", passwordS);
                            userdata.put("Mobile", mobileS);
                            userdata.put("Licence", licenceS);
                            userdata.put("Approved", 0);

                            db.collection("advocate").document(licenceS)
                                    .set(userdata)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(Register.this, "Register request sent successfully", Toast.LENGTH_SHORT).show();
                                            Intent goTOLogin = new Intent(Register.this,MainActivity.class);
                                            startActivity(goTOLogin);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Register.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
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
}