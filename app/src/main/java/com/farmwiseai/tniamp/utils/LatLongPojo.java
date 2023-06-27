package com.farmwiseai.tniamp.utils;

public class LatLongPojo {
    String lat,lon;

    public LatLongPojo(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public LatLongPojo() {
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
