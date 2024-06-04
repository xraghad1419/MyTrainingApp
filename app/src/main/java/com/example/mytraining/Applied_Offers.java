package com.example.mytraining;

public class Applied_Offers {
    String id, applicantID, trainingID;

    public Applied_Offers() {
    }

    public Applied_Offers(String id, String applicantID, String trainingID) {
        this.id = id;
        this.applicantID = applicantID;
        this.trainingID = trainingID;
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
}
