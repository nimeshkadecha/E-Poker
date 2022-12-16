package com.nimeshkadecha.e_poker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText email,password;
    private Button login;
    private TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.Lemail);
        password = findViewById(R.id.Lpassword);

        login = findViewById(R.id.Llogin);
        register = findViewById(R.id.Lregister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToRegister = new Intent(MainActivity.this,Register.class);

                startActivity(goToRegister);
            }
        });

    }
}