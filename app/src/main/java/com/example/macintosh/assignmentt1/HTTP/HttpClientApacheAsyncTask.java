package com.example.macintosh.assignmentt1.HTTP;

import android.util.DisplayMetrics;
import android.util.Log;

import com.example.macintosh.assignmentt1.Activities.MainActivity;
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



// example of HttpURLConnection by Caspar, updates sem 2, 2018
public class HttpClientApacheAsyncTask extends AbstractHttpAsyncTask
{
   private String LOG_TAG = HttpClientApacheAsyncTask.class.getName();

   public HttpClientApacheAsyncTask(MainActivity activity)
   {
      super(activity);
   }

   @Override
   // Runs in the worker thread
   protected Void doInBackground(Void... unused)
   {
      HttpClient httpclient = new DefaultHttpClient();
      HttpGet getRequest = new HttpGet( DistanceURL);

      try
      {
         Log.i(LOG_TAG, "starting");
         // the easy way using BasicResponseHandler
         String responseBody = httpclient.execute(getRequest,
                 new BasicResponseHandler());
         // log the full HTML
         Log.i(LOG_TAG, responseBody);

         Log.i(LOG_TAG, "MANUAL with progress:");

         // the manual way retrieving a content entity
         HttpResponse response = httpclient.execute(getRequest);
         HttpEntity entity = response.getEntity();
         logHeaders(response.getAllHeaders());

         if (entity != null)
         {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    entity.getContent()));

            // for progress updates
            int length = (int) entity.getContentLength();

            // some sites don't always set the length so we guess!
            // OPTION: could switch to indeterminate progress widget and display
            // ongoing data volume downloaded
            if (length == -1)
               length = 11000;

            Log.i(LOG_TAG, "getContentLength()=" + length);

            String line;
            // read chunk at a time so we can publish progress
            while ((line = br.readLine()) != null)
            {
               // log individual line
               //Log.i(LOG_TAG, line);
               json += line;
               //htmlStringBuilder.append(line);
               doProgress(line.length(), length);
            }
            // finished
            publishProgress(100);
            JSON.responseJSON( responseBody );

            // Log.i(LOG_TAG, htmlStringBuilder.toString());
            Log.i(LOG_TAG, "DONE");

            // UNCOMMENT following if you want to display the responseBody which holds the Entity
            // content instead
            // htmlStringBuilder=new StringBuilder();
            // htmlStringBuilder.append(responseBody);
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return null;
   }

   private void logHeaders(Header[] headers)
   {
      StringBuffer sb = new StringBuffer();
      // since already using a StringBuffer will not use String.format() here
      for (Header header : headers)
         sb.append("Header: ").append(header.getName()).append(", Value: ").append(header.getValue())
                 .append('\n');
      Log.i(LOG_TAG, sb.toString());
   }
}
