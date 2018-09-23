package com.example.macintosh.assignmentt1.ModelClass;

public class ResponseJSON {
    private String origin;
    private String destination;
    private String distance;
    private String duration;
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
}
