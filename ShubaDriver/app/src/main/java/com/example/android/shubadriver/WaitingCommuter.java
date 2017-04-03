package com.example.android.shubadriver;

/**
 * Created by Kennedy on 29/03/2017.
 */

public class WaitingCommuter {
    public String stopName;
    public long waiterCount;

    public WaitingCommuter(String stopName, long waiterCount) {
        this.stopName = stopName;
        this.waiterCount = waiterCount;
    }

    public WaitingCommuter() {

    }

    public void setStopName(String stopName) {this.stopName = stopName;}

    public void setWaiterCount(long waiterCount) {this.waiterCount = waiterCount;}

    public String getStopName() {return stopName;}

    public long getWaiterCount() {return waiterCount;}
}
