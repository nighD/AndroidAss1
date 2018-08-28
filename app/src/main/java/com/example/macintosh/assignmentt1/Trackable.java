package com.example.macintosh.assignmentt1;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Trackable {
    public static final String LOG_TAG = TrackingService.class.getName();
    public List<trackableInfo> trackableList = new ArrayList<>();
    public static Context context;
    public Trackable(){
    }
    public static class trackableInfo{
        private int ID;
        public String name;
        public String description;
        public String webURL;
        public String Category;
        @Override
        public String toString()
        {
            return String.format(Locale.getDefault(), "ID: %d, name : %s, description : %s, webURL : %s, category : %s", ID, name ,description,webURL,Category);
        }

    }
    public void parseFile(Context context)
    {
        trackableList.clear();
        // resource reference to tracking_data.txt in res/raw/ folder of your project
        // supports trailing comments with //
        try (Scanner scanner = new Scanner(context.getResources().openRawResource(R.raw.food_truck_data)))
        {
            // match comma and 0 or more whitespace OR trailing space and newline
            scanner.useDelimiter(",\"|\",\"|\"\\n");
            //scanner.useDelimiter(",|");
            while (scanner.hasNext())
            {
                trackableInfo trackInfo = new trackableInfo();
                trackInfo.ID = Integer.parseInt(scanner.next);
                trackInfo.name = scanner.next();
                trackInfo.description = scanner.next();
                trackInfo.webURL = scanner.next();
                trackInfo.Category = scanner.next();
                trackableList.add(trackInfo);
            }
        }
        catch (Resources.NotFoundException e)
        {
            Log.i(LOG_TAG, "File Not Found Exception Caught");
        }

    }
}
