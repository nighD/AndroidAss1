package com.example.macintosh.assignmentt1.HTTP;

import android.content.Context;
import android.util.Log;

import com.example.macintosh.assignmentt1.Activities.MainActivity;
import com.example.macintosh.assignmentt1.Receiver.AlarmReceiver;
import com.example.macintosh.assignmentt1.ModelClass.CurrentMeetLocationModel;
import com.example.macintosh.assignmentt1.ModelClass.NotificationModel;
import com.example.macintosh.assignmentt1.ModelClass.ResponseJSON;
import com.example.macintosh.assignmentt1.json.JSON;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class HttpClientApacheAsyncTask extends AbstractHttpAsyncTask
{
   private String LOG_TAG = HttpClientApacheAsyncTask.class.getName();
   public ResponseJSON response;
   private String DistURL;
   private AlarmReceiver alarmReceiver;
   //private Context context;


   public HttpClientApacheAsyncTask(MainActivity activity, String URL, Context context, CurrentMeetLocationModel currentMeetLocationModel0)
   {
      super(activity);
      Log.i(LOG_TAG,URL);
      this.context = context;
      DistURL = URL;
      currentMeetLocationModel = currentMeetLocationModel0;

   }

   @Override
   // Runs in the worker thread
   protected Void doInBackground(Void... unused)
   {
      HttpClient httpclient = new DefaultHttpClient();
      HttpGet getRequest = new HttpGet( DistURL);

      try
      {
         Log.i(LOG_TAG, "starting");
         String responseBody = httpclient.execute(getRequest,
                 new BasicResponseHandler());
         Log.i(LOG_TAG, "MANUAL with progress:");

         HttpResponse response = httpclient.execute(getRequest);
         HttpEntity entity = response.getEntity();
         if (entity != null)
         {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    entity.getContent()));

            // for progress updates
            int length = (int) entity.getContentLength();
            if (length == -1)
               length = 11000;

            String line;
            while ((line = br.readLine()) != null)
            {

               json += line;
               doProgress(line.length(), length);
            }
            // finished
            publishProgress(100);
            this.response = JSON.responseJSON( responseBody );
            int ID = this.response.getID();
            String destination = this.response.getDestination();
            String duration = this.response.getDuration();
            notificationModel = new NotificationModel( ID,destination,duration );
            Log.i(LOG_TAG, "DONE");
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return null;
   }

   public ResponseJSON getResponse(){ return this.response;}

   private void logHeaders(Header[] headers)
   {
      StringBuffer sb = new StringBuffer();
      for (Header header : headers)
         sb.append("Header: ").append(header.getName()).append(", Value: ").append(header.getValue())
                 .append('\n');
      Log.i(LOG_TAG, sb.toString());
   }

}
