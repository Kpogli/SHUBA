package com.example.android.shubadriver;

/**
 * Created by Kennedy on 29/03/2017.
 */

public class WaitingCommuter {
    public String stopName;
    public int waiterCount;
    public String key;
    public Double longitude;

    public WaitingCommuter(String stopName, int waiterCount, String key, Double longitude) {
        this.stopName = stopName;
        this.waiterCount = waiterCount;
        this.key = key;
        this.longitude = longitude;
    }

    public WaitingCommuter() {

    }

    public void setStopName(String stopName) {this.stopName = stopName;}

    public void setWaiterCount(int waiterCount) {this.waiterCount = waiterCount;}

    public void setKey(String key) {this.key = key;}

    public void setLongitude(Double longitude) {this.longitude = longitude;}

    public String getStopName() {return stopName;}

    public int getWaiterCount() {return waiterCount;}

    public String getKey() {return key;}

    public Double getLongitude() {return longitude;}
}
