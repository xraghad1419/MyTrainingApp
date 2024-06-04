package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Offers_View extends AppCompatActivity {

    TextView errorMsg;

    Context m;

    ArrayList<TrainingOffer> list;
    RecyclerView recyclerView;
    private View_Offers_Adapter.RecycleViewClickListener listener;

    ImageButton a;

    DatabaseReference ref;

    BottomNavigationView bottomNavigationView;
    private static final String TAG = "Offers_View";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_view);
        errorMsg = findViewById(R.id.textView11);
        a = findViewById(R.id.view_app);

        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        String Org_ID = s.getString("Org_Page", "DefaultValue");

        ref = FirebaseDatabase.getInstance().getReference().child("TrainingOffers");
        recyclerView =findViewById(R.id.rv_allOffers);
        setOnClickListner();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Intent intent = getIntent();
        //String OrgID = intent.getStringExtra("oID");
        // Nav bar
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.offer);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Org_Page.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.offer:
                        startActivity(new Intent(getApplicationContext(),Offers_View.class));
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

    }

    private void setOnClickListner() {
        listener = new View_Offers_Adapter.RecycleViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                 Intent intent = new Intent(getApplicationContext(), Training_Details_By_Org.class);
                 intent.putExtra("offerName",list.get(position).getOfferName());
                 intent.putExtra("offerNationality",list.get(position).getRequiredNationality());
                 intent.putExtra("offerGPA",list.get(position).getRequiredGPA());
                 intent.putExtra("offerCity",list.get(position).getCity());
                 intent.putExtra("offerStartD",list.get(position).getStartDate());
                 intent.putExtra("offerEndD",list.get(position).getEndDate());
                 intent.putExtra("offerDesc",list.get(position).getOfferDescription());
                 intent.putExtra("offerMajor",list.get(position).getMajor());
                 intent.putExtra("offerBenefits",list.get(position).getBenefits());
                 intent.putExtra("offerEligible",list.get(position).getEligibility());
                 intent.putExtra("offerLink",list.get(position).getLink());
                 startActivity(intent);
            }
        };
    }

    @Override
    public void onStart () {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        String Org_ID = s.getString("Org_Page", "DefaultValue");

        super.onStart();
        if (ref != null) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Intent intent = getIntent();
                    //String OrgID = intent.getStringExtra("orgID");

                    if (snapshot.exists()) {
                        list = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String oId = ds.child("organizationID").getValue().toString();

                            if(oId.equals(Org_ID)) {
                                list.add(ds.getValue(TrainingOffer.class));
                                System.out.print("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+list);
                            }
                            else{
                               //errorMsg.setText("There are no offers!");
                            }
                        }

                        View_Offers_Adapter view_offers_adapter = new View_Offers_Adapter(list,listener);
                        recyclerView.setAdapter(view_offers_adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Offers_View.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

