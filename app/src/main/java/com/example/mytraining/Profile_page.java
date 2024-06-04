package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_page extends AppCompatActivity {
    TextView Org_name,Vision,Brief;
    ImageView tw,email,linkedIn,telephone;

    DatabaseReference tr_DB;
    DatabaseReference org_DB;



    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        Org_name=findViewById(R.id.Company_name);
        Vision=findViewById(R.id.Vision_info);
        Brief=findViewById(R.id.Brief_info);
        tw=findViewById(R.id.twitter_info);
        email=findViewById(R.id.email_info);
        linkedIn=findViewById(R.id.linkedin_info);
        telephone=findViewById(R.id.telephone_info);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Applicant_page.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.profile:
                        //offerID.edit().putString(TAG,offer_ID).apply();
                        startActivity(new Intent(getApplicationContext(),Profile_page.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.review:
                        //offerID.edit().putString(TAG,offer_ID).apply();
                        startActivity(new Intent(getApplicationContext(),Reviews_Page.class));
                        overridePendingTransition(0,0);
                        return;
                }
                return;
            }
        });

        tr_DB = FirebaseDatabase.getInstance().getReference().child("TrainingOffers");
        org_DB = FirebaseDatabase.getInstance().getReference().child("Organizations");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id_offer= sp.getString("Training_Details", "DefaultValue");

        tr_DB.child(id_offer).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String org_id = snapshot.child("organizationID").getValue().toString();
                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$        $$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+org_id);

                    org_DB.child(org_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Org_name.setText(snapshot.child("name").getValue(String.class));
                            Vision.setText(snapshot.child("vision").getValue(String.class));
                            Brief.setText(snapshot.child("brief").getValue(String.class));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile_page.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}