package com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgriResponse {
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("tnau_land_dept_id")
    @Expose
    private Integer tnauLandDeptId;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getTnauLandDeptId() {
        return tnauLandDeptId;
    }

    public void setTnauLandDeptId(Integer tnauLandDeptId) {
        this.tnauLandDeptId = tnauLandDeptId;
    }
}
