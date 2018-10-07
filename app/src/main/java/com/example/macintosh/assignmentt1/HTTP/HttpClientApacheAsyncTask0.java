package com.example.macintosh.assignmentt1.HTTP;

import android.content.Context;
import android.util.Log;

import com.example.macintosh.assignmentt1.Activities.MainActivity;
import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.Receiver.AlarmReceiver;
import com.example.macintosh.assignmentt1.ModelClass.CurrentMeetLocationModel;
import com.example.macintosh.assignmentt1.ModelClass.NotificationModel;
import com.example.macintosh.assignmentt1.ModelClass.ResponseJSON;
import com.example.macintosh.assignmentt1.json.JSON;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class HttpClientApacheAsyncTask0 extends AbstractHttpAsyncTask0
{
    private String LOG_TAG = HttpClientApacheAsyncTask0.class.getName();
    public ResponseJSON response;
    private String DistURL;
    private AlarmReceiver alarmReceiver;
    //private Context context;


    public HttpClientApacheAsyncTask0(MainActivity activity, String URL, Context context, DataTracking dataTracking0, LatLng latLng0)
    {
        super(activity);
        Log.i(LOG_TAG,URL);
        this.context = context;
        DistURL = URL;
        this.dataTracking = dataTracking0;
        this.latLng = latLng0;

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
            // the easy way using BasicResponseHandler
            String responseBody = httpclient.execute(getRequest,
                    new BasicResponseHandler());
            // log the full HTML
            //Log.i(LOG_TAG, responseBody);

            Log.i(LOG_TAG, "MANUAL with progress:");

            // the manual way retrieving a content entity
            HttpResponse response = httpclient.execute(getRequest);
            HttpEntity entity = response.getEntity();
            //logHeaders(response.getAllHeaders());

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

                //Log.i(LOG_TAG, "getContentLength()=" + length);

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
                this.response = JSON.responseJSON( responseBody );
                int ID = this.response.getID();
                String destination = this.response.getDestination();
                String duration = this.response.getDuration();
                notificationModel = new NotificationModel( ID,destination,duration );
//            Intent i = new Intent("location_and_duration");
//            Log.i(LOG_TAG,this.response.getDistance());
//            //this.response.setID( trackableID );
//            i.putExtra("destination",this.response.getDestination().toString());
//            i.putExtra("duration",this.response.getDuration().toString() );
//            i.putExtra( "trackableID",this.response.getID().toString());
//            context.sendBroadcast(i);
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

    public ResponseJSON getResponse(){ return this.response;}

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
