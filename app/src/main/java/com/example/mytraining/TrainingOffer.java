package com.example.mytraining;

public class TrainingOffer {
    String id, organizationID, offerName, offerDescription, requiredNationality,
            requiredGPA, city, major, startDate, endDate, eligibility, benefits,imageUrl,link;

    public TrainingOffer() {
    }

    public TrainingOffer(String id, String organizationID, String imageUrl, String offerName, String offerDescription, String requiredNationality,
                         String requiredGPA, String city, String major, String startDate, String endDate, String eligibility, String benefits,String link) {
        this.id = id;
        this.organizationID = organizationID;
        this.imageUrl = imageUrl;
        this.offerName = offerName;
        this.offerDescription = offerDescription;
        this.requiredNationality = requiredNationality;
        this.requiredGPA = requiredGPA;
        this.city = city;
        this.major = major;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eligibility = eligibility;
        this.benefits = benefits;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getRequiredNationality() {
        return requiredNationality;
    }

    public void setRequiredNationality(String requiredNationality) {
        this.requiredNationality = requiredNationality;
    }

    public String getRequiredGPA() {
        return requiredGPA;
    }

    public void setRequiredGPA(String requiredGPA) {
        this.requiredGPA = requiredGPA;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
