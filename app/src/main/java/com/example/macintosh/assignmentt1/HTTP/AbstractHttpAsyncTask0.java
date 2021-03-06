package com.example.macintosh.assignmentt1.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.macintosh.assignmentt1.Activities.MainActivity;
import com.example.macintosh.assignmentt1.ModelClass.CurrentMeetLocationModel;
import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.ModelClass.NotificationModel;
import com.google.android.gms.maps.model.LatLng;

import java.net.URL;



public abstract class AbstractHttpAsyncTask0 extends AsyncTask<Void, Integer, Void>
{
    private static final String LOG_TAG = AbstractHttpAsyncTask0.class.getName();

    protected StringBuilder htmlStringBuilder = new StringBuilder();

    // this one has a valid CONTENT_LENGTH header
    public static NotificationModel notificationModel;
    public static String json;
    public static Context context;
    public static DataTracking dataTracking;
    public static LatLng latLng;
    public static LatLng latLng1;
    protected MainActivity activity = null;
    private int charsRead;

    public AbstractHttpAsyncTask0(MainActivity activity)
    {
        this.activity = activity;
    }

    @Override
    protected void onProgressUpdate(Integer... progress)
    {
        if (activity == null)
            Log.w(LOG_TAG, "onProgressUpdate() skipped -- no activity");
        else
        {
            // Log.i(LOG_TAG, "Task progress=" + progress[0] + "%");
            //activity.updateProgress(progress[0]);
        }
    }

    protected void doProgress(int charsRead, int length)
    {
        this.charsRead += charsRead;

        int progress = (int) ((double) this.charsRead / length * 100.0);
        publishProgress(progress);
    }



    @Override
    protected void onPostExecute(Void result)
    {
        activity.displayNoti(context,dataTracking,notificationModel,latLng,latLng1);
    }
}
