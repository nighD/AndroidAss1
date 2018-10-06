package com.example.macintosh.assignmentt1.NotificationScheduler;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.example.macintosh.assignmentt1.Activities.MainActivity;
import com.example.macintosh.assignmentt1.Activities.MapsActivity;
import com.example.macintosh.assignmentt1.AlarmReceiver.AlarmReceiver;
import com.example.macintosh.assignmentt1.R;
import com.example.macintosh.assignmentt1.Service.GPS_Service;

import java.util.Calendar;
import java.util.Scanner;

import static android.content.Context.ALARM_SERVICE;


public class NotificationScheduler
{
    public static final int REMINDER_REQUEST_CODE=100;
    public static final String TAG="NotificationScheduler";
    private static final String YES_ACTION = "com.example.macintosh.assignmentt1.NotificationScheduler.YES_ACTION";
    private static final String MAYBE_ACTION = "com.example.macintosh.assignmentt1.NotificationScheduler.MAYBE_ACTION";
    private static final String NO_ACTION = "com.example.macintosh.assignmentt1.NotificationScheduler.NO_ACTION";

    public static void setReminder(Context context,int alarmTriggerTime)
    {
       // Calendar calendar = Calendar.getInstance();
        Log.i(TAG, "Reminder Set");
        Calendar cal = Calendar.getInstance();
        // add alarmTriggerTime seconds to the calendar object
        cal.add(Calendar.SECOND, alarmTriggerTime);
        cancelReminder( context );
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,REMINDER_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds

        Toast.makeText(context, "Alarm Set for " + alarmTriggerTime + " seconds.", Toast.LENGTH_SHORT).show();

    }

    public static void cancelReminder(Context context)
    {
        // Disable a receiver
        Intent intent1 = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);//cancel the alarm manager of the pending intent
        Toast.makeText(context, "Alarm Canceled/Stop by User.", Toast.LENGTH_SHORT).show();
        pendingIntent.cancel();
    }

    public static void showNotification(Context context,Class<?> cls,String title,String content)
    {


        Intent intent0 =new Intent(context,GPS_Service.class);
        context.stopService(intent0);
        String CHANNEL_ID = "my_channel_01";
        Intent acceptIntent = new Intent( context, MapsActivity.class );
        acceptIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        PendingIntent pendingIntent = PendingIntent.getActivity( context,0, acceptIntent,PendingIntent.FLAG_ONE_SHOT );

        Intent maybeIntent = getNotificationIntent(context);
        //maybeIntent.setAction(MAYBE_ACTION);

        Intent noIntent = getNotificationIntent(context);
        //noIntent.setAction(NO_ACTION);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {



            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor( Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText("None" )

                .addAction(new NotificationCompat.Action(
                        R.mipmap.ic_thumb_up_black_36dp,
                        "Accept",
                        pendingIntent))
                .addAction(new NotificationCompat.Action(
                        R.mipmap.ic_thumbs_up_down_black_36dp,
                        "Skip",
                        PendingIntent.getActivity(context, 0, maybeIntent, PendingIntent.FLAG_UPDATE_CURRENT)))
                .addAction(new NotificationCompat.Action(
                        R.mipmap.ic_thumb_down_black_36dp,
                        "Cancel",
                        PendingIntent.getActivity(context, 0, noIntent, PendingIntent.FLAG_UPDATE_CURRENT)));

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        if(broadcastReceiver != null){
//            context.getApplicationContext().unregisterReceiver(broadcastReceiver);
//        }
        Intent i = new Intent(context,GPS_Service.class);
        context.stopService(i);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(REMINDER_REQUEST_CODE, builder.build());
    }


    private static Intent getNotificationIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intent.setFlags( Int )
        return intent;
    }
}