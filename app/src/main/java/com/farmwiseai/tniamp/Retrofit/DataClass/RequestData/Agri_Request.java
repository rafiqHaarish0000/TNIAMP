package com.farmwiseai.tniamp.Retrofit.DataClass.RequestData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Agri_Request {
    @SerializedName("village")
    @Expose
    private String village;
    @SerializedName("intervention1")
    @Expose
    private String intervention1;
    @SerializedName("intervention2")
    @Expose
    private String intervention2;
    @SerializedName("intervention3")
    @Expose
    private String intervention3;
    @SerializedName("farmer_name")
    @Expose
    private String farmerName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("survey_no")
    @Expose
    private String surveyNo;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("variety")
    @Expose
    private String variety;
    @SerializedName("image1")
    @Expose
    private String image1;
    @SerializedName("yield")
    @Expose
    private String yield;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("tank_name")
    @Expose
    private String tankName;
    @SerializedName("txn_date")
    @Expose
    private String txnDate;
    @SerializedName("photo_lat")
    @Expose
    private String photoLat;
    @SerializedName("photo_lon")
    @Expose
    private String photoLon;
    @SerializedName("txn_id")
    @Expose
    private String txnId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("intervention_type")
    @Expose
    private String interventionType;
    @SerializedName("other_intervention")
    @Expose
    private String otherIntervention;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("date_count_open")
    @Expose
    private String dateCountOpen;
    @SerializedName("date_revolving_fund_release")
    @Expose
    private String dateRevolvingFundRelease;
    @SerializedName("seed_area")
    @Expose
    private String seedAreaDecimal;
    @SerializedName("quantity_procured")
    @Expose
    private String quantityProcured;

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getIntervention1() {
        return intervention1;
    }

    public void setIntervention1(String intervention1) {
        this.intervention1 = intervention1;
    }

    public String getIntervention2() {
        return intervention2;
    }

    public void setIntervention2(String intervention2) {
        this.intervention2 = intervention2;
    }

    public String getIntervention3() {
        return intervention3;
    }

    public void setIntervention3(String intervention3) {
        this.intervention3 = intervention3;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSurveyNo() {
        return surveyNo;
    }

    public void setSurveyNo(String surveyNo) {
        this.surveyNo = surveyNo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getYield() {
        return yield;
    }

    public void setYield(String yield) {
        this.yield = yield;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTankName() {
        return tankName;
    }

    public void setTankName(String tankName) {
        this.tankName = tankName;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getPhotoLat() {
        return photoLat;
    }

    public void setPhotoLat(String photoLat) {
        this.photoLat = photoLat;
    }

    public String getPhotoLon() {
        return photoLon;
    }

    public void setPhotoLon(String photoLon) {
        this.photoLon = photoLon;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInterventionType() {
        return interventionType;
    }

    public void setInterventionType(String interventionType) {
        this.interventionType = interventionType;
    }

    public String getOtherIntervention() {
        return otherIntervention;
    }

    public void setOtherIntervention(String otherIntervention) {
        this.otherIntervention = otherIntervention;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDateCountOpen() {
        return dateCountOpen;
    }

    public void setDateCountOpen(String dateCountOpen) {
        this.dateCountOpen = dateCountOpen;
    }

    public String getDateRevolvingFundRelease() {
        return dateRevolvingFundRelease;
    }

    public void setDateRevolvingFundRelease(String dateRevolvingFundRelease) {
        this.dateRevolvingFundRelease = dateRevolvingFundRelease;
    }

    public String getSeedAreaDecimal() {
        return seedAreaDecimal;
    }

    public void setSeedAreaDecimal(String seedAreaDecimal) {
        this.seedAreaDecimal = seedAreaDecimal;
    }

    public String getQuantityProcured() {
        return quantityProcured;
    }

    public void setQuantityProcured(String quantityProcured) {
        this.quantityProcured = quantityProcured;
    }

}
