package com.example.mytraining;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Recomended_Page extends AppCompatActivity {

    public static final String CHANNEL_ID = "x_channelId";

    String pp;

    private static final String TAG = "Recommended_Page";
    private SharedPreferences app_id_to_tr;
    ArrayList<String> plist = new ArrayList<>();

    //Nav bar
    BottomNavigationView bottomNavigationView;
    ArrayList<TrainingOffer> list = new ArrayList<>();
    RecyclerView recyclerView;
    private Recommended_Adapter.RecycleViewClickListener listener;
    DatabaseReference ref;
    //ArrayList<TrainingOffer> mylist;
    DatabaseReference rA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.recomnded);

        recyclerView = findViewById(R.id.rec_Offers);

        app_id_to_tr = PreferenceManager.getDefaultSharedPreferences(this);

        // Nav bar
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.recommended);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Applicant_page.class));
                        overridePendingTransition(0, 0);
                        return;

                    case R.id.offer:
                        startActivity(new Intent(getApplicationContext(), offer_page.class));
                        overridePendingTransition(0, 0);
                        return;

                    case R.id.recommended:
                        startActivity(new Intent(getApplicationContext(), Recomended_Page.class));
                        overridePendingTransition(0, 0);
                        return;

                    case R.id.applied:
                        startActivity(new Intent(getApplicationContext(),Applied_Page.class));
                        overridePendingTransition(0,0);
                        return;
                }
                return;
            }
        });

        EmptyAdapter_recommended emptyAdapter = new EmptyAdapter_recommended();
        recyclerView.setAdapter(emptyAdapter);

        ref = FirebaseDatabase.getInstance().getReference().child("TrainingOffers");
        rA = FirebaseDatabase.getInstance().getReference().child("Applicants");

        setOnClickListner();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setOnClickListner() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id_app = sp.getString("Applicant_page", "DefaultValue");

        listener = new Recommended_Adapter.RecycleViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                app_id_to_tr.edit().putString(TAG, id_app).apply();
                Intent intent = new Intent(getApplicationContext(), Training_Details.class);
                intent.putExtra("offerID", list.get(position).getId());
                intent.putExtra("offerName", list.get(position).getOfferName());
                intent.putExtra("offerNationality", list.get(position).getRequiredNationality());
                intent.putExtra("offerGPA", list.get(position).getRequiredGPA());
                intent.putExtra("offerCity", list.get(position).getCity());
                intent.putExtra("offerStartD", list.get(position).getStartDate());
                intent.putExtra("offerEndD", list.get(position).getEndDate());
                intent.putExtra("offerDesc", list.get(position).getOfferDescription());
                intent.putExtra("offerMajor", list.get(position).getMajor());
                intent.putExtra("offerBenefits", list.get(position).getBenefits());
                intent.putExtra("offerEligible", list.get(position).getEligibility());
                intent.putExtra("offerLink", list.get(position).getLink());
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String valueID = sp.getString("Applicant_page", "DefaultValue");

        rA.child(valueID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String natio_app = snapshot.child("nationality").getValue().toString();
                String city_app = snapshot.child("city").getValue().toString();
                String major = snapshot.child("major").getValue().toString();
                String gpa_app = snapshot.child("gpa").getValue().toString();
                double gpa_a = Double.parseDouble(gpa_app);

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                System.out.println("*********************     offer     *******************************************      ");

                                if (ds.child("requiredNationality").getValue().toString().equals(natio_app)) {
                                    System.out.println("*********************     nationality matched      *******************************************   25%    ");

                                    if (ds.child("city").getValue().toString().equals(city_app)) {
                                        System.out.println("*********************     city matched      *******************************************   50%    ");

                                        if(ds.child("major").getValue().toString().equals(major)) {
                                            System.out.println("*********************     Major  matched      *******************************************    75%   ");

                                            String gpa_DB = ds.child("requiredGPA").getValue().toString();
                                            double g = Double.parseDouble(gpa_DB);
                                            if (gpa_a >= g) {
                                                System.out.println("*********************     GPA matched      *******************************************    100%   ");
                                                list.add(ds.getValue(TrainingOffer.class));
                                                plist.add("100%");
                                            }
                                            else{
                                                list.add(ds.getValue(TrainingOffer.class));
                                                plist.add("75%");
                                            }
                                        }
                                        else{
                                            list.add(ds.getValue(TrainingOffer.class));
                                            plist.add("50%");
                                        }
                                    }
                                    else{
                                        list.add(ds.getValue(TrainingOffer.class));
                                        plist.add("25%");
                                    }
                                }
                                else {
                                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& Nothing");
                                }
                            }
                            Recommended_Adapter recommended_Adapter = new Recommended_Adapter(list,listener,plist);
                            recyclerView.setAdapter(recommended_Adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Recomended_Page.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void displayNotification() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"channel display",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("my channel description");

            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this,Recomended_Page.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(),CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle("New Recommended Offer")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pi)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("We found recommended offer for you, check it"))
                .addAction(R.drawable.ic_baseline_notifications_active_24,"reply",pi);

        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        nmc.notify(10,builder.build());
    }
}
