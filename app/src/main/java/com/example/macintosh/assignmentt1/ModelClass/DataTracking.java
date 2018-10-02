package com.example.macintosh.assignmentt1.ModelClass;

import java.io.Serializable;
import java.util.Date;

public class DataTracking implements Serializable {

    private int trackableId;
    private String title;
    private Date starttime;
    private Date endtime;
    private Date meettime;
    private Double currentLocationlatitude;
    private Double currentLocationlongtitude;
    private Double meetLocationlatitude;
    private Double meetLocationlongtitude;
    public DataTracking(){
        this.trackableId = 0;
        this.title = "No Data";
        this.starttime = new Date();
        this.endtime = new Date();
        this.meettime = new Date();
        this.currentLocationlatitude = 0.0;
        this.currentLocationlongtitude = 0.0;
        this.meetLocationlatitude = 0.0;
        this.meetLocationlongtitude = 0.0;
    }

    public DataTracking(int trackableId,String title,Date starttime,Date endtime,Date meettime,
                        double currentLocationlatitude,double currentLocationlongtitude, double meetLocationlatitude, double meetLocationlongtitude){
        this.trackableId =trackableId;
        this.title = title;
        this.starttime = new Date();
        this.starttime.setTime(starttime.getTime());
        this.endtime = new Date();
        this.endtime.setTime(endtime.getTime());
        this.meettime = new Date();
        this.meettime.setTime(meettime.getTime());
        this.currentLocationlatitude = currentLocationlatitude;
        this.currentLocationlongtitude = currentLocationlongtitude;
        this.meetLocationlatitude = meetLocationlatitude;
        this.meetLocationlongtitude = meetLocationlongtitude;
    }
    public int getTrackableId() {
        return this.trackableId;
    }
    public String getTitle() {
        return this.title;
    }
    public Date getEndTime() {
        return this.endtime;
    }
    public Date getStartTime() {
        return this.starttime;
    }
    public Date getMeetTime() {
        return this.meettime;
    }
    public Double getCurrentLocationlatitude() {
        return this.currentLocationlatitude;
    }
    public Double getCurrentLocationlongtitude() {
        return this.currentLocationlongtitude;
    }
    public Double getMeetLocationlatitude() {
        return this.meetLocationlatitude;
    }
    public Double getMeetLocationlongtitude() {
        return this.meetLocationlongtitude;
    }
    public void setEndtime(Date newEndTime){this.endtime = newEndTime;}
    public void setTitle(String title){this.title = title;}
}
