package com.example.macintosh.assignmentt1.ModelClass;

import java.io.Serializable;
import java.util.Date;

public class CurrentMeetLocationModel {
    private int trackableId;
    private Date startTime;
    private Date meetTime;
    private Double meetLocationlatitude;
    private Double meetLocationlongtitude;
    public CurrentMeetLocationModel(int trackableId,Date startTime,Date meetTime,Double meetLocationlatitude,Double meetLocationlongtitude){
        this.trackableId = trackableId;
        this.startTime = startTime;
        this.meetTime = meetTime;
        this.meetLocationlatitude = meetLocationlatitude;
        this.meetLocationlongtitude = meetLocationlongtitude;
    }
    public int getTrackableId(){ return trackableId;}
    public Date getMeetTime(){ return meetTime;}
    public Date getStartTime() { return startTime;}
    public double getMeetLocationLatitude(){ return meetLocationlatitude;}
    public double getMeetLocationLongtitude(){ return meetLocationlongtitude;}
}
