package com.example.epokerproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.epokerproject.R;
import com.example.epokerproject.databinding.ActivityLoginBinding;
import com.example.epokerproject.utils.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private String UserType = "";
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView(){
        UserType = getIntent().getStringExtra(Common.USER_TYPE);
        mauth = FirebaseAuth.getInstance();

        if (UserType.equalsIgnoreCase("Admin")){
            binding.txtRegister.setVisibility(View.GONE);
            binding.txtOr.setVisibility(View.GONE);
        }else {
            binding.txtRegister.setVisibility(View.VISIBLE);
            binding.txtOr.setVisibility(View.VISIBLE);
        }

        binding.txtRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class).putExtra(Common.USER_TYPE,UserType));
            finish();
        });

        binding.btnLogin.setOnClickListener(v -> {
            if (checkConditions()){
                if (UserType.equalsIgnoreCase("Admin")){
                    if (checkAdminLogin()){
                        startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                        finish();
                    }
                }else {
                    loginUser();
                }
            }
        });
    }

    private void loginUser(){
        try {
            mauth.signInWithEmailAndPassword(binding.tieEmail.getText().toString(), binding.tiePassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "User has Successfully Login!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, CaseListActivity.class));
                        finish();
                    } else {

                        Toast.makeText(LoginActivity.this, "User Does not exist please try again with different user and password!", Toast.LENGTH_LONG).show();
                    }
                }

            });
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private boolean checkConditions(){
        boolean valid;

        if (binding.tieEmail.getText().toString().isEmpty()){
            binding.tilEmail.setError("Enter Email Address");
            valid = false;
        }else if (binding.tiePassword.getText().toString().isEmpty()){
            binding.tilPassword.setError("Enter Password");
            binding.tilEmail.setError(null);
            valid = false;
        }else if (binding.tiePassword.getText().length() < 6){
            binding.tilPassword.setError("Password must be 6 character long");
            binding.tilEmail.setError(null);
            valid = false;
        }else {
            valid = true;

            binding.tilEmail.setError(null);
            binding.tilPassword.setError(null);
        }

        return valid;
    }

    private boolean checkAdminLogin() {
        boolean valid;

        if (binding.tieEmail.getText().toString().equalsIgnoreCase("admin") && binding.tiePassword.getText().toString().equalsIgnoreCase("admin123")){
            valid = true;
        }else {
            valid = false;
            Toast.makeText(this, "Please enter valid details !", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }
}