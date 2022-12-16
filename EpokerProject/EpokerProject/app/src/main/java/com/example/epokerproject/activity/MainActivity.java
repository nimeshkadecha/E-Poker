package com.example.epokerproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.epokerproject.databinding.ActivityMainBinding;
import com.example.epokerproject.utils.Common;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        goToNextActivity();
    }

    private void goToNextActivity(){

        binding.btnAdmin.setOnClickListener(v -> nextActivity("Admin"));

        binding.btnAdvocate.setOnClickListener(v -> nextActivity("Advocate"));

        binding.btnClient.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,CaseListActivity.class));
            //finish();
        });
    }

    private void nextActivity(String userType){
        startActivity(new Intent(MainActivity.this,LoginActivity.class).putExtra(Common.USER_TYPE,userType));
    }

}