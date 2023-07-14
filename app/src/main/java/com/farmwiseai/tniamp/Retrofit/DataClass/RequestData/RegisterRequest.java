package com.farmwiseai.tniamp.Retrofit.DataClass.RequestData;

public class RegisterRequest {
    public String SERIAL_NO;
    public int LINE_DEPT;
    public int VILLAGE;
    public String NAME;
    public String MOBILE;
    public String EMAIL;
    public String CREATED_DATE;
    public String lat;
    public String lon;
    public String version;
    public int subbasin;
    public int USER_STATUS;

    public String getSERIAL_NO() {
        return SERIAL_NO;
    }

    public void setSERIAL_NO(String SERIAL_NO) {
        this.SERIAL_NO = SERIAL_NO;
    }

    public int getLINE_DEPT() {
        return LINE_DEPT;
    }

    public void setLINE_DEPT(int LINE_DEPT) {
        this.LINE_DEPT = LINE_DEPT;
    }

    public int getVILLAGE() {
        return VILLAGE;
    }

    public void setVILLAGE(int VILLAGE) {
        this.VILLAGE = VILLAGE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getCREATED_DATE() {
        return CREATED_DATE;
    }

    public void setCREATED_DATE(String CREATED_DATE) {
        this.CREATED_DATE = CREATED_DATE;
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

    public int getSubbasin() {
        return subbasin;
    }

    public void setSubbasin(int subbasin) {
        this.subbasin = subbasin;
    }

    public int getUSER_STATUS() {
        return USER_STATUS;
    }

    public void setUSER_STATUS(int USER_STATUS) {
        this.USER_STATUS = USER_STATUS;
    }
}
