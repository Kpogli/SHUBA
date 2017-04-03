package com.example.android.shuba1;

/**
 * Created by Kennedy on 29/03/2017.
 */

public class WaitingCommuter {

    public String stopName;
    public long waiterCount;
    public String UID;

    public WaitingCommuter(String stopName, long waiterCount) {
        this.stopName = stopName;
        this.waiterCount = waiterCount;
        this.UID = UID;
    }

    public WaitingCommuter() {

    }

    public void setStopName(String stopName) {this.stopName = stopName;}

    public void setWaiterCount(long waiterCount) {this.waiterCount = waiterCount;}

    public void setUID(String UID) {this.UID = UID;}

    public String getStopName() {return stopName;}

    public long getWaiterCount() {return waiterCount;}

    public String getUID() {return UID;}
}
