package com.example.macintosh.assignmentt1.Receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Context.*;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.macintosh.assignmentt1.NotificationScheduler.NotificationScheduler;
import android.app.NotificationManager;
public class CancelReceiver extends BroadcastReceiver {
    public static final String TAG="SkipReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"Cancel");
        Toast.makeText(context,"Cancel",Toast.LENGTH_SHORT).show();
        NotificationManager nm = (NotificationManager)
                context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.cancel(intent.getExtras().getInt("notificationID"));
        //NotificationScheduler.setReminder( context,0 );
    }



}