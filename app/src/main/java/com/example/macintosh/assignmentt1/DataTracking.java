package com.example.macintosh.assignmentt1;

import java.util.Date;

public class DataTracking {

    public int trackableId;
    public String title;
    public Date starttime;
    public Date endtime;
    public Date meettime;
    public Double currentLocationlatitude;
    public Double currentLocationlongtitude;
    public Double meetLocationlatitude;
    public Double meetLocationlongtitude;

    public DataTracking(int trackableId,String title,Date starttime,Date endtime,Date meettime,
                        double currentLocationlatitude,double currentLocationlongtitude, double meetLocationlatitude, double meetLocationlongtitude){
        this.trackableId =trackableId;
        this.title = title;
        this.starttime = starttime;
        this.endtime = endtime;
        this.meettime = meettime;
        this.currentLocationlatitude = currentLocationlatitude;
        this.currentLocationlongtitude = currentLocationlongtitude;
        this.meetLocationlatitude = meetLocationlatitude;
        this.meetLocationlongtitude = meetLocationlongtitude;
    }

}
