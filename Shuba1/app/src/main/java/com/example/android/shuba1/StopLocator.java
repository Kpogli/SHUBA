package com.example.android.shuba1;

/**
 * Created by Kennedy on 08/03/2017.
 */

public class StopLocator {
    private String stopName;
    private Double latitude;
    private Double longitude;

    public StopLocator(String stopName, Double latitude, Double longitude) {
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public StopLocator() {

    }

    public void setStopName(String stopName) { this.stopName = stopName;}

    public void setLatitude(Double latitude) { this.latitude = latitude;}

    public void setLongitude(Double longitude) { this.longitude = longitude;}

    public String getStopName() {return stopName;}

    public Double getLatitude() { return latitude;}

    public Double getLongitude() { return longitude;}

}
