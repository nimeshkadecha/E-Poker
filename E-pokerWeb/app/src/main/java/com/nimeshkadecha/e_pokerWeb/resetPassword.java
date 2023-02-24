package com.nimeshkadecha.e_pokerWeb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class resetPassword extends AppCompatActivity {


    private EditText password, confirmPassword;

    private Button confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.ConfirmPassword);

        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Calling Function
                Confirm();
            }
        });
    }


    //    validating Password length >= 6 --------------------------------------------------------------
    private boolean PasswordValidation(EditText password, EditText confirmPassword) {
        String passwordInput = password.getText().toString().trim();
        String confirmPasswordInput = confirmPassword.getText().toString().trim();
        if (passwordInput.length() != confirmPasswordInput.length() || passwordInput.length()<6) {
            return false;
        } else {
            return true;
        }
    }


    //    RESETTING PASSWORD ---------------------------------------------------------------------------
    public void Confirm() {
        boolean VP = PasswordValidation(password, confirmPassword);
        if (VP) {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {

                DatabaseReference reference;
                reference = FirebaseDatabase.getInstance().getReference();

                Bundle bundle = getIntent().getExtras();
                String number = bundle.getString("number");

                final String[] lic = new String[1];

                reference.child("advocate").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Map map = (Map) snapshot.getValue();

                        Log.d("ENimesh", "testtt" + map.get("Licence"));
                        if(map.get("Mobile").equals(number)){
                            lic[0] = String.valueOf(map.get("Licence"));
                            Log.d("ENimesh","Licence is = " + lic[0]);
                        }

                        reference.child("advocate").child(lic[0]).child("Password").setValue(confirmPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(resetPassword.this, "Updated", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(resetPassword.this,MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(resetPassword.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
    }

}