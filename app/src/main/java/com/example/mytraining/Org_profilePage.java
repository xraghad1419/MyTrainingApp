package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
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

import java.util.UUID;

public class Org_profilePage extends AppCompatActivity {
    /* Here we define our variables that we will link it with design variables
       Here we define firebaseUser for authentication
       Here we define database reference to store our database */
    EditText OrganizationName, Vision, Brief, Twitter, LinkedIn, Email, TelePhone;
    Button Save, Cancel;
    DatabaseReference OrganizationDB;
    DatabaseReference OrgDB = FirebaseDatabase.getInstance().getReference().child("Organizations");

    private static final String TAG = "Org_profilePage";

    SharedPreferences img;

    // Define design attributes of imageview
    private ImageView imageView;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    // Global variables
    String name, vision, brief, twitter, linkedIn, email, telephone, imgurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // organization table in Firebase
        OrganizationDB = FirebaseDatabase.getInstance().getReference().child("Organizations");
        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.activity_org_profile_page);
        OrganizationName = findViewById(R.id.etOrgNum);
        Vision = findViewById(R.id.etVision);
        Brief = findViewById(R.id.etBrief);
        Twitter = findViewById(R.id.etTwitter);
        LinkedIn = findViewById(R.id.etLinkedIn);
        Email = findViewById(R.id.etEmailOrg);
        TelePhone = findViewById(R.id.etTelePhoneOrg);
        Save = findViewById(R.id.btSaveProfile);
        Cancel = findViewById(R.id.btCancelProfile);
        imageView = findViewById(R.id.imageOrg);

        img = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        String vOrg = s.getString("Org_Page", "DefaultValue");


        // call method
        ShowOrganizationProfile();

        //Get intent
        Intent intent = getIntent();
        Organization organization = intent.getParcelableExtra("orgProfile");



        // Action of Sava button
        Save.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if imageUri is null or not
                if(imageUri != null){
                    uploadImageToFirebase(imageUri);
                }

                // Use intent to retrieve information of organization from Organization page by (intent = putExtra("orgProfile")
                Intent intent = getIntent();
                //String organizationID = intent.getStringExtra("oID");

                OrganizationDB.child(vOrg).child("brief").setValue(Brief.getText().toString());
                OrganizationDB.child(vOrg).child("email").setValue(Email.getText().toString());
                OrganizationDB.child(vOrg).child("linkedin").setValue(LinkedIn.getText().toString());
                OrganizationDB.child(vOrg).child("name").setValue(OrganizationName.getText().toString());
                OrganizationDB.child(vOrg).child("telephoneNumber").setValue(TelePhone.getText().toString());
                OrganizationDB.child(vOrg).child("twitter").setValue(Twitter.getText().toString());
                OrganizationDB.child(vOrg).child("vision").setValue(Vision.getText().toString());

                Intent OrgProfile = new Intent(Org_profilePage.this, Org_Page.class);
                startActivity(OrgProfile);
                Toast.makeText(Org_profilePage.this, "Changes are saved", Toast.LENGTH_SHORT).show();

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
                // Go to org page
                Intent OrgProfile = new Intent(Org_profilePage.this, Org_Page.class);
                startActivity(OrgProfile);
                // Show message
                Toast.makeText(Org_profilePage.this, "No changes in Profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowOrganizationProfile() {

        // Use intent to retrieve information of org from ogr page by (intent = putExtra("orgProfile")
        Intent intent = getIntent();
        // Retrieve and store as OrgShowProfile object
        Organization OrgShowProfile = intent.getParcelableExtra("orgProfile");
        //String organizationID = intent.getStringExtra("oID");

        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        String vOrg = s.getString("Org_Page", "DefaultValue");

        // Retrieve org information on profile (widgets link to design)
        OrgDB.child(vOrg).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrganizationName.setText(snapshot.child("name").getValue(String.class));
                Vision.setText(snapshot.child("vision").getValue(String.class));
                Brief.setText(snapshot.child("brief").getValue(String.class));
                Twitter.setText(snapshot.child("twitter").getValue(String.class));
                LinkedIn.setText(snapshot.child("linkedin").getValue(String.class));
                Email.setText(snapshot.child("email").getValue(String.class));
                TelePhone.setText(snapshot.child("telephoneNumber").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Define DB to read imgURL and retrieve it in imageview
        OrganizationDB.child(vOrg).child("imageUrl").addValueEventListener(new ValueEventListener() {
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
        String vOrg = s.getString("Org_Page", "DefaultValue");


        Intent intent = getIntent();
        //String organizationID = intent.getStringExtra("oID");
        // Storage reference
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Create object from organization, set uploaded image, save in organization DB
                        Organization org = new Organization();
                        org.setImageUrl(uri.toString());
                        OrganizationDB.child(vOrg).child("imageUrl").setValue(uri.toString());

                        img.edit().putString(TAG,uri.toString()).apply();

                        Toast.makeText(Org_profilePage.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Org_profilePage.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
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

