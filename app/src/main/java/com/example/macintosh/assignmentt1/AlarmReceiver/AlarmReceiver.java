package com.example.macintosh.assignmentt1.AlarmReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import com.example.macintosh.assignmentt1.Activities.MainActivity;
import com.example.macintosh.assignmentt1.Activities.MapsActivity;
import com.example.macintosh.assignmentt1.HTTP.HttpClientApacheAsyncTask;
import com.example.macintosh.assignmentt1.HTTP.UpdateURL;
import com.example.macintosh.assignmentt1.JDBC.JDBCActivity;
import com.example.macintosh.assignmentt1.ModelClass.CurrentMeetLocationModel;
import com.example.macintosh.assignmentt1.NotificationScheduler.NotificationScheduler;
import com.example.macintosh.assignmentt1.Service.GPS_Service;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;
import java.util.Scanner;


public class AlarmReceiver extends BroadcastReceiver {
    private static BroadcastReceiver broadcastReceiver;
    private static BroadcastReceiver broadcastReceiver1;
    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        final UpdateURL updateURL = new UpdateURL();
        Log.i(TAG,"SHIT");
        final MainActivity mainActivity = new MainActivity();
        Intent intent0 =new Intent(context,GPS_Service.class);
        context.startService(intent0);
        final String[] currentLocation = new String[1];
        final String[] longtitude = {""};
        final String[] latitude = {""};
        context.getApplicationContext().registerReceiver(broadcastReceiver1,new IntentFilter("location_and_duration"));
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
                    LatLng latLng0 = new LatLng(Double.parseDouble(latitude[0]),Double.parseDouble(longtitude[0]));
                    final String db = "jdbc:sqldroid:" + context.getApplicationContext().getDatabasePath("ass1.db").getAbsolutePath();
                    JDBCActivity jdbcActivity = new JDBCActivity();

                    CurrentMeetLocationModel[] currentMeetLocationModels = jdbcActivity.takeLatLng( db );
                    for(int i = 0 ; i < currentMeetLocationModels.length;i++){
                        LatLng latLng1 = new LatLng( currentMeetLocationModels[i].getMeetLocationLatitude()
                                ,currentMeetLocationModels[i].getMeetLocationLongtitude());
                        new HttpClientApacheAsyncTask( mainActivity,updateURL.UpdateURLService(latLng0,latLng1),context ).execute( );
                        if(broadcastReceiver1 == null) {
                            broadcastReceiver1 = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    Log.i( TAG,"SSSSS");
                                    // Log.i( TAG,  );

                                }
                            };
                        }
                    }

                    NotificationScheduler.showNotification(context, MainActivity.class,
                          "SHIT", "Watch them now?");

                }
            };
        }
        context.getApplicationContext().registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
        //MapsActivity mapsActivity = new MapsActivity();
        // Set the alarm here.
        Log.d(TAG, "onReceive AlarmReceiver: BOOT_COMPLETED");
        NotificationScheduler.setReminder(context,20);


        Log.d(TAG, "onReceive: ");

        //Trigger the notification


    }
}