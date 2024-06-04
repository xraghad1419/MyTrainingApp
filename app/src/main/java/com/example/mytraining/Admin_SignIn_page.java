package com.example.mytraining;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Admin_SignIn_page extends AppCompatActivity {

    Button btnLogin_admin;
    EditText etUsername, etPassword;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.admin);
        btnLogin_admin = findViewById(R.id.btnLogin_ADMIN);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword_ADMIN);

        firebaseAuth = FirebaseAuth.getInstance();

        // Action of Login button
        btnLogin_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), Admin_homePage.class);
                            startActivity(intent);
                            Toast.makeText(Admin_SignIn_page.this, "Login successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Admin_SignIn_page.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                }

            });


    }
}
