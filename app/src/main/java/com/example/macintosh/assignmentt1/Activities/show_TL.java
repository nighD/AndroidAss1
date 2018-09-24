package com.example.macintosh.assignmentt1.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.macintosh.assignmentt1.ModelClass.DataModel;
import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.ModelClass.DataTrackingModel;
import com.example.macintosh.assignmentt1.ModelClass.Trackable;
import com.example.macintosh.assignmentt1.ModelClass.TrackingService;
import com.example.macintosh.assignmentt1.R;
import com.example.macintosh.assignmentt1.ViewAdapter.RecyclerViewDialogAdapter;
import com.example.macintosh.assignmentt1.ViewAdapter.ShowTrackingListAdapter;

import java.text.ParseException;
import java.util.ArrayList;

public class show_TL extends AppCompatActivity {
    private Trackable trackable;
    private TrackingService trackingService;
    RecyclerView rv;
    ShowTrackingListAdapter adapter;
    private ArrayList<DataTrackingModel> trackingData;
    private ArrayList<DataModel> dataa;
    int finalPosition;
    //private LayoutInflater inflater;
    //private static ArrayList<DataTracking> dataTrackings = new ArrayList<>();
    public show_TL showTl(final int num, ArrayList<DataTrackingModel> dataTrackingModels){
        show_TL show = new show_TL();
        finalPosition = num;
        this.trackingData = dataTrackingModels;
        return show;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__tl);
        dataa = new ArrayList<>( );
        trackingData = new ArrayList<>();
        trackable = new Trackable();
        trackable.parseFile(this.getApplicationContext());
        for (int i = 0; i < trackable.trackableList.size(); i++) {
            dataa.add( new DataModel(
                    trackable.trackableList.get( i ).name,
                    trackable.trackableList.get( i ).description,
                    trackable.trackableList.get( i ).webURL,
                    trackable.trackableList.get( i ).Category,
                    trackable.trackableList.get( i ).getID()

            ) );
        }
        trackingService = new TrackingService();
        trackingService.parseFile(getApplicationContext());
        for (int i = 0; i < trackingService.trackingList.size(); i++){
            trackingData.add(new DataTrackingModel(trackingService.trackingList.get(i).date,
                    trackingService.trackingList.get(i).date.getTime(),
                    trackingService.trackingList.get(i).trackableId,
                    trackingService.trackingList.get(i).stopTime,
                    trackingService.trackingList.get(i).latitude,
                    trackingService.trackingList.get(i).longitude));
        }
        rv= findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //ADAPTER
        try {
            adapter=new ShowTrackingListAdapter(dataa,trackingData,finalPosition,getApplicationContext(),getParent());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rv.setAdapter(adapter);
    }
}
