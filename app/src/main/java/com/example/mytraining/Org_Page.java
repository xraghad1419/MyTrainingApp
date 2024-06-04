package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class Org_Page extends AppCompatActivity {
    /* Here we define our variables that we will link it with design variables
       Here we define database reference to store our database */
    ImageButton  ShowProfile,logout;
    TextView welcome,orgNum;
    ImageView org;
    Button AddOffer;
    BottomNavigationView bottomNavigationView;

    DatabaseReference OrgDB = FirebaseDatabase.getInstance().getReference().child("Organizations");

    private static final String TAG = "Org_Page";
    private SharedPreferences preferencesForOffer;
    private SharedPreferences preferencesForProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.activity_org_page);
        ShowProfile=findViewById(R.id.ShowProfile);
        welcome=findViewById(R.id.textWelcome);
        orgNum=findViewById(R.id.orgname);
        org=findViewById(R.id.imageViewOrg);
        AddOffer=findViewById(R.id.buttonAddOffer);
        logout = findViewById(R.id.signout);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String valueOrg = sp.getString("SignIN", "DefaultValue");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+valueOrg);

        preferencesForOffer = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesForProfile = PreferenceManager.getDefaultSharedPreferences(this);

// Use intent to retrieve information of organization from Sign in by (intent = putExtra("orgInfo")
        Intent intent = getIntent();

        // Retrieve and store as organization object
        Organization organization = intent.getParcelableExtra("orgInfo");

        //String ID = intent.getStringExtra("oID");
        // Nav bar
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Org_Page.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.offer:

                        Intent offer = new Intent(Org_Page.this,Offers_View.class);
                        preferencesForProfile.edit().putString(TAG, valueOrg).apply();
                        //offer.putExtra("orgID",ID);
                        startActivity(offer);
                        overridePendingTransition(0,0);
                        return;

                    case R.id.applicant:
                        startActivity(new Intent(getApplicationContext(), Applicant_view.class));
                        overridePendingTransition(0,0);
                        return;
                }
                return;
            }
        });




        OrgDB.child(valueOrg).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orgNum.setText(snapshot.child("name").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        // Action of Logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Sign in page
                Intent intent = new Intent(Org_Page.this,SignIN.class);
                startActivity(intent);
                // Show message
                Toast.makeText(Org_Page.this, "Sign out Successful", Toast.LENGTH_SHORT).show();
            }
        });

       // once click on Add profile button it will move to org_profile page
        ShowProfile.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                // Go to organization profile
                preferencesForProfile.edit().putString(TAG, valueOrg).apply();
                Intent intent = new Intent(Org_Page.this,Org_profilePage.class);
                // Send putExtra with object organization that was created in line 42
                intent.putExtra("orgProfile",organization);
                //intent.putExtra("oID",ID);
                startActivity(intent);
            }
        }) ;

        AddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferencesForOffer.edit().putString(TAG, valueOrg).apply();
                Intent intent = new Intent(Org_Page.this,TrainingOfferPage.class);
                intent.putExtra("orgProfile",organization);
                //intent.putExtra("oID",ID);
                startActivity(intent);
            }
        });
    }
}