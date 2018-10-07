package com.example.macintosh.assignmentt1.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.macintosh.assignmentt1.Activities.MainActivity;
import com.example.macintosh.assignmentt1.ModelClass.CurrentMeetLocationModel;
import com.example.macintosh.assignmentt1.ModelClass.NotificationModel;

import java.net.URL;


// example of HttpURLConnection by Caspar, updated sem 2, 2018
public abstract class AbstractHttpAsyncTask extends AsyncTask<Void, Integer, Void>
{
   private static final String LOG_TAG = AbstractHttpAsyncTask.class.getName();

   protected StringBuilder htmlStringBuilder = new StringBuilder();

   // this one has a valid CONTENT_LENGTH header
   public static NotificationModel notificationModel;
   public static String json;
   public static Context context;
   public static CurrentMeetLocationModel currentMeetLocationModel;

   //"https://maps.googleapis.com/maps/api/distancematrix/json?origins=-37.814644,144.955412&destinations=-37.810045,144.964220&key=AIzaSyCDbBGQ8CaRLa4rvOhsaG-LO0Rxy0CUGxI"
   // this one does not always set the content length so makes progress tracking
   // difficult
   // static final String TEST_URL = "http://www.wikipedia.org/";

   protected MainActivity activity = null;
   private int charsRead;

   public AbstractHttpAsyncTask(MainActivity activity)
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
      // delay allows us to see progress on fast network!
      //Thread.sleep(1);
      // convert to percentage for progress update (standard 0..100 range)
      int progress = (int) ((double) this.charsRead / length * 100.0);
     // Log.i(LOG_TAG, Integer.toString(progress) + "%");
      publishProgress(progress);
   }

  // protected void updateURL(String URL){
//      this.DistanceURL = URL;
//   }

   @Override
   protected void onPostExecute(Void result)
   {
      activity.displayNotification(notificationModel,context,currentMeetLocationModel);
   }
}
