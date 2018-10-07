package com.example.macintosh.assignmentt1.ModelClass;

public class ResponseJSON {
    private String origin;
    private String destination;
    private String distance;
    private String duration;
    private int id;
    public ResponseJSON(String origin,String destination,String distance,String duration){
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
        this.duration = duration;
    }
    public String getOrigin(){
        return origin;
    }
    public String getDestination(){
        return destination;
    }
    public String getDistance(){
        return distance;
    }
    public String getDuration(){
        return duration;
    }
    public void setID(int ID){this.id = ID;}
    public Integer getID(){return id;}
}
