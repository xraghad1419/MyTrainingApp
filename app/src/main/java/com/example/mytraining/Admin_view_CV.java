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

public class Admin_view_CV extends AppCompatActivity {

    ArrayList<Applicant> list = new ArrayList<>();
    // define attributes
    DatabaseReference ref;
    RecyclerView recyclerView;
    private Adapter_Admin_CV.RecycleViewClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.rv_admin_cv);
        recyclerView =findViewById(R.id.rv_a_cv);

        ref = FirebaseDatabase.getInstance().getReference().child("Applicants");

        EA_Admin_CV emptyAdapter = new EA_Admin_CV();
        recyclerView.setAdapter(emptyAdapter);
        // Define reference in DB, and attach attributes with design attributes

        setOnClickListner();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Use intent to retrieve information of applicant from Sign in by (intent = putExtra("appInfo")
        Intent intent = getIntent();
        Applicant applicant = intent.getParcelableExtra("appInfo");
        String ID = intent.getStringExtra("appID");

    }

    private void setOnClickListner() {
        listener = new Adapter_Admin_CV.RecycleViewClickListener(){
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(),view_applicant_cv.class);
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
        if(list!=null){
            list.clear();
            if(ref != null) {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot ds: snapshot.getChildren())
                            {
                                list.add(ds.getValue(Applicant.class));
                            }
                            Adapter_Admin_CV adapterClass = new Adapter_Admin_CV(list,listener);
                            recyclerView.setAdapter(adapterClass);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Admin_view_CV.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&   list    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        }
    }
}
