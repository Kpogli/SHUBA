package com.example.android.shubadriver;

/**
 * Created by Kennedy on 29/03/2017.
 */

public class WaitingCommuter {
    public String stopName;
    public String waiterCount;
    public String key;
    public Double longitude;

    public WaitingCommuter(String stopName, String waiterCount, String key, Double longitude) {
        this.stopName = stopName;
        this.waiterCount = waiterCount;
        this.key = key;
        this.longitude = longitude;
    }

    public WaitingCommuter() {

    }

    public void setStopName(String stopName) {this.stopName = stopName;}

    public void setWaiterCount(String waiterCount) {this.waiterCount = waiterCount;}

    public void setKey(String key) {this.key = key;}

    public void setLongitude(Double longitude) {this.longitude = longitude;}

    public String getStopName() {return stopName;}

    public String getWaiterCount() {return waiterCount;}

    public String getKey() {return key;}

    public Double getLongitude() {return longitude;}
}
