package com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HortiResponse {
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("responseMessage")
    @Expose
    private ResponseMessage responseMessage;

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

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }


    public class ResponseMessage {

        @SerializedName("horti_land_dept_id")
        @Expose
        private Integer hortiLandDeptId;

        public Integer getHortiLandDeptId() {
            return hortiLandDeptId;
        }

        public void setHortiLandDeptId(Integer hortiLandDeptId) {
            this.hortiLandDeptId = hortiLandDeptId;
        }

    }
}
