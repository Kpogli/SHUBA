package com.example.android.shubadriver;

import java.util.Date;

/**
 * Created by Kennedy on 01/03/2017.
 */

public class BusLocator {

    private String driverId;
    private String driverName;
    private Double latitude;
    private Double longitude;
    private Long timestamp;

    public BusLocator(String driverId, String driverName, Double latitude, Double longitude) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.latitude = latitude;
        this.longitude = longitude;

        // Initialize to current time
        timestamp = new Date().getTime();
    }

    public BusLocator() {

    }

    public void setDriverId(String driverId) { this.driverId = driverId;}

    public void setDriverName(String driverName) { this.driverName = driverName;}

    public void setLatitude(Double latitude) { this.latitude = latitude;}

    public void setLongitude(Double longitude) { this.longitude = longitude;}

    public String getDriverId() { return driverId;}

    public String getDriverName() { return driverName;}

    public Double getLatitude() { return latitude;}

    public Double getLongitude() { return longitude;}


}
