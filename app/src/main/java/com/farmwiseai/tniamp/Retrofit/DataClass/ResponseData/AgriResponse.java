package com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData;

public class AgriResponse {
    public String statusCode;
    public String response;
    public int tnau_land_dept_id;

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

    public int getTnau_land_dept_id() {
        return tnau_land_dept_id;
    }

    public void setTnau_land_dept_id(int tnau_land_dept_id) {
        this.tnau_land_dept_id = tnau_land_dept_id;
    }
}
