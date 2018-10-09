package com.example.macintosh.assignmentt1.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.macintosh.assignmentt1.Activities.MainActivity;
import com.example.macintosh.assignmentt1.HTTP.HttpClientApacheAsyncTask;
import com.example.macintosh.assignmentt1.HTTP.HttpClientApacheAsyncTask0;
import com.example.macintosh.assignmentt1.HTTP.UpdateURL;
import com.example.macintosh.assignmentt1.JDBC.JDBCActivity;
import com.example.macintosh.assignmentt1.ModelClass.CurrentMeetLocationModel;
import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.NotificationScheduler.NotificationScheduler;
import com.example.macintosh.assignmentt1.Service.GPS_Service;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;


public class NotiReceiver extends BroadcastReceiver {
    private static BroadcastReceiver broadcastReceiver;
    private static int REMINDER_TIME = 10;
    String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        final UpdateURL updateURL = new UpdateURL();
        final MainActivity mainActivity = new MainActivity();
        final String db = "jdbc:sqldroid:" + context.getApplicationContext().getDatabasePath( "assignment1.db" ).getAbsolutePath();
        JDBCActivity jdbcActivity = new JDBCActivity();
        //HttpClientApache httpClientApache;
        jdbcActivity.turnOnConnection( db );
        CurrentMeetLocationModel[] currentMeetLocationModels = jdbcActivity.takeLatLng( db, parseDate( "07-05-2018 13:00:00" ) );
        Log.i( TAG, "SHIT" );
        final String[] currentLocation = new String[1];
        final String[] longtitude = {""};
        final String[] latitude = {""};
        ArrayList<DataTracking> dataTrackings;
        dataTrackings = jdbcActivity.getData( db );
        ArrayList<Integer> chosen = getCloserTime( dataTrackings,parseDate( "07-05-2018 13:01:00" ) );
        int closest = 10;
        if (chosen.size() > 0){
            closest = chosen.get( 0 );
            for(int i = 0 ;i < chosen.size();i++){
                if( closest > chosen.get( i ))
                    closest = chosen.get( i );
            }
        }
        final int chosenClosest = closest;
        final LatLng[] latLng0 = new LatLng[1];
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //Log.i(TAG,"\n" +intent.getExtras().get("coordinates"));
                    Log.i( TAG, "GET RECEIVER" );
                    currentLocation[0] = intent.getExtras().get( "coordinates" ).toString();
                    Scanner scanner = new Scanner( currentLocation[0] );
                    scanner.useDelimiter( "\\s" );
                    longtitude[0] = scanner.next();
                    latitude[0] = scanner.next();
                    latLng0[0] = new LatLng( Double.parseDouble( latitude[0] ), Double.parseDouble( longtitude[0] ) );
                    if( chosenClosest != 10){
                        LatLng latLng1 = new LatLng( dataTrackings.get(chosenClosest).getMeetLocationlatitude()
                            , dataTrackings.get(chosenClosest).getMeetLocationlongtitude() );
                        new HttpClientApacheAsyncTask0( mainActivity, updateURL.UpdateURLService( latLng0[0], latLng1 )
                            , context, dataTrackings.get( chosenClosest ),latLng0[0]).execute();
                    }
                }
            };
        }
        Intent i = new Intent( context, GPS_Service.class );
        context.stopService( i );
        context.getApplicationContext().registerReceiver( broadcastReceiver, new IntentFilter( "location_update" ) );
        // Set the alarm here.
        Log.d( TAG, "onReceive AlarmReceiver: BOOT_COMPLETED" );
        NotificationScheduler.setReminderNoti( context, 10 );
        jdbcActivity.turnOffConnection();
    }
    private static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("DD-MM-YYYY hh:mm:ss").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
    private static ArrayList<Integer> getCloserTime(ArrayList<DataTracking> dataTrackings,Date current){
        ArrayList<Integer> chosen = new ArrayList<Integer>(  );
        SimpleDateFormat hour = new SimpleDateFormat("hh" );
        SimpleDateFormat minute = new SimpleDateFormat("mm" );
        int currentHour = Integer.parseInt(hour.format( current ));
        int currentMinute = Integer.parseInt(minute.format( current ));
        for( int i = 0 ;i < dataTrackings.size();i++){
            int trackingHour  = Integer.parseInt( hour.format(dataTrackings.get(i).getMeetTime()));
            int trackingMinute = Integer.parseInt( minute.format( dataTrackings.get(i).getMeetTime()));
            int compareHour = trackingHour - currentHour;
            int compareMinute = trackingMinute - currentMinute;
            Log.i("COMPARISION", " Compare Minute : " + i + " Result: "+compareMinute);
            if (compareHour == 0){
                if( compareMinute > 5 && compareMinute < 10 ){
                    chosen.add(i);
                }
            }
        }
        return chosen;
    }


}