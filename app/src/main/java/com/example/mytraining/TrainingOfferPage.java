package com.example.mytraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class TrainingOfferPage extends AppCompatActivity {

    public static final String CHANNEL_ID = "x_channelId";

    /* Here we define our variables that we will link it with design variables
       Here we define database reference to store our database */
    EditText OfferName, City, GPA, OfferDes, Eligibility, Benefits, Nationality, StartDate, EndDate, Link, Major;
    Button Save, Cancel;

    //ImageView imageOffer;
    DatabaseReference Training_Offer = FirebaseDatabase.getInstance().getReference().child("TrainingOffers");

    private static final String TAG = "TrainingOfferPage";


    // Global variables used in save method, and showApplicantCV method.
    String offer_name, city, gpa, offer_des, eligibility, benefits, nationality, startDate, endDate, imgurl, link, major;
    String tr_offer_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.activity_training_offer);
        OfferName = findViewById(R.id.etFullName);
        City = findViewById(R.id.etCityOffer);
        GPA = findViewById(R.id.etGPA_Offer);
        OfferDes = findViewById(R.id.etDes);
        Eligibility = findViewById(R.id.etEligibility);
        Benefits = findViewById(R.id.etBenefits);
        Nationality = findViewById(R.id.etNationality);
        StartDate = findViewById(R.id.editTextStartDate);
        EndDate = findViewById(R.id.editTextEndDate);
        Save = findViewById(R.id.btSaveOffer);
        Cancel = findViewById(R.id.btCancelOffer);
        Link = findViewById(R.id.etlinkoffer);
        Major = findViewById(R.id.et_Major);
        //imageOffer = findViewById(R.id.imageOffer);


        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        String OrgID = s.getString("Org_Page", "DefaultValue");

        // Creating spinner for dropdown menu of majors

        Intent intent = getIntent();
        Organization organization = intent.getParcelableExtra("orgProfile");

        SharedPreferences image = PreferenceManager.getDefaultSharedPreferences(this);
        String imgOffer = image.getString("Org_profilePage", "DefaultValue");

        imgurl = imgOffer;
        //Picasso.get().load(imgurl).into(imageOffer);


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Org_Page.class);
                startActivity(intent);
            }
        });

        // Action of Save Button
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertTrainingOffers();

                if (offer_name.isEmpty()) {
                    OfferName.setError("Offer Name is required!");
                    OfferName.requestFocus();
                    return;
                }

                if (startDate.isEmpty()) {
                    StartDate.setError("Start Date is required!");
                    StartDate.requestFocus();
                    return;
                }

                if (endDate.isEmpty()) {
                    EndDate.setError("End Date is required!");
                    EndDate.requestFocus();
                    return;
                }

                if (city.isEmpty()) {
                    City.setError("City is required!");
                    City.requestFocus();
                    return;
                }

                if (gpa.isEmpty()) {
                    GPA.setError("GPA is required!");
                    GPA.requestFocus();
                    return;
                }

                if (major.isEmpty()) {
                    Major.setError("Major is required!");
                    Major.requestFocus();
                    return;
                }


                if (nationality.isEmpty()) {
                    Nationality.setError("Nationality is required!");
                    Nationality.requestFocus();
                    return;
                }

                if (offer_des.isEmpty()) {
                    OfferDes.setError("Description is required!");
                    OfferDes.requestFocus();
                    return;
                }

                if (eligibility.isEmpty()) {
                    Eligibility.setError("Eligibility is required!");
                    Eligibility.requestFocus();
                    return;
                }

                if (benefits.isEmpty()) {
                    Benefits.setError("Benefits are required!");
                    Benefits.requestFocus();
                    return;
                }

                if (link.isEmpty()) {
                    Link.setError("Link is required!");
                    Link.requestFocus();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), Org_Page.class);
                startActivity(intent);
                Toast.makeText(TrainingOfferPage.this,"Added successfully", Toast.LENGTH_SHORT).show();


                // Code Notifications
                //displayNotification();
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

    private void insertTrainingOffers() {

        Intent intent = getIntent();
        //String OrganizationID = intent.getStringExtra("oID");


        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        String OrgID = s.getString("Org_Page", "DefaultValue");


        String orgID = OrgID;
        offer_name = OfferName.getText().toString();
        city = City.getText().toString();
        gpa = GPA.getText().toString();
        offer_des = OfferDes.getText().toString();
        eligibility = Eligibility.getText().toString();
        benefits = Benefits.getText().toString();
        nationality = Nationality.getText().toString();
        startDate = StartDate.getText().toString();
        endDate = EndDate.getText().toString();
        link = Link.getText().toString();
        major = Major.getText().toString();


        tr_offer_id = Training_Offer.push().getKey();
        // Training object
        TrainingOffer trainingOffer = new TrainingOffer(tr_offer_id,orgID,imgurl,offer_name,offer_des,nationality,gpa,city,major,startDate,endDate,eligibility,benefits,link);

        //Added to DB
        Training_Offer.child(tr_offer_id).setValue(trainingOffer);

    }

}