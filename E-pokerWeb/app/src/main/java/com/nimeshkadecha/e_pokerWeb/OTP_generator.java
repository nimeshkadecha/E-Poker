package com.nimeshkadecha.e_pokerWeb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTP_generator extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private EditText otp;

    private Button verifyy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_generator);

        Bundle otpp = getIntent().getExtras();
        String OTP = otpp.getString("OTP");

        verifyy = findViewById(R.id.Verify);
        verifyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = findViewById(R.id.OTP);
                String otpInput = otp.getText().toString().trim();
                boolean OTP_V = OTPValidate(otpInput);
                if (OTP_V) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP, otpInput);

                    mAuth.signInWithCredential(credential)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Bundle bundle = getIntent().getExtras();
                                    String number = bundle.getString("number");
                                        Intent GoToResetPassword = new Intent(OTP_generator.this, resetPassword.class);
                                        //                Fowarding number to intent reset password
                                        GoToResetPassword.putExtra("number", number);

                                        startActivity(GoToResetPassword);
                                        finish();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OTP_generator.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                                }
                            });
//                    if (otp.getText().toString().equals(OTP)) {
//                        Intent GoToResetPassword = new Intent(OTP_Generator.this, resetPassword.class);
//
//                        Bundle bundle = getIntent().getExtras();
//                        String number = bundle.getString("number");
////                Fowarding number to intent reset password
//                        GoToResetPassword.putExtra("number", number);
//
//                        startActivity(GoToResetPassword);
//                        finish();
//                    } else {
//                        Toast.makeText(OTP_Generator.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    Toast.makeText(OTP_generator.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean OTPValidate(String otpInput) {
        if (otpInput.length() < 6) {
            return false;
        } else {
            return true;
        }
    }
}