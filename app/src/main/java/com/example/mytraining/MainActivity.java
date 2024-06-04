package com.example.mytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    /* Here we define our variables that we will link it with design variables */
    TextView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /// setContentView(R.layout.activity_main);
      //  btn=findViewById(R.id.Sin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Sign in page
               // Intent movetosign = new Intent(getApplicationContext(),SignIN.class);
              //  startActivity(movetosign);


            }
        });
    }
}