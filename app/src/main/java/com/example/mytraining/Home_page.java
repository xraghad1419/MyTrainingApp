package com.example.mytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Home_page extends AppCompatActivity {
    /* Here we define our variables that we will link it with design variables */
    TextView arraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.home_page);
        arraw = findViewById(R.id.arraw);

    arraw.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View V) {
            Intent intent = new Intent(Home_page.this, SignIN.class);
            startActivity(intent);
        }
    });

    }
}