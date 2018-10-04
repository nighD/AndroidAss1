package com.example.macintosh.assignmentt1.AlarmReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.macintosh.assignmentt1.Activities.CheckAvailability;
import com.example.macintosh.assignmentt1.Activities.MainActivity;
import com.example.macintosh.assignmentt1.NotificationScheduler.NotificationScheduler;


public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub


        try {

            boolean isVisible = CheckAvailability.isActivityVisible();// Check if
            // activity
            // is
            // visible
            // or not
            Log.i("Activity is Visible ", "Is activity visible : " + isVisible);

            // If it is visible then trigger the task else do nothing
            if (isVisible == true) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager
                        .getActiveNetworkInfo();

                // Check internet connection and accrding to state change the
                // text of activity by calling method
                if (networkInfo != null && networkInfo.isConnected()) {

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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}