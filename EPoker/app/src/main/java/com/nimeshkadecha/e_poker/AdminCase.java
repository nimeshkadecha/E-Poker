package com.nimeshkadecha.e_poker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminCase extends AppCompatActivity {
    private Button addCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_case);
        addCase = findViewById(R.id.addCBtn);
        addCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAddcase = new Intent(AdminCase.this,Addcase.class);
                startActivity(goToAddcase);
            }
        });
    }
}