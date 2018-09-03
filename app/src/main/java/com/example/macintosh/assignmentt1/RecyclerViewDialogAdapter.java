package com.example.macintosh.assignmentt1;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;


public class RecyclerViewDialogAdapter extends RecyclerView.Adapter<RecyclerViewDialogAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {

    TextView date;
    TextView trackableID;
    TextView stoptime;
    TextView longtitude;
    TextView latitude;


        public ViewHolder (View itemView) {
            super(itemView);
            date= itemView.findViewById(R.id.date);
            trackableID = itemView.findViewById( R.id.trackableID );
            stoptime = itemView.findViewById( R.id.stoptime );
            longtitude = itemView.findViewById( R.id.longtitude );
            latitude = itemView.findViewById( R.id.latitude );
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

    //INITIALIE VH
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_cardview,parent,false);
        ViewHolder holder=new ViewHolder(v);
        return holder;
    }

    //BIND DATA
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.date.setText( trackingData1.get( position ).getDate().toString() );
            holder.trackableID.setText( Integer.toString( trackingData1.get( position ).getTrackableId() ) );
            holder.stoptime.setText( Integer.toString( trackingData1.get( position ).getStopTime() ) );
            holder.latitude.setText( Double.toString( trackingData1.get( position ).getLatitude() ) );
            holder.longtitude.setText( Double.toString( trackingData1.get( position ).getLongitude() ) );
    }

    @Override
    public int getItemCount() {
        return trackingData1.size();
    }
    private ArrayList<DataTrackingModel> compartID(ArrayList<DataTrackingModel> dataTrackingModels, int position){
        ArrayList<DataTrackingModel> dataTrackingModels1 = new ArrayList<>(  );
        int key = position1 + 1;
        for(int i = 0 ; i < dataTrackingModels .size();i++) {
            System.out.println("Tracking ID is "+dataTrackingModels.get( i ).getTrackableId());
            if (key == dataTrackingModels.get( i ).getTrackableId()) {

                dataTrackingModels1.add(dataTrackingModels.get(i));
            }
        }
        return dataTrackingModels1;
    }
}