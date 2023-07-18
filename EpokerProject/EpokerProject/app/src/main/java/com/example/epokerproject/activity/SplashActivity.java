package com.example.epokerproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.epokerproject.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startActivity();
    }

    private void startActivity(){

        binding.textsplashscreen.animate().translationX(900).setDuration(900).setStartDelay(3000);

        Thread thread = new Thread(){

            public void run()

            {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }
            }
        };
        thread.start();
    }

}