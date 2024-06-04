package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class Training_Details extends AppCompatActivity {
    TextView OfferName, GPA, Nationality, City, StartDate, EndDate, Major, Eligibility, Benefits, Description;
    Button Apply;
    BottomNavigationView bottomNavigationView;
    DatabaseReference applied_offers;

    String applied_offer_id;

    private static final String TAG = "Training_Details";
    //نرسل هذا الاي دي للرفيو بيج عشان ترجع الاي دي حسب الشركة
    private SharedPreferences offerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_details);
        OfferName = findViewById(R.id.Name_offer);
        Nationality = findViewById(R.id.Nationality);
        GPA = findViewById(R.id.GPA_offer);
        City = findViewById(R.id.city);
        StartDate = findViewById(R.id.start_D);
        EndDate = findViewById(R.id.End_Date);
        Major = findViewById(R.id.major_offer);
        Eligibility = findViewById(R.id.tvEligibility);
        Benefits = findViewById(R.id.benefites);
        Description = findViewById(R.id.textDescription);
        Apply = findViewById(R.id.button);

        applied_offers = FirebaseDatabase.getInstance().getReference().child("Applied_offers");

        offerID = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id_applicant = sp.getString("Applicant_page", "DefaultValue");

        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        String id_offer = s.getString("offer_page", "DefaultValue");

        Bundle extras_offers = getIntent().getExtras();

        //String offer_ID = extras_offers.getString("offerID");

        // Nav bar
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

                    case R.id.profile:
                        offerID.edit().putString(TAG,id_offer).apply();
                        startActivity(new Intent(getApplicationContext(),Profile_page.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.review:
                        offerID.edit().putString(TAG,id_offer).apply();
                        startActivity(new Intent(getApplicationContext(),Reviews_Page.class));
                        overridePendingTransition(0,0);
                        return;
                }
                return;
            }
        });


        String offer_Name = extras_offers.getString("offerName");
        OfferName.setText(offer_Name);

        String offer_Nationality = extras_offers.getString("offerNationality");
        Nationality.setText(offer_Nationality);

        String offer_GPA = extras_offers.getString("offerGPA");
        GPA.setText(offer_GPA);

        String offer_City = extras_offers.getString("offerCity");
        City.setText(offer_City);

        String offer_SD = extras_offers.getString("offerStartD");
        StartDate.setText(offer_SD);

        String offer_ED = extras_offers.getString("offerEndD");
        EndDate.setText(offer_ED);

        String offer_Desc = extras_offers.getString("offerDesc");
        Description.setText(offer_Desc);

        String offer_Major = extras_offers.getString("offerMajor");
        Major.setText(offer_Major);

        String offer_Benefits = extras_offers.getString("offerBenefits");
        Benefits.setText(offer_Benefits);

        String offer_Eligible = extras_offers.getString("offerEligible");
        Eligibility.setText(offer_Eligible);

        String offer_Link = extras_offers.getString("offerLink");

        Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(offer_Link);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));

                applied_offer_id = applied_offers.push().getKey();
                Applied_Offers applied = new Applied_Offers(applied_offer_id,id_applicant,id_offer);
                applied_offers.child(applied_offer_id).setValue(applied);
            }
        });
    }

    private void gotoUrl(String link) {
        Uri uri = Uri.parse(link);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}