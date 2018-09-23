package com.example.macintosh.assignmentt1.HTTP;

import com.example.macintosh.assignmentt1.ModelClass.DataTracking;

public class UpdateURL {
    private String gglAPI = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    private String APIKey = "&key=AIzaSyCDbBGQ8CaRLa4rvOhsaG-LO0Rxy0CUGxI";
    public String UpdateURL(DataTracking dataTracking){
        gglAPI += "origin=";
        String tempurl = gglAPI + Double.toString(dataTracking.getCurrentLocationlatitude()) +","
                                + Double.toString(dataTracking.getCurrentLocationlongtitude())+"&destination="
                                + Double.toString(dataTracking.getMeetLocationlatitude()) +","
                                + Double.toString(dataTracking.getMeetLocationlongtitude()) + APIKey;
        return tempurl;
    }
}
