package com.example.mytraining;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Applicant implements Parcelable {
   String accountID,email,fname,date,city,nationality,skills,interest,phoneNumber,GPA,major, imageUrl;



    public Applicant() {
    }


    public Applicant(String accountID, String email, String fname, String date, String city, String nationality, String skills, String interest, String phoneNumber, String GPA, String major, String imageUrl) {
        this.accountID = accountID;
        this.email = email;
        this.fname = fname;
        this.date = date;
        this.city = city;
        this.nationality = nationality;
        this.skills = skills;
        this.interest = interest;
        this.phoneNumber = phoneNumber;
        this.GPA = GPA;
        this.major = major;
        this.imageUrl = imageUrl;
    }


    protected Applicant(Parcel in) {
        accountID = in.readString();
        email = in.readString();
        fname = in.readString();
        date = in.readString();
        city = in.readString();
        nationality = in.readString();
        skills = in.readString();
        interest = in.readString();
        phoneNumber = in.readString();
        GPA = in.readString();
        major = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Applicant> CREATOR = new Creator<Applicant>() {
        @Override
        public Applicant createFromParcel(Parcel in) {
            return new Applicant(in);
        }

        @Override
        public Applicant[] newArray(int size) {
            return new Applicant[size];
        }
    };

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGPA() {
        return GPA;
    }

    public void setGPA(String GPA) {
        this.GPA = GPA;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(accountID);
        parcel.writeString(email);
        parcel.writeString(fname);
        parcel.writeString(date);
        parcel.writeString(city);
        parcel.writeString(nationality);
        parcel.writeString(skills);
        parcel.writeString(interest);
        parcel.writeString(phoneNumber);
        parcel.writeString(GPA);
        parcel.writeString(major);
        parcel.writeString(imageUrl);
    }

}





