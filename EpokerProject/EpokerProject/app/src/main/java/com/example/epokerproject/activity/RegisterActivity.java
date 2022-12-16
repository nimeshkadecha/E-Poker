package com.example.epokerproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.epokerproject.databinding.ActivityRegisterBinding;
import com.example.epokerproject.model.User;
import com.example.epokerproject.utils.Common;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private String UserType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
        UserType = getIntent().getStringExtra(Common.USER_TYPE);
        mAuth = FirebaseAuth.getInstance();

        binding.btnSignup.setOnClickListener(v -> {

            if (checkConditions()) {
                try {

                    mAuth.createUserWithEmailAndPassword(binding.tieEmail.getText().toString(), binding.tiePassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(binding.tieUser.getText().toString(),
                                                binding.tiePassword.getText().toString(),
                                                binding.tieMobile.getText().toString(),
                                                binding.tieEmail.getText().toString(),
                                                binding.tieLicence.getText().toString());

                                        FirebaseDatabase.getInstance().getReference("Users")
                                                //.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(RegisterActivity.this, "User Registered Succesfully", Toast.LENGTH_LONG).show();

                                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class).putExtra(Common.USER_TYPE,UserType));
                                                            finish();

                                                        } else {

                                                            Toast.makeText(RegisterActivity.this, "User Failed to Register ", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "User Failed to Register with email", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private boolean checkConditions() {
        boolean valid;

        if (binding.tieUser.getText().toString().isEmpty()) {
            binding.tilUser.setError("Enter User Name");
            valid = false;
        } else if (binding.tieEmail.getText().toString().isEmpty()) {
            binding.tilEmail.setError("Enter Email Address");
            binding.tilUser.setError(null);
            valid = false;
        } else if (binding.tiePassword.getText().toString().isEmpty()) {
            binding.tilPassword.setError("Enter Password");
            binding.tilUser.setError(null);
            binding.tilEmail.setError(null);
            valid = false;
        } else if (binding.tiePassword.getText().length() < 6) {
            binding.tilPassword.setError("Password must be 6 character long");
            binding.tilUser.setError(null);
            binding.tilEmail.setError(null);
            valid = false;
        } else if (binding.tieMobile.getText().toString().isEmpty()) {
            binding.tilMobile.setError("Enter Mobile Number");
            binding.tilUser.setError(null);
            binding.tilEmail.setError(null);
            binding.tilPassword.setError(null);
            valid = false;
        } else if (binding.tieLicence.getText().toString().isEmpty()) {
            binding.tilLicence.setError("Enter Licence Number");
            binding.tilUser.setError(null);
            binding.tilEmail.setError(null);
            binding.tilPassword.setError(null);
            binding.tilMobile.setError(null);
            valid = false;
        } else {
            valid = true;

            binding.tilUser.setError(null);
            binding.tilEmail.setError(null);
            binding.tilPassword.setError(null);
            binding.tilMobile.setError(null);
            binding.tilLicence.setError(null);
        }

        return valid;
    }
}