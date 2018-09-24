package com.example.macintosh.assignmentt1.json;

import android.util.Log;

import com.example.macintosh.assignmentt1.ModelClass.ResponseJSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

// simple Android JSON example by Caspar
public class JSON
{
    private static final String LOG_TAG = JSON.class.getName();
    public static ResponseJSON responseJSON(String jsonString) throws Exception
    {
          JSONObject trackingInfo = new JSONObject(jsonString);
          JSONArray jsonArray1 = trackingInfo.getJSONArray( "destination_addresses" );
          JSONArray jsonArray2 = trackingInfo.getJSONArray( "origin_addresses" );
          JSONArray jsonObject1 = trackingInfo.getJSONArray("rows");
          JSONObject jsonObject2 = (JSONObject)jsonObject1.get(0);
          JSONArray jsonObject3 = (JSONArray)jsonObject2.get("elements");
          JSONObject elementObj = (JSONObject) jsonObject3.get(0);
          JSONObject distanceObj = (JSONObject)elementObj.get("distance");
          JSONObject durationObj = (JSONObject)elementObj.get("duration");
          String distance = distanceObj.getString("text");
          String duration = durationObj.getString("text");
          String origin = jsonArray1.get(0).toString();
          String destination = jsonArray2.get(0).toString();
          Log.i(LOG_TAG, "distance: " + distance);
          Log.i(LOG_TAG, "duration: " + duration);
          Log.i(LOG_TAG, "from : " + origin);
          Log.i(LOG_TAG, "to : " + destination);
          ResponseJSON responseJSON = new ResponseJSON( origin,destination,distance,duration );
          return responseJSON;
    }

}
