package com.example.macintosh.assignmentt1.HTTP;

import android.util.Log;

import com.example.macintosh.assignmentt1.Activities.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;



// example of HttpURLConnection by Caspar, updated sem 2, 2018
public class HttpURLConnectionAsyncTask extends AbstractHttpAsyncTask
{
   private final String LOG_TAG = HttpURLConnectionAsyncTask.class.getName();

   public HttpURLConnectionAsyncTask(MainActivity activity)
   {
      super(activity);
   }

   @Override
   protected Void doInBackground(Void... unused)
   {
      // HttpURLConnection is not AutoCloseable so cannot use try with resources
      HttpURLConnection connection = null;
      try
      {
         Log.i(LOG_TAG, "Starting ..");
         URL url = new URL(TEST_URL);

         // set some connection properties
         connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod("GET");
         // see http://en.wikipedia.org/wiki/List_of_HTTP_header_fields
         connection.setRequestProperty("Accept", "text/html");
         // milliseconds
         connection.setReadTimeout(5000);
         connection.setConnectTimeout(5000);

         // check the response code
         int statusCode = connection.getResponseCode();
         if (statusCode != HttpURLConnection.HTTP_OK)
         {
            Log.e(LOG_TAG, "Invalid Response Code: " + statusCode);
            return null;
         }

         logHeaders(connection.getHeaderFields());

         // wrap the stream to extract its contents (the HTML at the given URL)
         BufferedReader br = new BufferedReader(new InputStreamReader(
                 connection.getInputStream()));

         // calculate number of blocks for progress updates
         int length = connection.getContentLength();

         // some sites don't set the length so we guess!
         // OPTION: could switch to indeterminate progress widget
         if (length == -1)
            length = 50000;

         Log.i(LOG_TAG, "getContentLength()=" + length);

         String line;
         while ((line = br.readLine()) != null)
         {
            // log individual line
            //Log.i(LOG_TAG, line);
            htmlStringBuilder.append(line);
            doProgress(line.length(), length);
         }

         // finished
         publishProgress(100);
         // Log.i(LOG_TAG, htmlStringBuilder.toString());
         Log.i(LOG_TAG, "DONE");
      }
      catch (Exception e)
      {
         Log.e(LOG_TAG, "Exception caught: " + e.getMessage());
         e.printStackTrace();
      }
      finally
      {
         if (connection != null)
            connection.disconnect();
      }

      return null;
   }

   protected void logHeaders(Map<String, List<String>> headers)
   {
      Set<String> keySet = headers.keySet();
      StringBuffer sb = new StringBuffer();

      for (String key : keySet)
      {
         sb.append("Header: " + key + ", Values: ");
         for (String value : headers.get(key))
            sb.append(value + " ");
         sb.append('\n');
      }

      Log.i(LOG_TAG, sb.toString());
   }
}
