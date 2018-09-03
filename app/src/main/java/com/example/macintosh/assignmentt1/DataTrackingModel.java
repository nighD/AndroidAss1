package com.example.macintosh.assignmentt1;

import java.util.Date;

public class DataTrackingModel {
    public Date date;
    public int trackableId;
    public int stopTime;
    public double latitude;
    public double longitude;

    public DataTrackingModel(Date date, int trackableId, int stopTime, double latitude, double longitude){
        this.date = date;
        this.trackableId = trackableId;
        this.stopTime = stopTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Date getDate() {
        return this.date;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public int getStopTime() {
        return this.stopTime;
    }

    public int getTrackableId() {
        return this.trackableId;
    }
}