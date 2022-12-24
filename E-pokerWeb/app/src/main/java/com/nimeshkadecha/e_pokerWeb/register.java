package com.nimeshkadecha.e_pokerWeb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    private EditText name, email, password, mobile, licence;
    private TextView ALogin;
    private Button Register;

    private ProgressBar lodingPB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lodingPB = findViewById(R.id.Plodingr);

//        Fetching all info
        name = findViewById(R.id.rName);
        email = findViewById(R.id.remail);
        password = findViewById(R.id.rpassword);
        mobile = findViewById(R.id.rcontactNumber);
        licence = findViewById(R.id.rlic);
        Register = findViewById(R.id.register);

        ALogin = findViewById(R.id.AlreadyUserTV);

        //        managing textview for user who has account
        ALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToLogin = new Intent(register.this, MainActivity.class);
                startActivity(goToLogin);
                finish();
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameS = name.getText().toString();
                String emailS = email.getText().toString();
                String passwordS = password.getText().toString();
                String mobileS = mobile.getText().toString();
                String licenceS = licence.getText().toString();

                DatabaseReference reference;
                reference = FirebaseDatabase.getInstance().getReference();

                //                Checking all conditions
                if (nameS.length() == 0) {
                    lodingPB.setVisibility(View.INVISIBLE);
                    Toast.makeText(register.this, "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (emailS.length() == 0) {
                    lodingPB.setVisibility(View.INVISIBLE);
                    Toast.makeText(register.this, "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (passwordS.length() == 0) {
                    lodingPB.setVisibility(View.INVISIBLE);
                    Toast.makeText(register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else if (mobileS.length() == 0) {
                    lodingPB.setVisibility(View.INVISIBLE);
                    Toast.makeText(register.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (licenceS.length() == 0) {
                    lodingPB.setVisibility(View.INVISIBLE);
                    Toast.makeText(register.this, "Enter Licence Number", Toast.LENGTH_SHORT).show();
                } else {
//                    Valadition methods
                    boolean ev = EmailValidation(emailS);
                    boolean pv = passwordValidation(passwordS);
                    if (!ev) {
                        lodingPB.setVisibility(View.INVISIBLE);
                        Toast.makeText(register.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    } else if (!pv) {
                        lodingPB.setVisibility(View.INVISIBLE);
                        Toast.makeText(register.this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
                    }
                    if (ev && pv) {
                        reference.child("advocate").child(licenceS).child("Approved").setValue(0);
                        reference.child("advocate").child(licenceS).child("Email").setValue(emailS);
                        reference.child("advocate").child(licenceS).child("Licence").setValue(licenceS);
                        reference.child("advocate").child(licenceS).child("Mobile").setValue(mobileS);
                        reference.child("advocate").child(licenceS).child("Name").setValue(nameS);
                        reference.child("advocate").child(licenceS).child("Password").setValue(passwordS);
                        Toast.makeText(register.this, "Register request sent successfully", Toast.LENGTH_SHORT).show();
                        Intent goToLogin = new Intent(register.this, MainActivity.class);
                        startActivity(goToLogin);
                        finish();
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

    //    Code for validation Password starts---------------------------------------------------
    public boolean passwordValidation(String password) {
        String passwordInput = password;
        if (!passwordInput.isEmpty() && passwordInput.length() > 6) {
            return true;
        } else {
            return false;
        }
    }
}