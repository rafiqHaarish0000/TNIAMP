package com.farmwiseai.tniamp.Retrofit.DataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserCountData {
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("responseMessage")
    @Expose
    private Response.ResponseMessage responseMessage;

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

    public Response.ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(Response.ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

}
     class Response {

        @SerializedName("geo_tag_count")
        @Expose
        private Integer geoTagCount;

        public Integer getGeoTagCount() {
            return geoTagCount;
        }

        public void setGeoTagCount(Integer geoTagCount) {
            this.geoTagCount = geoTagCount;
        }


         class ResponseMessage {

            @SerializedName("Response")
            @Expose
            private Response response;

            public Response getResponse() {
                return response;
            }

            public void setResponse(Response response) {
                this.response = response;
            }
        }
    }

