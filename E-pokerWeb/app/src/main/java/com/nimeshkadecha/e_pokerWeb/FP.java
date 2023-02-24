package com.nimeshkadecha.e_pokerWeb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class FP extends AppCompatActivity {

    private EditText number;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCAllbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fp);

        number = findViewById(R.id.contactnumber);

        mAuth = FirebaseAuth.getInstance();

    }

    private boolean numberValidation(EditText number) {
        String numberInput = number.getText().toString().trim();
        if (numberInput.length() == 10) {
            return true;
        } else {
            return false;
        }
    }


    //    Button
    public void GetOTP(View view) {
        boolean NV = numberValidation(number);
        if (NV) {
            //     remove coment from getOTP() in next line and remove intent
            // Calling Function to get OTP
            getOTP();

//             Testing Code to BY PASS OTP REQUIREMENT THERE IS FEW MORE TO COMMENT
//            Intent TestDownload = new Intent(FP.this,OTP_generator.class);
//
//            TestDownload.putExtra("number", number.getText().toString());
//
//            startActivity(TestDownload);

        } else {
            Toast.makeText(this, "Invalid Number", Toast.LENGTH_SHORT).show();
        }
    }

    private void getOTP() {
//        OTP From Firebase
        String CN = number.getText().toString();
        Log.d("ENimesh", "NUmber =" + CN);
        mCAllbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(FP.this, "Verified..?", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("ENimesh", "Error is =" + e);
                Toast.makeText(FP.this, "Failed to send OTP, Try Again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                Toast.makeText(FP.this, "Sending OTP ...", Toast.LENGTH_SHORT).show();
                Intent GETOTP = new Intent(FP.this, OTP_generator.class);

                // If Origin is from Cloud Then it Going TO download Data Else it go to Reset password

                GETOTP.putExtra("Origin", "Cloud");

                GETOTP.putExtra("number", CN);
                GETOTP.putExtra("OTP", s);
                startActivity(GETOTP);
                finish();
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91 " + CN)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCAllbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
//--------------------------------------------------------------------------------------------------
    }
}