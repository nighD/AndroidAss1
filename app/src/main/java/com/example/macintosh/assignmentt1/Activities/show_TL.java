package com.example.macintosh.assignmentt1.Activities;

import android.app.Activity;
import android.content.Intent;
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
import java.util.Date;

public class show_TL extends AppCompatActivity {
    private Trackable trackable;
    private TrackingService trackingService;
    RecyclerView rv;
    ShowTrackingListAdapter adapter;
    private ArrayList<DataTrackingModel> trackingData;
    private ArrayList<DataModel> dataa;
    private ArrayList<ArrayList<DataTrackingModel>> Big_Track_list;
    private Activity activity;
    int finalPosition;
    //private LayoutInflater inflater;
    //private static ArrayList<DataTracking> dataTrackings = new ArrayList<>();
//    public show_TL showTl(final int num, ArrayList<DataTrackingModel> dataTrackingModels){
//        show_TL show = new show_TL();
//        finalPosition = num;
//        this.trackingData = dataTrackingModels;
//        return show;
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_tracking_list);
        dataa = new ArrayList<>( );
        trackingData = new ArrayList<>();
        trackable = new Trackable();
        Big_Track_list = new ArrayList<>();
        Intent mIntent = getIntent();
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

        for(int i = 0; i < trackable.trackableList.size(); i++)
        {
            Big_Track_list.add(new ArrayList<DataTrackingModel>());
        }
        for (int i = 0; i < trackingService.trackingList.size(); i++ ) {
            for(int j = 0; j < trackable.trackableList.size(); j++){
                if((trackingService.trackingList.get(i).trackableId==j+1)&&(trackingService.trackingList.get(i).stopTime!=0)){
                    Big_Track_list.get(j).add(trackingData.get(i));
//                    dataTrackingModels2 = new ArrayList<>();
                    // dataTrackingModels2.add(dataTracking.get(i));
                }
            }
        }
        for (int i = 0; i < trackable.trackableList.size(); i++){
            if (Big_Track_list.get(i).isEmpty()) {
                this.Big_Track_list.get(i).add(new DataTrackingModel(new Date(),0,i+1,0,0,0));
            }
        }
        rv= (RecyclerView) findViewById(R.id.tracking_list_RV);

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //ADAPTER
        try {
            adapter=new ShowTrackingListAdapter(dataa,Big_Track_list.get(mIntent.getIntExtra("CellPosition",0)),getApplicationContext(),this);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rv.setAdapter(adapter);
    }
}
