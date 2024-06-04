package com.example.mytraining;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {
    /* Here we define our variables that we will link it with design variables
       Here we define firebaseUser for authentication
       Here we define database reference to store our database */
    EditText etEmailAddress, etPassword, name;
    Button btnRegisterNow;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference AccountDB;
    DatabaseReference ApplicantDB;
    DatabaseReference OrganizationDB;
    TextView login, forgotPass;
    RadioButton a,o;
    RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.activity_registration);
        btnRegisterNow = findViewById(R.id.btnRegister);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        AccountDB = FirebaseDatabase.getInstance().getReference().child("Accounts");
        //AccountDB = FirebaseDatabase.getInstance().getReference("path: Accounts");
        ApplicantDB = FirebaseDatabase.getInstance().getReference().child("Applicants");
        OrganizationDB = FirebaseDatabase.getInstance().getReference().child("Organizations");
        login = findViewById(R.id.logbtn);
        forgotPass = findViewById(R.id.forgotPassword);
        a = findViewById(R.id.apps); //applicant button
        o = findViewById(R.id.orgs); //organization button
        radioGroup = findViewById(R.id.rg);
        name = findViewById(R.id.fullName);


        // Action of Register button
        btnRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call method
                createAccount();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movetoLog = new Intent(RegistrationActivity.this, SignIN.class);
                startActivity(movetoLog);

            }
        });

    }

    private void createAccount() {

        String emailAddress = etEmailAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String Name = name.getText().toString().trim();

        if(Name.isEmpty()){
            name.setError("Name is required!");
            name.requestFocus();
            return;
        }

        if(emailAddress.isEmpty()){
            etEmailAddress.setError("Email is required!");
            etEmailAddress.requestFocus();
            return;
        }

        if(password.isEmpty()){
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            etEmailAddress.setError("Please enter valid email!");
            etEmailAddress.requestFocus();
            return;
        }

        if(password.length() < 6){
            etPassword.setError("Your Password is less than 6 digits!");
            etPassword.requestFocus();
            return;
        }

        if(radioGroup.getCheckedRadioButtonId() == -1){
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&666666666666666666&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            a.setFocusable(true);
            o.setFocusable(true);
        }


        firebaseAuth.createUserWithEmailAndPassword(emailAddress,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    firebaseUser = task.getResult().getUser();
                    if(a.isChecked()){
                        Intent intent = new Intent(getApplicationContext(), SignIN.class);
                        startActivity(intent);

                        // save userID into accountID, to use it in two objects and to save in DB
                        String accountID = firebaseUser.getUid();
                        // create an account object
                        Account appAccount = new Account(Name,emailAddress,password,"applicant");

                        // AccountDB.child(accountID); edited by Dr. Sahar
                        // Create a child with accountID and set attributes (emailAddress,password,"applicant")
                        AccountDB.child(accountID).setValue(appAccount);
                        // Create an applicant object (accountID, email address) and( empty arguments )
                        Applicant applicant = new Applicant(accountID,emailAddress,Name,"","","","","","", "","","");
                        ApplicantDB.push().setValue(applicant);
                        // Go to Login page

                    }
                    else if(o.isChecked()){
                        Intent intent = new Intent(getApplicationContext(), SignIN.class);
                        startActivity(intent);
                        // save userID into accountID, to use it in two objects and to save in DB
                        String accountID = firebaseUser.getUid();
                        // create an account object
                        Account orgAccount = new Account(Name,emailAddress,password,"organization");

                        // AccountDB.child(accountID); edited by Dr. Sahar
                        // Create a child with accountID and set attributes (emailAddress,password,"organization")
                        AccountDB.child(accountID).setValue(orgAccount);
                        // Create an organization object (accountID, email address) and( empty arguments )
                        Organization organization = new Organization(accountID,emailAddress,Name,"","","","","","");
                        OrganizationDB.push().setValue(organization);
                        // Go to Login page
                    }



                    Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}





