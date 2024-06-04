package com.example.mytraining;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Training_Details_By_Org extends AppCompatActivity {

    TextView OfferName, GPA, Nationality, City, StartDate, EndDate, Major, Eligibility, Benefits, Description,Link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_org_offers);
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
        Link = findViewById(R.id.link_details);

        // Nav bar
        Bundle extras_offers = getIntent().getExtras();

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
        Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("#####################################################################################################################################" + offer_Link);
                Uri uri = Uri.parse(offer_Link);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });
    }

    private void gotoUrl(String link) {
        Uri uri = Uri.parse(link);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}