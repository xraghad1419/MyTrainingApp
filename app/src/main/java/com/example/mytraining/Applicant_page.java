package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Applicant_page extends AppCompatActivity {
    /* Here we define our variables that we will link it with design variables
       Here we define database reference to store our database */

    public static final String CHANNEL_ID = "x_channelId";

    TextView close;
    Button showMe;
    Dialog myDialog;

    SharedPreferences sharedPreferences;

    ImageButton  showCV, logout;
    TextView welcome ,ApplicantName;
    BottomNavigationView bottomNavigationView;

    DatabaseReference AppDB = FirebaseDatabase.getInstance().getReference().child("Applicants");

    ArrayList <Organization> list = new ArrayList<>();

    private static final String TAG = "Applicant_page";
    private SharedPreferences m;
    private SharedPreferences ID;
    private SharedPreferences ID_app;
    private SharedPreferences id_app_offer;
    private SharedPreferences passToOrg_info;

    String app_name;

    // define attributes
    DatabaseReference ref;
    RecyclerView recyclerView;
    private AdapterClass.RecycleViewClickListener listener;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_page);

        myDialog = new Dialog(this);


        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + firstStart);
        if(firstStart){

            // Dialog of popup
            myDialog.show();
            myDialog.setContentView(R.layout.custompopup);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            close = myDialog.findViewById(R.id.close);
            showMe = myDialog.findViewById(R.id.showMe);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.dismiss();
                }
            });

            showMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Applicant_page.this, Recomended_Page.class);
                    startActivity(intent);
                }
            });

            SharedPreferences refs = getSharedPreferences("prefs",MODE_PRIVATE);
            SharedPreferences.Editor editor = refs.edit();
            editor.putBoolean("firstStart",false);
            editor.apply();
            displayNotification();

            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + firstStart);

        }


        showCV = findViewById(R.id.showCV);
        welcome = findViewById(R.id.textwelcomeA);
        ApplicantName = findViewById(R.id.textApplicantNum);
        logout = findViewById(R.id.logout);
        recyclerView =findViewById(R.id.rv);

        // هذي جبناها من الساين ان
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String value = sp.getString("SignIN", "DefaultValue");

        // الي راح نرسلها للسي في
        m = PreferenceManager.getDefaultSharedPreferences(this);
        // الي راح نرسلها للي اورج انفو
        passToOrg_info = PreferenceManager.getDefaultSharedPreferences(this);
        // رايح لل ريكومندد اوفر بيج
        ID = PreferenceManager.getDefaultSharedPreferences(this);
        // رايح لـ كل الاوفر بيج
        id_app_offer = PreferenceManager.getDefaultSharedPreferences(this);
        // رايح لـ كل الابلايد بيج
        ID_app = PreferenceManager.getDefaultSharedPreferences(this);


        // Nav bar
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:

                        startActivity(new Intent(getApplicationContext(),Applicant_page.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.offer:
                        id_app_offer.edit().putString(TAG, value).apply();
                        startActivity(new Intent(getApplicationContext(),offer_page.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.recommended:
                        ID.edit().putString(TAG, value).apply();
                        startActivity(new Intent(getApplicationContext(),Recomended_Page.class));
                        overridePendingTransition(0,0);
                        return;

                    case R.id.applied:
                        ID_app.edit().putString(TAG, value).apply();
                        startActivity(new Intent(getApplicationContext(),Applied_Page.class));
                        overridePendingTransition(0,0);
                        return;
                }
                return;
            }
        });

        EmptyAdapter emptyAdapter = new EmptyAdapter();
        recyclerView.setAdapter(emptyAdapter);


        // Define reference in DB, and attach attributes with design attributes
        ref = FirebaseDatabase.getInstance().getReference().child("Organizations");
        searchView = findViewById(R.id.searchView);

        setOnClickListner();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Use intent to retrieve information of applicant from Sign in by (intent = putExtra("appInfo")
        Intent intent = getIntent();
        Applicant applicant = intent.getParcelableExtra("appInfo");
        //String ID = intent.getStringExtra("appID");

        //Intent i = getIntent();
        //String n = i.getStringExtra("appID");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+value);
        //String a = ID;

        AppDB.child(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ApplicantName.setText(snapshot.child("fname").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });




        // Action of searchView button
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

        // Action of Logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Sign in page
                Intent intent = new Intent(Applicant_page.this, SignIN.class);
                startActivity(intent);
                // Show message
                Toast.makeText(Applicant_page.this, "Sign out Successful", Toast.LENGTH_SHORT).show();

                SharedPreferences refs = getSharedPreferences("prefs",MODE_PRIVATE);
                SharedPreferences.Editor editor = refs.edit();
                editor.putBoolean("firstStart",true);
                editor.apply();
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + firstStart);

            }
        });

        showCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to CV Form page
                m.edit().putString(TAG, value).apply();
                Intent intent = new Intent(Applicant_page.this, CV_Form.class);
                // Send putExtra with object applicant that was created in line 34
                intent.putExtra("applicantCV",applicant);
                //intent.putExtra("aID",ID);
                startActivity(intent);
            }
        });

    }

    private void showStartDialog() {
    }

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
                        AdapterClass adapterClass = new AdapterClass(list,listener);
                        recyclerView.setAdapter(adapterClass);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Applicant_page.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void search(String str) {
        ArrayList<Organization> mylist = new ArrayList<>();
        for(Organization object: list)
        {
            if(object.getName().toLowerCase().contains(str.toLowerCase())){
                mylist.add(object);
            }
        }
        AdapterClass adapterClass = new AdapterClass(mylist,listener);
        recyclerView.setAdapter(adapterClass);
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