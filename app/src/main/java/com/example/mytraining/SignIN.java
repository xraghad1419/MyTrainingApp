package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignIN extends AppCompatActivity {
    /* Here we define our variables that we will link it with design variables
       Here we define firebaseUser for authentication */
    TextView txRegisterNow, forgotPass, Admin;
    Button btnLogin;
    EditText etEmailAddress, etPassword;
    FirebaseAuth firebaseAuth;

    private static final String TAG = "SignIN";
    private SharedPreferences mPreferences;
    private SharedPreferences preferencesOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.activity_sign_in);
        btnLogin = findViewById(R.id.btnLogin);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        txRegisterNow = findViewById(R.id.txRegisterNow);
        forgotPass = findViewById(R.id.forgotPassword);
        Admin = findViewById(R.id.admin);


        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesOrg = PreferenceManager.getDefaultSharedPreferences(this);

        // Action of Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call method
                authenticateUserAccount();

            }
        });

        txRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent movetoREG = new Intent(SignIN.this, RegistrationActivity.class);
                startActivity(movetoREG);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movetoLog = new Intent(SignIN.this, Forgot_Password.class);
                startActivity(movetoLog);
            }
        });

        Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movetoLog = new Intent(SignIN.this, Admin_SignIn_page.class);
                startActivity(movetoLog);
            }
        });

    }

    public void authenticateUserAccount() {
        String email = etEmailAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmailAddress.setError("Email is required!");
            etEmailAddress.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailAddress.setError("Please provide valid email!");
            etEmailAddress.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("You enter wrong password!");
            etPassword.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //getUid it retrieve user id authentication table
                    String uid = task.getResult().getUser().getUid();
                    System.out.println(uid);

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                    firebaseDatabase.getReference().child("Accounts").child(uid).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String userType = snapshot.getValue(String.class);
                            if(userType.equals("applicant")) {
                                firebaseDatabase.getReference().child("Applicants").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String id = dataSnapshot.child("accountID").getValue().toString();
                                            if (id.equals(uid)) {

                                                // This object is retrieve information of this applicant, will send it to Applicant page
                                                Applicant appInfo = dataSnapshot.getValue(Applicant.class);
                                                String appID = dataSnapshot.getKey();

                                                mPreferences.edit().putString(TAG, appID).apply();

                                                // Go to applicant page
                                                Intent intent = new Intent(getApplicationContext(), Applicant_page.class);
                                                intent.putExtra("appInfo",appInfo);
                                                //intent.putExtra("appID",appID);
                                                // putExtra will take (appInfo) object and send it to applicant page to retrieve applicant information
                                                startActivity(intent);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else if(userType.equals("organization")){
                                System.out.println("-------------------------------------------------------O---------------------------------------------------------------");
                                firebaseDatabase.getReference().child("Organizations").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String id = dataSnapshot.child("accountID").getValue().toString();
                                            if (id.equals(uid)) {

                                                // This object is retrieve information of this organization, will send it to Organization page
                                                Organization orgInfo = dataSnapshot.getValue(Organization.class);
                                                String orgID = dataSnapshot.getKey();

                                                preferencesOrg.edit().putString(TAG, orgID).apply();

                                                // Go to organization page
                                                Intent intent = new Intent(getApplicationContext(), Org_Page.class);
                                                // putExtra will take (orgInfo) object and send it to organization page to retrieve organization information
                                                intent.putExtra("orgInfo", orgInfo);
                                                //intent.putExtra("oID",orgID);
                                                startActivity(intent);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                else {
                    Toast.makeText(SignIN.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}