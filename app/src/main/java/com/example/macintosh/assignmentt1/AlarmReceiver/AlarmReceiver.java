package com.example.macintosh.assignmentt1.AlarmReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.macintosh.assignmentt1.Activities.MainActivity;
import com.example.macintosh.assignmentt1.NotificationScheduler.NotificationScheduler;


public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub


                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                Log.d(TAG, "HERE");
               // LocalData localData = new LocalData(context);
                NotificationScheduler.setReminder(context,10);


        Log.d(TAG, "onReceive: ");

        //Trigger the notification
        NotificationScheduler.showNotification(context, MainActivity.class,
                "You have 5 unwatched videos", "Watch them now?");

    }
}