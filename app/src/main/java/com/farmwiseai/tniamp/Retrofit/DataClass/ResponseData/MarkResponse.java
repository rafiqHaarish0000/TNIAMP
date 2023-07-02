package com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarkResponse {
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("responseMessage")
    @Expose
    private MarkResponse.ResponseMessage responseMessage;

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

    public MarkResponse.ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(MarkResponse.ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }


    public class ResponseMessage {

        @SerializedName("marketin_land_dept_id")
        @Expose
        private Integer marketinglanddeptid;

        public Integer getMarketinglanddeptid() {
            return marketinglanddeptid;
        }

        public void setMarketinglanddeptid(Integer marketinglanddeptid) {
            this.marketinglanddeptid = marketinglanddeptid;
        }



    }
}
