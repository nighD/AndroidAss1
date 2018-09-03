package com.example.macintosh.assignmentt1;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import android.widget.Toast;

public class RecyclerViewDialogAdapter extends RecyclerView.Adapter<RecyclerViewDialogAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView trackableID;
        TextView stoptime;
        TextView longtitude;
        TextView latitude;
        ImageButton Delete;
        public ViewHolder (View itemView) {
            super(itemView);
            date= itemView.findViewById(R.id.date);
            trackableID = itemView.findViewById( R.id.trackableID );
            stoptime = itemView.findViewById( R.id.stoptime );
            longtitude = itemView.findViewById( R.id.longtitude );
            latitude = itemView.findViewById( R.id.latitude );
            Delete = itemView.findViewById( R.id.deleteButton );
        }
    }
    Context c;
    ArrayList<DataTrackingModel> trackingData;
    ArrayList<DataTrackingModel> trackingData1;
    ArrayList<DataModel> trackableData;
    int position1;
    public RecyclerViewDialogAdapter(Context c, ArrayList<DataTrackingModel> trackingData, ArrayList<DataModel> trackableData,int position) throws ParseException {
        this.c = c;
        this.trackingData = trackingData;
        this.trackableData = trackableData;

        this.position1 = position;
        this.trackingData1 = compartID( trackingData,position1 );
        if (trackingData1.isEmpty()){
            trackingData1.add(new DataTrackingModel(
                    DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse("00/00/0000 0:00:00 AM")
                    , 0,0,0,0 ));
        }
    }
    public void AddTrackingData(DataTrackingModel dataTrackingModel)
    {
        trackingData1.add(0,new DataTrackingModel(dataTrackingModel.date,dataTrackingModel.trackableId,dataTrackingModel.stopTime,dataTrackingModel.latitude,dataTrackingModel.longitude) );
        trackingData.add(0,new DataTrackingModel(dataTrackingModel.date,dataTrackingModel.trackableId,dataTrackingModel.stopTime,dataTrackingModel.latitude,dataTrackingModel.longitude) );
    }
    public void RemoveTrackingData(int position )
    {
        trackingData1.remove(position);
        trackingData.remove(position);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_cardview,parent,false);
        ViewHolder holder=new ViewHolder(v);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.date.setText("Date: " + trackingData1.get( position ).getDate().toString() );
        holder.stoptime.setText( "Stop Time : " + Integer.toString( trackingData1.get( position ).getStopTime() ) );
        holder.latitude.setText( "Latitude : "+ Double.toString( trackingData1.get( position ).getLatitude() ) );
        holder.longtitude.setText( "Longtitude : "+Double.toString( trackingData1.get( position ).getLongitude() ) );

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveTrackingData(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, trackingData1.size());
                notifyItemRangeChanged(position,trackingData.size());
                Toast.makeText(c,"Removed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackingData1.size();
    }
    private ArrayList<DataTrackingModel> compartID(ArrayList<DataTrackingModel> dataTrackingModels, int position){
        ArrayList<DataTrackingModel> dataTrackingModels1 = new ArrayList<>(  );
        int key = position1 + 1;
        for(int i = 0 ; i < dataTrackingModels .size();i++) {
            if (key == dataTrackingModels.get( i ).getTrackableId()) {
                dataTrackingModels1.add(dataTrackingModels.get(i));
            }
        }
        return dataTrackingModels1;
    }
//    public View.OnClickListener onClickListener(final int position) {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////
//            }
//        };
//    }
}