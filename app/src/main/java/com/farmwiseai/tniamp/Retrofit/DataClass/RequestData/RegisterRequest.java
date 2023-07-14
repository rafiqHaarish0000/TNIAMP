package com.farmwiseai.tniamp.Retrofit.DataClass.RequestData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterRequest {


    @SerializedName("SERIAL_NO")
    @Expose
    private String serialNo;
    @SerializedName("LINE_DEPT")
    @Expose
    private String lineDept;
    @SerializedName("VILLAGE")
    @Expose
    private String village;
    @SerializedName("NAME")
    @Expose
    private String name;
    @SerializedName("MOBILE")
    @Expose
    private String mobile;
    @SerializedName("EMAIL")
    @Expose
    private String email;
    @SerializedName("CREATED_DATE")
    @Expose
    private String createdDate;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("subbasin")
    @Expose
    private String subbasin;
    @SerializedName("USER_STATUS")
    @Expose
    private String userStatus;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getLineDept() {
        return lineDept;
    }

    public void setLineDept(String lineDept) {
        this.lineDept = lineDept;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSubbasin() {
        return subbasin;
    }

    public void setSubbasin(String subbasin) {
        this.subbasin = subbasin;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

}

