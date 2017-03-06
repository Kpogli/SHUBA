package com.example.android.shuba1;

import java.util.Date;

/**
 * Created by Kennedy on 03/03/2017.
 */

public class BusLocator {

    private String driverName;
    private String driverEmail;
    private Double latitude;
    private Double longitude;
    private Long timestamp;

    public BusLocator(String driverName, String driverEmail, Double latitude, Double longitude) {
        this.driverName = driverName;
        this.driverEmail = driverEmail;
        this.latitude = latitude;
        this.longitude = longitude;

        // Initialize to current time
        timestamp = new Date().getTime();
    }

    public BusLocator() {

    }


    public void setDriverName(String driverName) { this.driverName = driverName;}

    public void setDriverEmail(String driverEmail) { this.driverEmail = driverEmail;}

    public void setLatitude(Double latitude) { this.latitude = latitude;}

    public void setLongitude(Double longitude) { this.longitude = longitude;}

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }



    public String getDriverName() { return driverName;}

    public String getDriverEmail() { return driverEmail;}

    public Double getLatitude() { return latitude;}

    public Double getLongitude() { return longitude;}

    public long getTimestamp() {
        return timestamp;
    }

}
