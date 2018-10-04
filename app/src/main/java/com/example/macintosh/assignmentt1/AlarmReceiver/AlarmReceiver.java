package com.example.macintosh.assignmentt1.AlarmReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.util.Log;

import com.example.macintosh.assignmentt1.Activities.MainActivity;
import com.example.macintosh.assignmentt1.Activities.MapsActivity;
import com.example.macintosh.assignmentt1.NotificationScheduler.NotificationScheduler;
import com.example.macintosh.assignmentt1.Service.GPS_Service;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;
import java.util.Scanner;


public class AlarmReceiver extends BroadcastReceiver {
    private static BroadcastReceiver broadcastReceiver;
    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        Intent intent0 =new Intent(context,GPS_Service.class);
        context.startService(intent0);
        final String[] currentLocation = new String[1];
        final String[] longtitude = {""};
        final String[] latitude = {""};
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    Log.i(TAG,"\n" +intent.getExtras().get("coordinates"));
                    currentLocation[0] = intent.getExtras().get( "coordinates" ).toString();
                    Scanner scanner = new Scanner( currentLocation[0] );
                    scanner.useDelimiter( "\\s" );
                    longtitude[0] = scanner.next();
                    latitude[0] = scanner.next();
                    Log.i(TAG,"\n" +longtitude[0] + latitude[0]);
                    LatLng latlng = new LatLng(Double.parseDouble(longtitude[0]),Double.parseDouble(latitude[0]));

                    NotificationScheduler.showNotification(context, MainActivity.class,
                            longtitude[0] + " " +latitude[0], "Watch them now?");
                }
            };
        }
        context.getApplicationContext().registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
        MapsActivity mapsActivity = new MapsActivity();
        // Set the alarm here.
        Log.d(TAG, "onReceive AlarmReceiver: BOOT_COMPLETED");
        // LocalData localData = new LocalData(context);
//        Location currentLocation = mapsActivity.getDeviceLocation();
        NotificationScheduler.setReminder(context,10);


        Log.d(TAG, "onReceive: ");

        //Trigger the notification


    }
}