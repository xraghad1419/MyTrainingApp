package com.example.mytraining;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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

public class Admin_view_Profile extends AppCompatActivity {
    /* Here we define our variables that we will link it with design variables
       Here we define database reference to store our database */

    SharedPreferences sharedPreferences;

    ArrayList <Organization> list = new ArrayList<>();

    // define attributes
    DatabaseReference ref;
    RecyclerView recyclerView;
    private Adapter_Admin_Profile.RecycleViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.rv_admin_profile);
        recyclerView =findViewById(R.id.rv_a_profile);

        EA_Admin_Profile emptyAdapter = new EA_Admin_Profile();
        recyclerView.setAdapter(emptyAdapter);


        // Define reference in DB, and attach attributes with design attributes
        ref = FirebaseDatabase.getInstance().getReference().child("Organizations");

        setOnClickListner();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setOnClickListner() {
        listener = new Adapter_Admin_Profile.RecycleViewClickListener(){
            @Override
            public void onClick(View v, int position) {
                // راح تروح على الشركة الي في الكليك ليسنر عشان شو للداتا
                sharedPreferences = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("oName",list.get(position).getName());
                editor.putString("oVision",list.get(position).getVision());
                editor.putString("oBrief",list.get(position).getBrief());
                editor.putString("oEmail",list.get(position).getEmail());
                editor.putString("oTwitter",list.get(position).getTwitter());
                editor.putString("oLinkedin",list.get(position).getLinkedin());
                editor.putString("oTelephone",list.get(position).getTelephoneNumber());
                editor.apply();
                Intent intent = new Intent(getApplicationContext(),Org_info.class);
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
                           list.add(ds.getValue(Organization.class));
                       }
                       Adapter_Admin_Profile adapterClass = new Adapter_Admin_Profile(list,listener);
                      recyclerView.setAdapter(adapterClass);

                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {
                   Toast.makeText(Admin_view_Profile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
               }
           });
        }

    }

}
