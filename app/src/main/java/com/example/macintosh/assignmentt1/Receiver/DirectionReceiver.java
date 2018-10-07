package com.example.macintosh.assignmentt1.Receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Context.*;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.macintosh.assignmentt1.Activities.DirectionActivity;
import com.example.macintosh.assignmentt1.NotificationScheduler.NotificationScheduler;
import android.app.NotificationManager;
public class DirectionReceiver extends BroadcastReceiver {
    public static final String TAG="DirectionReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"Recevier");
        String location = intent.getExtras().getString("location");
        String current = intent.getExtras().getString( "current" );
        Toast.makeText(context,"Go",Toast.LENGTH_SHORT).show();
        Log.i(TAG,"Direction " + location);
        Log.i(TAG,"current " + current);
//        Intent i = new Intent("location_direction");
//        i.putExtra("directionLocation",location);
//        context.sendBroadcast(i);
        Intent map = new Intent( context, DirectionActivity.class );
        map.putExtra( "direction_location",location );
        map.putExtra( "direction_current",current );
        context.startActivity( map );

        //NotificationScheduler.setReminder( context,0 );
    }



}