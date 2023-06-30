package com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WRDResponse {
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("responseMessage")
    @Expose
    private AgriResponse.ResponseMessage responseMessage;

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

    public AgriResponse.ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(AgriResponse.ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }


    public class ResponseMessage {

        @SerializedName("wrd_land_dept_id")
        @Expose
        private Integer wrdLandDeptId;

        public Integer getWrdLandDeptId() {
            return wrdLandDeptId;
        }

        public void setWrdLandDeptId(Integer wrdLandDeptId) {
            this.wrdLandDeptId = wrdLandDeptId;
        }
    }



}
