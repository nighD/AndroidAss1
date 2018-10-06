package com.example.macintosh.assignmentt1.HTTP;

import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.google.android.gms.maps.model.LatLng;

public class UpdateURL {
    private String gglAPI = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    private String APIKey = "&key=AIzaSyCDbBGQ8CaRLa4rvOhsaG-LO0Rxy0CUGxI";
    public String UpdateURL(DataTracking dataTracking){
        gglAPI += "origins=";
        String tempurl = gglAPI + Double.toString(dataTracking.getCurrentLocationlatitude()) +","
                                + Double.toString(dataTracking.getCurrentLocationlongtitude())+"&destinations="
                                + Double.toString(dataTracking.getMeetLocationlatitude()) +","
                                + Double.toString(dataTracking.getMeetLocationlongtitude()) + APIKey;
        return tempurl;
    }
    public String UpdateURLService(LatLng currenLocation,LatLng destinationLocation){

        String tempurl = gglAPI + "origins=" + Double.toString(currenLocation.latitude) +","
                + Double.toString(currenLocation.longitude)+"&destinations="
                + Double.toString(destinationLocation.latitude) +","
                + Double.toString(destinationLocation.longitude) + APIKey;
        return tempurl;
    }
}
