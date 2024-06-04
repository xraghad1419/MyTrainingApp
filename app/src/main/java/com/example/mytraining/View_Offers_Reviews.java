package com.example.mytraining;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class View_Offers_Reviews extends AppCompatActivity {

    ArrayList<Review> list = new ArrayList<>();
    //define attributes
    DatabaseReference app_DB;
    DatabaseReference applied_DB;

    String a;
    DatabaseReference reviewDB = FirebaseDatabase.getInstance().getReference().child("Reviews");


    RecyclerView recyclerView;
    private Adapter_View_offers_Reviews.RecycleViewClickListener listener;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.view_offer_reviews);

        recyclerView =findViewById(R.id.offer_reviews);

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
                        startActivity(new Intent(getApplicationContext(), View_Offers_Reviews.class));
                        overridePendingTransition(0,0);
                        return;

                }
                return;
            }
        });

        EA_View_offers_Reviews emptyAdapter = new EA_View_offers_Reviews();
        recyclerView.setAdapter(emptyAdapter);


        // Define reference in DB, and attach attributes with design attributes
        app_DB = FirebaseDatabase.getInstance().getReference().child("Applicants");
        applied_DB = FirebaseDatabase.getInstance().getReference().child("Applied_offers");
        //setOnClickListner();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Use intent to retrieve information of applicant from Sign in by (intent = putExtra("appInfo")
        Intent intent = getIntent();
        Applicant applicant = intent.getParcelableExtra("appInfo");
        String ID = intent.getStringExtra("appID");

    }
/*
    private void setOnClickListner() {
        listener = new Adapter_View_offers_Reviews.RecycleViewClickListener(){
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(),Applied_CV.class);
                intent.putExtra("aName",list.get(position).getFname());
                intent.putExtra("aEmail",list.get(position).getEmail());
                intent.putExtra("aPhone",list.get(position).getPhoneNumber());
                intent.putExtra("aMajor",list.get(position).getMajor());
                intent.putExtra("aGPA",list.get(position).getGPA());
                intent.putExtra("aSkills",list.get(position).getSkills());
                intent.putExtra("aInterest",list.get(position).getInterest());
                intent.putExtra("aNationality",list.get(position).getNationality());
                intent.putExtra("aImg",list.get(position).getImageUrl());
                startActivity(intent);
            }
        };
    }

 */

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = getIntent();
        String offerID = i.getStringExtra("offer_ID");

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%      offerID     %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+ offerID);
        if(list!=null) {
            list.clear();
            reviewDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds: snapshot.getChildren()){
                        if(ds.child("trainingID").getValue().toString().equals(offerID)){
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
