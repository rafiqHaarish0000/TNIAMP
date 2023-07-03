package com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FishResponse {
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("responseMessage")
    @Expose
    private FishResponse.ResponseMessage responseMessage;

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

    public FishResponse.ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(FishResponse.ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }


    public class ResponseMessage {

        @SerializedName("fishery_land_dept_id")
        @Expose
        private Integer fisherylanddeptid;

        public Integer getFisherylanddeptid() {
            return fisherylanddeptid;
        }

        public void setFisherylanddeptid(Integer fisherylanddeptid) {
            this.fisherylanddeptid = fisherylanddeptid;
        }
    }
}
