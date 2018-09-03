package com.example.macintosh.assignmentt1;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;
import org.w3c.dom.Text;

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


    TextView trackableID;
    TextView title;
    TextView starttime;
    TextView endtime;
    TextView meettime;
    TextView currentlocation;
    TextView meetlocation;
    ImageButton Delete;
    ImageButton Edit;
//    Button Add;
        TextView date;

        TextView stoptime;
        TextView longtitude;
        TextView latitude;

        public ViewHolder (View itemView) {
            super(itemView);

            trackableID = itemView.findViewById( R.id.trackableID );
            title = itemView.findViewById( R.id.title );
            starttime = itemView.findViewById( R.id.starttime );
            endtime = itemView.findViewById( R.id.endtime );
            meettime = itemView.findViewById( R.id.meettime );
            currentlocation = itemView.findViewById( R.id.currentLocation );
            meetlocation = itemView.findViewById( R.id.meetLocation );
            Delete = itemView.findViewById( R.id.deleteButton );
            Edit = itemView.findViewById( R.id.edit );
//            Add = itemView.findViewById( R.id.btn_add );

        }
    }
    Context c;
    ArrayList<DataTrackingModel> trackingData;
    ArrayList<DataTrackingModel> trackingData1;
     static ArrayList<DataTracking> dataTrackings;
    ArrayList<DataModel> trackableData;
    int position1;
    Dialog dialog;
    public RecyclerViewDialogAdapter(Context c, ArrayList<DataTrackingModel> trackingData, ArrayList<DataModel> trackableData,int position,ArrayList<DataTracking> dataTrackings1) throws ParseException {
        this.c = c;
        this.trackingData = trackingData;
        //dataTrackings = new ArrayList<>(  );
        this.trackableData = trackableData;
        this.dataTrackings = dataTrackings1;
        this.position1 = position;
        this.trackingData1 = compartID( trackingData,position1 );
//        addTrackingData(position, "lalala", new Date(), new Date(),new Date(), 0.0,0.1,0.2,0.3);

        if (dataTrackings1.isEmpty()){
            this.dataTrackings.add(new DataTracking(position1 +1,"No Tracking Data",
                    DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse("00/00/0000 0:00:00 AM"),
                    DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse("00/00/0000 0:00:00 AM"),
                    DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse("00/00/0000 0:00:00 AM")
                    , 0,0,0,0 ));
        }
    }
    public void AddTrackingData(DataTrackingModel dataTrackingModel)
    {
        trackingData1.add(0,new DataTrackingModel(dataTrackingModel.date,dataTrackingModel.trackableId,dataTrackingModel.stopTime,dataTrackingModel.latitude,dataTrackingModel.longitude) );
        trackingData.add(0,new DataTrackingModel(dataTrackingModel.date,dataTrackingModel.trackableId,dataTrackingModel.stopTime,dataTrackingModel.latitude,dataTrackingModel.longitude) );
        this.dataTrackings.add(new DataTracking(position1 +1,"No tracking data",
                new Date(),
                new Date(),
                new Date()
                , 0,0,0,0 ));
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

    public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.Delete.setOnClickListener(onClickListener(position));
            holder.trackableID.setText("Trackable ID: "+ Integer.toString( dataTrackings.get(position).trackableId ));
            holder.title.setText("Title: "+ dataTrackings.get(position).title );
            holder.starttime.setText("Start Time: "+ dataTrackings.get(position).starttime.toString() );
            holder.endtime.setText("End Time: " +dataTrackings.get( position ).endtime.toString());
            holder.meettime.setText( "Meet Time: "+dataTrackings.get(position).meettime.toString() );
            holder.currentlocation.setText("Current Location: "+ Double.toString(dataTrackings.get( position ).currentLocationlatitude )
                    + Double.toString(dataTrackings.get( position ).currentLocationlongtitude ) );
            holder.meetlocation.setText("Meet Location: "+ Double.toString(dataTrackings.get( position ).meetLocationlatitude )
                    + Double.toString(dataTrackings.get( position ).meetLocationlongtitude ) );



    }

    @Override
    public int getItemCount() {
        return dataTrackings.size();
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

    public View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(c);
                dialog.setTitle( "Add" );
                dialog.setContentView(R.layout.dialog_template);
                final EditText Write = dialog.findViewById(R.id.writeTitle);
                Button SaveMyName = dialog.findViewById(R.id.SaveNow);
                final DatePicker datePicker =  dialog.findViewById(R.id.date_picker);
                 final TimePicker startTimePicker =  dialog.findViewById(R.id.start_time_picker);
                 final TimePicker endTimePicker = dialog.findViewById(R.id.end_time_picker);
                final EditText WriteMeetLong = dialog.findViewById(R.id.writeMeetLong);
                Write.setEnabled(true);
                SaveMyName.setEnabled(true);

                SaveMyName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dataTrackings.get( 0 ).title = Write.getText().toString();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set( datePicker.getMonth(),datePicker.getDayOfMonth(),endTimePicker.getHour() - startTimePicker.getHour(),endTimePicker.getMinute()-startTimePicker.getMinute(),0 );
                        Date date = calendar.getTime();
                        dataTrackings.get( 0 ).meettime = date;
                        addTrackingData(position, Write.getText().toString(), date, date,date, 0.0,0.1,0.2,0.3);
                        notifyDataSetChanged();
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        };
    }
    public void SharedPrefesSAVE(String Name){
        SharedPreferences prefs = c.getSharedPreferences("NAME", 0);
        SharedPreferences.Editor prefEDIT = prefs.edit();
        prefEDIT.putString("Name", Name);
        prefEDIT.commit();
    }

    public void addTrackingData(int ID, String title, Date startTime, Date endTime, Date meetTime, double currLat, double currLong,
                                double meetLat, double meetLong)
    {
        this.dataTrackings.add(0,new DataTracking(ID,title,startTime,endTime,meetTime,currLat,currLong,meetLat,meetLong));
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