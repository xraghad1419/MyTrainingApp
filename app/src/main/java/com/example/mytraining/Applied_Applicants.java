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

public class Applied_Applicants extends AppCompatActivity {

    ArrayList<Applicant> list = new ArrayList<>();
    // define attributes
    DatabaseReference app_DB;
    DatabaseReference applied_DB;

    String a;


    RecyclerView recyclerView;
    private Adapter_Applied_CV.RecycleViewClickListener listener;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.applied_applicants);

        recyclerView =findViewById(R.id.rv);

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

                        startActivity(new Intent(getApplicationContext(),Offers_View.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.applicant:
                        startActivity(new Intent(getApplicationContext(), Applied_Applicants.class));
                        overridePendingTransition(0,0);
                        return;

                }
                return;
            }
        });

        EmptyAdapter_view_applied_applicant emptyAdapter = new EmptyAdapter_view_applied_applicant();
        recyclerView.setAdapter(emptyAdapter);


        // Define reference in DB, and attach attributes with design attributes
        app_DB = FirebaseDatabase.getInstance().getReference().child("Applicants");
        applied_DB = FirebaseDatabase.getInstance().getReference().child("Applied_offers");
        setOnClickListner();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Use intent to retrieve information of applicant from Sign in by (intent = putExtra("appInfo")
        Intent intent = getIntent();
        Applicant applicant = intent.getParcelableExtra("appInfo");
        String ID = intent.getStringExtra("appID");

    }

    private void setOnClickListner() {
        listener = new Adapter_Applied_CV.RecycleViewClickListener(){
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

    @Override
    protected void onStart() {
        super.onStart();


        Intent i = getIntent();
        String offerID = i.getStringExtra("offer_ID");

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%      offerID     %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+ offerID);
        if(list!=null) {
            list.clear();
            applied_DB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%      For loop      %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                        String offer = ds.child("trainingID").getValue().toString();

                        if (offer.equals(offerID)) {
                            String app_id = ds.child("applicantID").getValue().toString();
                            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%     store app id       %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + app_id);

                            app_DB.child(app_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    list.add(snapshot.getValue(Applicant.class));
                                    System.out.println("*********************************  Added list  ************************************" + list);

                                    Adapter_Applied_CV adapterClass = new Adapter_Applied_CV(list, listener);
                                    recyclerView.setAdapter(adapterClass);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Applied_Applicants.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
