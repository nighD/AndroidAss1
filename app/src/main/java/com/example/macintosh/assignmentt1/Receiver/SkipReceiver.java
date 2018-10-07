package com.example.macintosh.assignmentt1.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.macintosh.assignmentt1.NotificationScheduler.NotificationScheduler;

public class SkipReceiver extends BroadcastReceiver {
    public static final String TAG="SkipReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"RECEIVERRRR");
        Toast.makeText(context,"recieved and set",Toast.LENGTH_SHORT).show();

        NotificationScheduler.setReminder( context,0 );
    }
}