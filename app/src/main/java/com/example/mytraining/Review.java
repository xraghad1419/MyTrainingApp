package com.example.mytraining;

public class Review {
    String id, applicantID, trainingID, applicantName, review, ratingStar,orgID, rate_date;

    public Review() {
    }

    public Review(String id, String applicantID, String trainingID, String applicantName, String review, String ratingStar, String orgID, String rate_date) {
        this.id = id;
        this.applicantID = applicantID;
        this.trainingID = trainingID;
        this.applicantName = applicantName;
        this.review = review;
        this.ratingStar = ratingStar;
        this.orgID = orgID;
        this.rate_date = rate_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public String getTrainingID() {
        return trainingID;
    }

    public void setTrainingID(String trainingID) {
        this.trainingID = trainingID;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(String ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public String getRate_date() {
        return rate_date;
    }

    public void setRate_date(String rate_date) {
        this.rate_date = rate_date;
    }
}
