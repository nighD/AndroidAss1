package com.example.macintosh.assignmentt1.ModelClass;

public class CurrentMeetLocationModel {
    private int trackableId;
    private Double meetLocationlatitude;
    private Double meetLocationlongtitude;
    public CurrentMeetLocationModel(int trackableId,Double meetLocationlatitude,Double meetLocationlongtitude){
        this.trackableId = trackableId;
        this.meetLocationlatitude = meetLocationlatitude;
        this.meetLocationlongtitude = meetLocationlongtitude;
    }
    public int getTrackableId(){ return trackableId;}
    public double getMeetLocationLatitude(){ return meetLocationlatitude;}
    public double getMeetLocationLongtitude(){ return meetLocationlongtitude;}
}
