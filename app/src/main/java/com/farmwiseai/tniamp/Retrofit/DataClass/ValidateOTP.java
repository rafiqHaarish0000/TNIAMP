package com.farmwiseai.tniamp.Retrofit.DataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidateOTP {

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


        @SerializedName("Response")
        @Expose
        private String response;
        @SerializedName("ID")
        @Expose
        private Integer id;
        @SerializedName("SERIAL_NO")
        @Expose
        private String serialNo;
        @SerializedName("LINE_DEPT")
        @Expose
        private Integer lineDept;
        @SerializedName("VILLAGE")
        @Expose
        private Integer village;
        @SerializedName("NAME")
        @Expose
        private String name;
        @SerializedName("MOBILE")
        @Expose
        private Long mobile;
        @SerializedName("EMAIL")
        @Expose
        private String email;
        @SerializedName("CREATED_DATE")
        @Expose
        private String createdDate;
        @SerializedName("lat")
        @Expose
        private Object lat;
        @SerializedName("lon")
        @Expose
        private Object lon;
        @SerializedName("version")
        @Expose
        private Object version;
        @SerializedName("subbasin")
        @Expose
        private Integer subbasin;
        @SerializedName("USER_STATUS")
        @Expose
        private Integer userStatus;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public Integer getLineDept() {
            return lineDept;
        }

        public void setLineDept(Integer lineDept) {
            this.lineDept = lineDept;
        }

        public Integer getVillage() {
            return village;
        }

        public void setVillage(Integer village) {
            this.village = village;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getMobile() {
            return mobile;
        }

        public void setMobile(Long mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Object getLat() {
            return lat;
        }

        public void setLat(Object lat) {
            this.lat = lat;
        }

        public Object getLon() {
            return lon;
        }

        public void setLon(Object lon) {
            this.lon = lon;
        }

        public Object getVersion() {
            return version;
        }

        public void setVersion(Object version) {
            this.version = version;
        }

        public Integer getSubbasin() {
            return subbasin;
        }

        public void setSubbasin(Integer subbasin) {
            this.subbasin = subbasin;
        }

        public Integer getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(Integer userStatus) {
            this.userStatus = userStatus;
        }
        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

    }
}