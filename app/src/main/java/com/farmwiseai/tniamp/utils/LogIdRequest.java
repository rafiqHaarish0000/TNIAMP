package com.farmwiseai.tniamp.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogIdRequest {
    @SerializedName("SERIAL_NO")
    @Expose
    private String serialNo;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("LINE_DEPT")
    @Expose
    private String lineDept;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLineDept() {
        return lineDept;
    }

    public void setLineDept(String lineDept) {
        this.lineDept = lineDept;
    }

}