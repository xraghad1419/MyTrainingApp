package com.example.mytraining;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class offer_page extends AppCompatActivity {

    private static final String TAG = "offer_page";
    private SharedPreferences app_id_to_tr;
    private SharedPreferences offer_id;

    //Nav bar
    BottomNavigationView bottomNavigationView;

    ArrayList<TrainingOffer> list = new ArrayList<>();
    RecyclerView recyclerView;
    private OffersAdapter.RecycleViewClickListener listener;
    SearchView searchView;
    DatabaseReference ref;

    ArrayList<TrainingOffer> mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.offer_page);

        recyclerView =findViewById(R.id.rv_Offers);

        app_id_to_tr = PreferenceManager.getDefaultSharedPreferences(this);

        offer_id = PreferenceManager.getDefaultSharedPreferences(this);


        // Nav bar
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.offer);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Applicant_page.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.offer:
                        startActivity(new Intent(getApplicationContext(),offer_page.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.recommended:
                        startActivity(new Intent(getApplicationContext(),Recomended_Page.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.applied:
                        startActivity(new Intent(getApplicationContext(),Applied_Page.class));
                        overridePendingTransition(0,0);
                        return;
                }
                return;


            }
        });



        EmptyAdapter2 emptyAdapter = new EmptyAdapter2();
        recyclerView.setAdapter(emptyAdapter);

        ref = FirebaseDatabase.getInstance().getReference().child("TrainingOffers");
        searchView = findViewById(R.id.searchOffer);

        setOnClickListner();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setOnClickListner() {
        SharedPreferences sss = PreferenceManager.getDefaultSharedPreferences(this);
        String id_app = sss.getString("Applicant_page", "DefaultValue");

        listener = new OffersAdapter.RecycleViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                //app_id_to_tr.edit().putString(TAG, id_app).apply();
                offer_id.edit().putString(TAG, list.get(position).getId()).apply();
                Intent intent = new Intent(getApplicationContext(), Training_Details.class);
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
    protected void onStart() {
        super.onStart();
        if(ref != null) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {

                        for(DataSnapshot ds: snapshot.getChildren())
                        {
                            list.add(ds.getValue(TrainingOffer.class));
                        }
                        OffersAdapter offersAdapter = new OffersAdapter(list,listener);
                        recyclerView.setAdapter(offersAdapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(offer_page.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;}
            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
    }

    private void search(String str) {
        mylist = new ArrayList<>();
        for(TrainingOffer object: list)
        {
            if(object.getOfferName().toLowerCase().contains(str.toLowerCase())){
                mylist.add(object);
            }
        }
        OffersAdapter offersAdapter = new OffersAdapter(mylist,listener);
        recyclerView.setAdapter(offersAdapter);
    }

}
