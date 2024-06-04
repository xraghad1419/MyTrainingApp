package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

public class CV_Form extends AppCompatActivity {
    /* Here we define our variables that we will link it with design variables
       Here we define database reference to store our database */
    EditText FullName,City,GPA,Skills,Interest,Email,PhoneNum,Nationality,Date,Major;
    Button Save,Cancel;
    DatabaseReference ApplicantDB;


    private static final String TAG = "CV_Form";
    private SharedPreferences c;

    DatabaseReference AppDB = FirebaseDatabase.getInstance().getReference().child("Applicants");

    private ImageView imageView;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    // Global variables used in save method, and showApplicantCV method.
    String full_Name, city, skills, interest, email, nationality, date, phoneNum, gpa, major,imgurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = PreferenceManager.getDefaultSharedPreferences(this);


        // Applicant table in Firebase
        ApplicantDB = FirebaseDatabase.getInstance().getReference().child("Applicants");

        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.activity_cv_form);
        FullName = findViewById(R.id.etFullNameCV);
        City= findViewById(R.id.etCityCV);
        GPA= findViewById(R.id.etGPA_CV);
        Skills= findViewById(R.id.etSkills);
        Interest= findViewById(R.id.etInterest);
        Email= findViewById(R.id.etEmailCV);
        PhoneNum= findViewById(R.id.etPhoneCV);
        Nationality= findViewById(R.id.etNationality);
        Date= findViewById(R.id.editTextDate);
        Major = findViewById(R.id.tMajor);
        Save= findViewById(R.id.btSaveCV);
        Cancel= findViewById(R.id.btCancelCV);
        imageView = findViewById(R.id.imageApplicant);


        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        String v = s.getString("Applicant_page", "DefaultValue");


        // calling method
        ShowApplicantCV();

        // Get Intent
        Intent intent = getIntent();
        // Retrieve and store as applicant object
        Applicant applicant = intent.getParcelableExtra("applicantCV");


        // Action of Save Button
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if imageUri is null or not
                if(imageUri != null){
                    uploadImageToFirebase(imageUri);
                }

                // Get intent
                Intent intent = getIntent();
                // Retrieve applicant ID to use it in path of "Applicants" in DB
                //String applicantID = intent.getStringExtra("aID");

                // Only store and update in database
                ApplicantDB.child(v).child("city").setValue(City.getText().toString());
                ApplicantDB.child(v).child("date").setValue(Date.getText().toString());
                ApplicantDB.child(v).child("email").setValue(Email.getText().toString());
                ApplicantDB.child(v).child("fname").setValue(FullName.getText().toString());
                ApplicantDB.child(v).child("interest").setValue(Interest.getText().toString());
                ApplicantDB.child(v).child("nationality").setValue(Nationality.getText().toString());
                ApplicantDB.child(v).child("phoneNumber").setValue(PhoneNum.getText().toString());
                ApplicantDB.child(v).child("skills").setValue(Skills.getText().toString());
                ApplicantDB.child(v).child("gpa").setValue(GPA.getText().toString());
                ApplicantDB.child(v).child("major").setValue(Major.getText().toString());

                // Go to Applicant page
                Intent cv = new Intent(CV_Form.this, Applicant_page.class);
                Toast.makeText(CV_Form.this, "Changes are saved", Toast.LENGTH_SHORT).show();
                startActivity(cv);

            }
        });

        // Action of imageView button
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,2);
            }
        });

        // Action of Cancel Button
        Cancel.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Applicant page
                Intent cv = new Intent(CV_Form.this, Applicant_page.class);
                startActivity(cv);
                // Show message
                Toast.makeText(CV_Form.this, "No changes in CV", Toast.LENGTH_SHORT).show();
            }
        });
        c.edit().putString(TAG, City.toString()).apply();
    }


    public void ShowApplicantCV(){
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        String v = s.getString("Applicant_page", "DefaultValue");

        // Use intent to retrieve information of applicant from Applicant page by (intent = putExtra("applicantCV")
        Intent intent = getIntent();

        // Retrieve and store as appShowCV object
        Applicant applicantInfo = intent.getParcelableExtra("applicantCV");
        //String applicantID = intent.getStringExtra("aID");

        AppDB.child(v).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FullName.setText(snapshot.child("fname").getValue(String.class));
                Email.setText(snapshot.child("email").getValue(String.class));
                Date.setText(snapshot.child("date").getValue(String.class));
                City.setText(snapshot.child("city").getValue(String.class));
                PhoneNum.setText(snapshot.child("phoneNumber").getValue(String.class));
                Nationality.setText(snapshot.child("nationality").getValue(String.class));
                Skills.setText(snapshot.child("skills").getValue(String.class));
                Interest.setText(snapshot.child("interest").getValue(String.class));
                GPA.setText(snapshot.child("gpa").getValue(String.class));
                Major.setText(snapshot.child("major").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ApplicantDB.child(v).child("imageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // read from DB and save value of imageUrl in String
                String image = snapshot.getValue(String.class);

                // if value is null (" "), organization did not upload an image
                if(image.equals("")){
                    System.out.println("##########################################################    Null    ################################################################");
                }
                else{
                    // if value is path of url, organization upload an image
                    System.out.println("##########################################################################################################################" + image);
                    // Picasso take picture and load it to display it inside imageview
                    Picasso.get().load(image).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadImageToFirebase(Uri uri) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        String v = s.getString("Applicant_page", "DefaultValue");
        //
        // Intent intent = getIntent();
        //String applicantID = intent.getStringExtra("aID");

        // Storage reference
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Create object from Applicant, set uploaded image, save in organization DB
                        Applicant app = new Applicant();
                        app.setImageUrl(uri.toString());
                        ApplicantDB.child(v).child("imageUrl").setValue(uri.toString());

                        Toast.makeText(CV_Form.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CV_Form.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri= data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}