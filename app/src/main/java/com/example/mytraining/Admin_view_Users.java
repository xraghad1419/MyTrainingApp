package com.example.mytraining;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_view_Users extends AppCompatActivity {

    // define attributes
    DatabaseReference ref;
    RecyclerView recyclerView;
    SearchView searchView;

    ArrayList <Account> list = new ArrayList<>();
    private Adapter_admin_users.RecycleViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.admin_users);
        recyclerView =findViewById(R.id.rv_users);
        searchView = findViewById(R.id.searchView);

        ref = FirebaseDatabase.getInstance().getReference().child("Accounts");

        EA_Admin_Users emptyAdapter = new EA_Admin_Users();
        recyclerView.setAdapter(emptyAdapter);

        searchView = findViewById(R.id.searchView);
        //setOnClickListner();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                            list.add(ds.getValue(Account.class));
                        }
                        Adapter_admin_users adapter_admin_users = new Adapter_admin_users(list,listener);
                        recyclerView.setAdapter(adapter_admin_users);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Admin_view_Users.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
/*
    private void setOnClickListner() {
        listener = new AdapterClass.RecycleViewClickListener(){
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

 */


    private void search(String str) {
        ArrayList<Account> mylist = new ArrayList<>();
        for(Account object: list)
        {
            if(object.getName().toLowerCase().contains(str.toLowerCase())){
                mylist.add(object);
            }
        }
        Adapter_admin_users adapterClass = new Adapter_admin_users(mylist,listener);
        recyclerView.setAdapter(adapterClass);
    }

}
