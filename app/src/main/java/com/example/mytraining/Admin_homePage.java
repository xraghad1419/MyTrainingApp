package com.example.mytraining;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Admin_homePage extends AppCompatActivity {

    CardView logout, users, applicants, organizations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_page);
        logout = findViewById(R.id.cardLogout);
        users = findViewById(R.id.cardUsers);
        applicants = findViewById(R.id.cardCV);
        organizations = findViewById(R.id.cardProfile);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Sign in page
                Intent intent = new Intent(Admin_homePage.this, SignIN.class);
                startActivity(intent);
                // Show message
                Toast.makeText(Admin_homePage.this, "Sign out Successful", Toast.LENGTH_SHORT).show();
            }
        });

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Sign in page
                Intent intent = new Intent(Admin_homePage.this, Admin_view_Users.class);
                startActivity(intent);
            }
        });

        organizations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Sign in page
                Intent intent = new Intent(Admin_homePage.this, Admin_view_Profile.class);
                startActivity(intent);
            }
        });

        applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Sign in page
                Intent intent = new Intent(Admin_homePage.this, Admin_view_CV.class);
                startActivity(intent);
            }
        });


    }
}
