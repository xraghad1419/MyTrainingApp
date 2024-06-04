package com.example.mytraining;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Organization implements Parcelable {
    String accountID,email, name, vision , brief, twitter , linkedin , telephoneNumber, imageUrl;

    public Organization() {
    }

    public Organization(String accountID, String email, String name, String vision, String brief, String twitter, String linkedin, String telephoneNumber, String imageUrl) {
            this.accountID = accountID;
            this.email = email;
            this.name = name;
            this.vision = vision;
            this.brief = brief;
            this.twitter = twitter;
            this.linkedin = linkedin;
            this.telephoneNumber = telephoneNumber;
            this.imageUrl = imageUrl;
    }

    protected Organization(Parcel in) {
        accountID = in.readString();
        email = in.readString();
        name = in.readString();
        vision = in.readString();
        brief = in.readString();
        twitter = in.readString();
        linkedin = in.readString();
        telephoneNumber = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Organization> CREATOR = new Creator<Organization>() {
        @Override
        public Organization createFromParcel(Parcel in) {
            return new Organization(in);
        }

        @Override
        public Organization[] newArray(int size) {
            return new Organization[size];
        }
    };


    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
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
        parcel.writeString(name);
        parcel.writeString(vision);
        parcel.writeString(brief);
        parcel.writeString(twitter);
        parcel.writeString(linkedin);
        parcel.writeString(telephoneNumber);
        parcel.writeString(imageUrl);
    }
}


