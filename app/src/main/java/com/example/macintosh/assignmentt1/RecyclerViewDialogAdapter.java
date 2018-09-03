package com.example.macintosh.assignmentt1;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    public RecyclerViewDialogAdapter(Context c, ArrayList<DataTrackingModel> trackingData, ArrayList<DataModel> trackableData,int position) {
        this.c = c;
        this.trackingData = trackingData;
        this.trackableData = trackableData;
        //this.trackingData1 = compartID( trackingDat;
        this.position1 = position;
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
        System.out.println("Postition1 is "+position1);
        for(int i = 0 ; i < trackingData .size();i++) {
            System.out.println("Tracking ID is "+trackingData.get( i ).getTrackableId());
            if ((position1+1) == trackingData.get( i ).getTrackableId()) {

                holder.date.setText( trackingData.get( i ).getDate().toString() );
                holder.trackableID.setText( Integer.toString( trackingData.get( i ).getTrackableId() ) );
                holder.stoptime.setText( Integer.toString( trackingData.get( i ).getStopTime() ) );
                holder.latitude.setText( Double.toString( trackingData.get( i ).getLatitude() ) );
                holder.longtitude.setText( Double.toString( trackingData.get( i ).getLongitude() ) );
            }
        }

    }

    @Override
    public int getItemCount() {
        return trackingData.size();
    }
//    private ArrayList<DataTrackingModel> compartID(ArrayList<DataTrackingModel> dataTrackingModels, int position){
//        ArrayList<DataTrackingModel> dataTrackingModels1 = new ArrayList<>(  );
//        for(int a = 0 ;a<dataModels.size();a++) {
//            for (int i = 0; i < dataTrackingModels.size(); i++) {
//                if (Integer.parseInt( dataModels.get( a ).getImage() ) == dataTrackingModels.get( i ).getTrackableId())
//                    dataTrackingModels1.add( dataTrackingModels.get( i ) );
//
//
//            }
//        }
//        return dataTrackingModels1;
//    }
}