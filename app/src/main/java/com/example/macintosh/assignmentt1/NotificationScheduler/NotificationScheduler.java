package com.example.macintosh.assignmentt1.NotificationScheduler;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.example.macintosh.assignmentt1.Activities.AddTrackingServiceActivity;
import com.example.macintosh.assignmentt1.Activities.MainActivity;
import com.example.macintosh.assignmentt1.Receiver.AlarmReceiver;
//import com.example.macintosh.assignmentt1.HTTP.HttpClientApache;
import com.example.macintosh.assignmentt1.ModelClass.CurrentMeetLocationModel;
import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.ModelClass.NotificationModel;
import com.example.macintosh.assignmentt1.R;
import com.example.macintosh.assignmentt1.Receiver.CancelReceiver;
import com.example.macintosh.assignmentt1.Receiver.NotiReceiver;
import com.example.macintosh.assignmentt1.Receiver.SkipReceiver;
import com.example.macintosh.assignmentt1.Service.GPS_Service;

import java.util.ArrayList;
import java.util.Calendar;


public class NotificationScheduler
{
    public static final int NOTI_ID=1;
    public static final int NOTI_ID0=2;
    public static final String TAG="NotificationScheduler";
    private static final String YES_ACTION = "com.example.macintosh.assignmentt1.NotificationScheduler.YES_ACTION";
    private static final String MAYBE_ACTION = "com.example.macintosh.assignmentt1.NotificationScheduler.MAYBE_ACTION";
    private static final String NO_ACTION = "com.example.macintosh.assignmentt1.NotificationScheduler.NO_ACTION";
    private static ArrayList<NotificationModel> notificationModels;
    private static MainActivity activity;
    public static void setReminder(Context context,int alarmTriggerTime)
    {
       // Calendar calendar = Calendar.getInstance();
        Intent intent0 =new Intent(context,GPS_Service.class);
        context.startService(intent0);
        Log.i(TAG, "Reminder Set");
        Calendar cal = Calendar.getInstance();
        // add alarmTriggerTime seconds to the calendar object
        cal.add(Calendar.SECOND, alarmTriggerTime);
        cancelReminder( context );
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,NOTI_ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds

        Toast.makeText(context, "Alarm Set for " + alarmTriggerTime + " seconds.", Toast.LENGTH_SHORT).show();

    }
    public static void setReminderNoti(Context context,int alarmTriggerTime)
    {
        // Calendar calendar = Calendar.getInstance();
        Intent intent0 =new Intent(context,GPS_Service.class);
        context.startService(intent0);
        Log.i(TAG, "Reminder Set");
        Calendar cal = Calendar.getInstance();
        // add alarmTriggerTime seconds to the calendar object
        cal.add(Calendar.SECOND, alarmTriggerTime);
        cancelReminder( context );
        Intent alarmIntent = new Intent(context, NotiReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,NOTI_ID0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds

        Toast.makeText(context, "Alarm Set for " + alarmTriggerTime + " seconds.", Toast.LENGTH_SHORT).show();

    }
    public static void cancelReminder(Context context)
    {
        // Disable a receiver
        Intent intent1 = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTI_ID, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);//cancel the alarm manager of the pending intent
        Toast.makeText(context, "Alarm Canceled/Stop by User.", Toast.LENGTH_SHORT).show();
        pendingIntent.cancel();
    }

    public static void showNotification(Context context, CurrentMeetLocationModel currentMeetLocationModel
                                            , String truckName,int ID,NotificationModel notificationModel)
    {
        String CHANNEL_ID = "my_channel_01";
        Intent i = new Intent(context,GPS_Service.class);
        context.stopService(i);

        PendingIntent acceptIntent = createAcceptIntent( context,currentMeetLocationModel );
        PendingIntent skipIntent = createSkipIntent( context );
        PendingIntent cancelIntent= createCancelIntent( context );

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel( context );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("You are near " + truckName + ".Duration " +notificationModel.getDuration())
                .setContentText(" Do you want to add Tracking Service ?" )
                .setAutoCancel(true)
                .addAction(new NotificationCompat.Action(
                        R.mipmap.ic_thumb_up_black_36dp,
                        "Accept",
                        acceptIntent))
                .addAction(new NotificationCompat.Action(
                        R.mipmap.ic_thumbs_up_down_black_36dp,
                        "Skip",
                       skipIntent))
                .addAction(new NotificationCompat.Action(
                        R.mipmap.ic_thumb_down_black_36dp,
                        "Cancel",
                        cancelIntent));
        builder.setTimeoutAfter( 20 );

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTI_ID, builder.build());
    }


    private static PendingIntent createCancelIntent(Context context) {
        Intent skipIntent = new Intent(context, CancelReceiver.class );
        skipIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        skipIntent.putExtra("notificationID", NOTI_ID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( context,1, skipIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
    private static PendingIntent createAcceptIntent(Context context,CurrentMeetLocationModel currentMeetLocationModel){
        Intent acceptIntent = new Intent();

        acceptIntent.setClass(context,AddTrackingServiceActivity.class);
        acceptIntent.putExtra("dataTracking1", new DataTracking(currentMeetLocationModel.getTrackableId(),"No Data",
                currentMeetLocationModel.getStartTime(),currentMeetLocationModel.getMeetTime(),currentMeetLocationModel.getStartTime()
                ,0.0,0.0,
                currentMeetLocationModel.getMeetLocationLatitude(),currentMeetLocationModel.getMeetLocationLongtitude()));
        acceptIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );

        PendingIntent pendingIntent = PendingIntent.getActivity( context,0, acceptIntent,PendingIntent.FLAG_ONE_SHOT );
        return pendingIntent;
    }
    private static PendingIntent createSkipIntent(Context context){
        Intent skipIntent = new Intent(context, SkipReceiver.class );
        skipIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );

        PendingIntent pendingIntent = PendingIntent.getBroadcast( context,1, skipIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
    private static void createChannel(Context context){
        String CHANNEL_ID = "my_channel_01";
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
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
        }
    }


}