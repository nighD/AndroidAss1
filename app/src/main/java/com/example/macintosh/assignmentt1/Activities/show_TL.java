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
    RecyclerView rv;
    ShowTrackingListAdapter adapter;
    private ArrayList<DataTrackingModel> trackingData;
    private ArrayList<DataModel> dataa;
    private ArrayList<ArrayList<DataTrackingModel>> Big_Track_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_tracking_list);
        dataa = new ArrayList<>( );
        trackingData = new ArrayList<>();
        Big_Track_list = new ArrayList<>();
        Intent mIntent = getIntent();
        dataa = (ArrayList<DataModel>) mIntent.getSerializableExtra("dataModels");
        Big_Track_list = (ArrayList<ArrayList<DataTrackingModel>>)mIntent.getSerializableExtra("dataTrackingModels");
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
