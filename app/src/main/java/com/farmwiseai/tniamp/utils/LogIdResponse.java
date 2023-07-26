package com.farmwiseai.tniamp.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogIdResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}
