package com.example.mytraining;

import android.content.Context;
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

public class Applied_Page extends AppCompatActivity {

    private static final String TAG = "Applied_Page";
    private SharedPreferences app_id_to_tr;

    //Nav bar
    BottomNavigationView bottomNavigationView;

    ArrayList<TrainingOffer> list = new ArrayList<>();
    RecyclerView recyclerView;
    private Applied_Adapter.RecycleViewClickListener listener;
    DatabaseReference Applied_DB;
    DatabaseReference Tr_DB;
    DatabaseReference App_DB;
    ArrayList<TrainingOffer> mylist;


    String app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        String app__name = s.getString("Applicant_page", "DefaultValue");

        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.applied_page);

        recyclerView =findViewById(R.id.applied);

        app_id_to_tr = PreferenceManager.getDefaultSharedPreferences(this);

        // Nav bar
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.applied);

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
                        startActivity(new Intent(getApplicationContext(),Reviews_Page.class));
                        overridePendingTransition(0,0);
                        return;
                }
                return;
            }
        });



        EmptyAdapter_Applied emptyAdapter_applied = new EmptyAdapter_Applied();
        recyclerView.setAdapter(emptyAdapter_applied);

        App_DB = FirebaseDatabase.getInstance().getReference().child("Applicants");
        Applied_DB = FirebaseDatabase.getInstance().getReference().child("Applied_offers");
        Tr_DB = FirebaseDatabase.getInstance().getReference().child("TrainingOffers");

        setOnClickListner();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setOnClickListner() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id_app = sp.getString("Applicant_page", "DefaultValue");

        listener = new Applied_Adapter.RecycleViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                app_id_to_tr.edit().putString(TAG, id_app).apply();
                Intent intent = new Intent(getApplicationContext(), Applied_Training_Details.class);
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

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id = sp.getString("Applicant_page", "DefaultValue");


        if(list!=null){
            list.clear();
        App_DB.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               app_name = snapshot.child("fname").getValue().toString();
               System.out.println("-------------------------------------------------------------------"+app_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            Applied_DB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        for(DataSnapshot ds: snapshot.getChildren())
                        {
                            if(ds.child("applicantID").getValue().toString().equals(id)){
                                String id_applied_offer = ds.child("trainingID").getValue().toString();
                                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+id_applied_offer);

                                Tr_DB.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot dss: snapshot.getChildren()){
                                           if(dss.child("id").getValue().toString().equals(id_applied_offer)){
                                               System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%      added        %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

                                               list.add(dss.getValue(TrainingOffer.class));
                                           }
                                        }
                                        Applied_Adapter applied_adapter = new Applied_Adapter(list,listener,id,app_name);
                                        recyclerView.setAdapter(applied_adapter);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Applied_Page.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}


