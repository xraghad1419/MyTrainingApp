package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {
    /* Here we define our variables that we will link it with design variables
       Here we define firebaseUser for authentication */
    TextView backPage;
    EditText email;
    Button resetPass;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.activity_forgot_password);
        email = findViewById(R.id.email);
        resetPass = findViewById(R.id.resetPass);
        backPage = findViewById(R.id.backnav);
        firebaseAuth = FirebaseAuth.getInstance();

        // Action of Reset Password button
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Calling method
                resetPassword();
            }
        });

        // Action of Back to sign in button
        backPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Sign in
                Intent cv = new Intent(Forgot_Password.this, SignIN.class);
                startActivity(cv);
            }
        });

    }

    public void resetPassword(){
        String emailAddress = email.getText().toString().trim();

        // Check if field is empty
        if(emailAddress.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }

        // Check if email pattern is correct (@....)
        if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            email.setError("Please enter valid email!");
            email.requestFocus();
            return;
        }
        // Send link to this email to reset password
        firebaseAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Forgot_Password.this, "We send link to your email", Toast.LENGTH_SHORT).show();

                    // Go to Sign in
                    Intent cv = new Intent(Forgot_Password.this, SignIN.class);
                    startActivity(cv);
                }
                else{
                    Toast.makeText(Forgot_Password.this, "Try again! Something wrong happened!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}