package com.example.mytraining;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Reviews_Page extends AppCompatActivity {
    private static final String TAG = "Reviews_Page";

    //Nav bar
    BottomNavigationView bottomNavigationView;

    ArrayList<Review> list = new ArrayList<>();
    RecyclerView recyclerView;
    private Reviews_Adapter.RecycleViewClickListener listener;
    DatabaseReference reviewDB;
    DatabaseReference tr_DB;

    ArrayList<Review> mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reviews_page);
        recyclerView =findViewById(R.id.rv_reviews);

        // Nav bar
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.review);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Applicant_page.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile_page.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.review:
                        startActivity(new Intent(getApplicationContext(),Reviews_Page.class));
                        overridePendingTransition(0,0);
                        return;
                }
                return;

            }
        });



        EmptyAdapter_Reviews emptyAdapter = new EmptyAdapter_Reviews();
        recyclerView.setAdapter(emptyAdapter);

        reviewDB = FirebaseDatabase.getInstance().getReference().child("Reviews");
        tr_DB = FirebaseDatabase.getInstance().getReference().child("TrainingOffers");
        //setOnClickListner();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setOnClickListner() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id_app = sp.getString("Applicant_page", "DefaultValue");

        listener = new Reviews_Adapter.RecycleViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                /*
                app_id_to_tr.edit().putString(TAG, id_app).apply();
                Intent intent = new Intent(getApplicationContext(), Training_Details.class);
                intent.putExtra("offerID",list.get(position).getId());
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
*/
            }
        };
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (list != null) {
            list.clear();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            String id_offer = sp.getString("Training_Details", "DefaultValue");

            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + id_offer);

            reviewDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.child("trainingID").getValue().toString().equals(id_offer)) {
                            list.add(ds.getValue(Review.class));
                        }
                    }
                    Reviews_Adapter reviews_adapter = new Reviews_Adapter(list);
                    recyclerView.setAdapter(reviews_adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}
