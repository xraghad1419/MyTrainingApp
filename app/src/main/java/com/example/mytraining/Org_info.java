package com.example.mytraining;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Org_info extends AppCompatActivity {

    TextView Org_name,Vision,Brief;
    ImageView tw,email,linkedIn,telephone;
    BottomNavigationView bottomNavigationView;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.org_info);
        Org_name=findViewById(R.id.Company_name);
        Vision=findViewById(R.id.Vision_info);
        Brief=findViewById(R.id.Brief_info);
        tw=findViewById(R.id.twitter_info);
        email=findViewById(R.id.email_info);
        linkedIn=findViewById(R.id.linkedin_info);
        telephone=findViewById(R.id.telephone_info);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Applicant_page.class));
                        overridePendingTransition(0,0);
                        return;
                }
                return;


            }
        });

        preferences = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
        // Here we get Information of organization from onClick method in applicant page


        String orgName = preferences.getString("oName","");
        Org_name.setText(orgName);

        String orgVision = preferences.getString("oVision","");
        Vision.setText(orgVision);

        String orgBrief = preferences.getString("oBrief","");
        Brief.setText(orgBrief);


        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String twitter = preferences.getString("oTwitter","");
                Uri uri = Uri.parse(twitter);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });

        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String linkedin = preferences.getString("oLinkedin","");
                Uri uri = Uri.parse(linkedin);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });

        telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String phone =  preferences.getString("oTelephone","");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone));
                startActivity(intent);
            }
        });


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = preferences.getString("oEmail","");
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra("e",email);
                startActivity(intent);



            }
        });
    }
}
