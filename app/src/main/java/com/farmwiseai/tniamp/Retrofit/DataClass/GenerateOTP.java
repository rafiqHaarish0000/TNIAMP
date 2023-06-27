package com.farmwiseai.tniamp.Retrofit.DataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateOTP {
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("response message")
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

        @SerializedName("otp_data_id")
        @Expose
        private Integer otpDataId;

        public Integer getOtpDataId() {
            return otpDataId;
        }

        public void setOtpDataId(Integer otpDataId) {
            this.otpDataId = otpDataId;
        }

    }

}