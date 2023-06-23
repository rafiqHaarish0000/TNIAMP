package com.farmwiseai.tniamp.Retrofit.DataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateOTP {
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("otp_data_id")
    @Expose
    private Integer otpDataId;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getOtpDataId() {
        return otpDataId;
    }

    public void setOtpDataId(Integer otpDataId) {
        this.otpDataId = otpDataId;
    }
}
