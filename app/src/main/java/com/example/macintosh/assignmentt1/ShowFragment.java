package com.example.macintosh.assignmentt1;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.text.DateFormat;

import android.widget.Button;
import java.time.LocalDateTime;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class ShowFragment extends DialogFragment  {
    int finalPosition;
    private Button addButton;
    private Trackable trackable;
    private TrackingService trackingService;
    public ShowFragment newInstance(final int num) {
       ShowFragment f = new ShowFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        finalPosition = num;
        System.out.println("Num is: "+num);
        return f;
    }
    RecyclerView rv;
    RecyclerViewDialogAdapter adapter;
    private ArrayList<DataTrackingModel> trackingData;
    private ArrayList<DataModel> dataa;
    private ArrayList<DataTracking> dataTrackings;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //String strtext=getArguments().getString("ID");

        Bundle aa = this.getArguments();
        if (aa != null) {


        finalPosition = aa.getInt("num");}
        System.out.println("finalPosition is: "+finalPosition);
        final View rootView=inflater.inflate(R.layout.fraglayout,container);

        //RECYCER
        dataa = new ArrayList<>( );
        trackingData = new ArrayList<>();
        trackable = new Trackable();
        trackable.parseFile( this.getContext() );
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
        trackingService.parseFile(this.getContext());
        for (int i = 0; i < trackingService.trackingList.size(); i++){
            trackingData.add(new DataTrackingModel(trackingService.trackingList.get(i).date,
                    trackingService.trackingList.get(i).trackableId,
                    trackingService.trackingList.get(i).stopTime,
                    trackingService.trackingList.get(i).latitude,
                    trackingService.trackingList.get(i).longitude));
        }
     dataTrackings = new ArrayList<>(  );

        addButton = (Button) rootView.findViewById(R.id.btn_add);

        rv= (RecyclerView) rootView.findViewById(R.id.mRecyerID);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        getDialog().setTitle("Tracking data: "+ "\r" + trackable.trackableList.get(finalPosition).name);
        //ADAPTER
        try {
            adapter=new RecyclerViewDialogAdapter(this.getActivity(),trackingData,dataa,finalPosition,dataTrackings);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rv.setAdapter(adapter);
        final Context ctx = this.getContext();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                int i = trackable.trackableList.size();
                trackingData.add(0,new DataTrackingModel(date, finalPosition+1, 2, 954.3, 107.5));
                adapter.AddTrackingData(new DataTrackingModel(date, finalPosition+1, 2, 954.3, 107.5));
                adapter.notifyItemInserted(i);
                rv.scrollToPosition(i);

                // Show the added item label
                Toast.makeText(ctx,"Added",Toast.LENGTH_SHORT).show();
            }
        });

        getDialog().setCancelable(true);
        return rootView;
    }
}