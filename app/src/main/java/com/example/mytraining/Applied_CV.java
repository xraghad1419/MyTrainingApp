package com.example.mytraining;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class Applied_CV extends AppCompatActivity {

    TextView Name, Email, Phone, Major, GPA, Skills, Interest, Nationality;
    ImageView Img;
    Uri imgurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* R: resource
           link between java variables and design variables by (findViewById) */
        setContentView(R.layout.applied_cv);

        Name=findViewById(R.id.fullNameCV);
        Email=findViewById(R.id.EmailCV);
        Phone=findViewById(R.id.phone_cv);
        Major=findViewById(R.id.major_cv);
        GPA=findViewById(R.id.gpa_cv);
        Skills=findViewById(R.id.skills_cv);
        Interest=findViewById(R.id.intrest_cv);
        Nationality=findViewById(R.id.national_cv);
        Img=findViewById(R.id.image_app_view);

        // Here we get Information of organization from onClick method in applicant page
        Bundle x = getIntent().getExtras();


        String img = x.getString("aImg");

        imgurl = Uri.parse(img);
        Picasso.get().load(imgurl).into(Img);

        String name = x.getString("aName");
        Name.setText(name);

        String email = x.getString("aEmail");
        Email.setText(email);

        String phone = x.getString("aPhone");
        Phone.setText(phone);

        String major = x.getString("aMajor");
        Major.setText(major);

        String gpa = x.getString("aGPA");
        GPA.setText(gpa);

        String skills = x.getString("aSkills");
        Skills.setText(skills);

        String interest = x.getString("aInterest");
        Interest.setText(interest);

        String nationality = x.getString("aNationality");
        Nationality.setText(nationality);


    }
}
