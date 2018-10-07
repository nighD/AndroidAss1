package com.example.macintosh.assignmentt1.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.macintosh.assignmentt1.Activities.MainActivity;
import com.example.macintosh.assignmentt1.HTTP.HttpClientApacheAsyncTask;
import com.example.macintosh.assignmentt1.HTTP.UpdateURL;
import com.example.macintosh.assignmentt1.JDBC.JDBCActivity;
import com.example.macintosh.assignmentt1.ModelClass.CurrentMeetLocationModel;
import com.example.macintosh.assignmentt1.NotificationScheduler.NotificationScheduler;
import com.example.macintosh.assignmentt1.Service.GPS_Service;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;


public class AlarmReceiver extends BroadcastReceiver {
    private static BroadcastReceiver broadcastReceiver;
    private static int REMINDER_TIME = 10;
    String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        final UpdateURL updateURL = new UpdateURL();
        final MainActivity mainActivity = new MainActivity();
        final String db = "jdbc:sqldroid:" + context.getApplicationContext().getDatabasePath("ass1.db").getAbsolutePath();
        JDBCActivity jdbcActivity = new JDBCActivity();
        //HttpClientApache httpClientApache;
        CurrentMeetLocationModel[] currentMeetLocationModels = jdbcActivity.takeLatLng( db,parseDate("07-05-2018 13:00:00") );
        Log.i(TAG,"SHIT");
        final String[] currentLocation = new String[1];
        final String[] longtitude = {""};
        final String[] latitude = {""};
        //context.getApplicationContext().registerReceiver(broadcastReceiver1,new IntentFilter("location_and_duration"));
        //NotificationModel[] notificationModel1 = new NotificationModel[currentMeetLocationModels.length];
        final LatLng[] latLng0 = new LatLng[1];
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //Log.i(TAG,"\n" +intent.getExtras().get("coordinates"));
                    Log.i(TAG,"GET RECEIVER");
                    currentLocation[0] = intent.getExtras().get( "coordinates" ).toString();
                    Scanner scanner = new Scanner( currentLocation[0] );
                    scanner.useDelimiter( "\\s" );
                    longtitude[0] = scanner.next();
                    latitude[0] = scanner.next();
                    latLng0[0] = new LatLng(Double.parseDouble(latitude[0]),Double.parseDouble(longtitude[0]));
                    Random rand = new Random();
                    int  n = rand.nextInt(3) + 0;
                    Log.i(TAG,"number "+ n);
                    LatLng latLng1 = new LatLng( currentMeetLocationModels[n].getMeetLocationLatitude()
                                , currentMeetLocationModels[n].getMeetLocationLongtitude() );
                    Log.i(TAG,"trackableID "+currentMeetLocationModels[n].getTrackableId());
                    new HttpClientApacheAsyncTask( mainActivity, updateURL.UpdateURLService( latLng0[0], latLng1 )
                                , context, currentMeetLocationModels[n]).execute();
                }
            };
        }
        Intent i = new Intent(context,GPS_Service.class);
        context.stopService(i);
        context.getApplicationContext().registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
        // Set the alarm here.
        Log.d(TAG, "onReceive AlarmReceiver: BOOT_COMPLETED");
        NotificationScheduler.setReminder(context,REMINDER_TIME);

    }
    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("DD-MM-YYYY hh:mm:ss").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }


}