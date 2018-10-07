package com.example.macintosh.assignmentt1.ModelClass;

public class NotificationModel {
    private int ID;
    private String destination;
    private String duration;
    public NotificationModel(){
        ID = 0;
        destination= "no data";
        duration = "no data";
    }
    public NotificationModel(int ID, String destination,String duration){
        this.ID = ID;
        this.destination = destination;
        this.duration = duration;
    }
    public int getID(){return ID;}
    public String getDestination(){return destination; }
    public String getDuration(){return duration;}
}
