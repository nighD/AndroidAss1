package com.example.macintosh.assignmentt1.ViewAdapter;

import android.app.Dialog;
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
import java.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.ArrayList;

import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.ModelClass.DataModel;
import com.example.macintosh.assignmentt1.ModelClass.DataTrackingModel;
import com.example.macintosh.assignmentt1.R;

public class RecyclerViewDialogAdapter extends RecyclerView.Adapter<RecyclerViewDialogAdapter.ViewHolder> {

    Context c;
    ArrayList<DataTrackingModel> trackingData;
    ArrayList<DataTrackingModel> trackingData1;
    static ArrayList<DataTracking> dataTrackings = new ArrayList<>();
    ArrayList<DataModel> trackableData;
    int position1;
    Dialog dialog;
    String DATE_FORMAT = "MM/dd/yyyy";
    String TIME_FORMAT = "hh:mm";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    SimpleDateFormat stf = new SimpleDateFormat(TIME_FORMAT);
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

        }
    }
    public RecyclerViewDialogAdapter(Context c, ArrayList<DataTrackingModel> trackingData, ArrayList<DataModel> trackableData,int position,ArrayList<DataTracking> dataTrackings) throws ParseException {
        this.c = c;
        this.trackingData = trackingData;
        this.trackableData = trackableData;
        this.dataTrackings = dataTrackings;
        this.position1 = position;
        this.trackingData1 = compartID( trackingData,position1 );

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
            holder.trackableID.setText("Trackable ID: "+ Integer.toString( dataTrackings.get(position).getTrackableId() ));
            holder.title.setText("Title: "+ dataTrackings.get(position).getTitle() );
            holder.starttime.setText("Start Time: "+ stf.format(dataTrackings.get(position).getStartTime()));
            holder.endtime.setText("End Time: " +stf.format(dataTrackings.get(position).getEndTime()));
            holder.meettime.setText( "Meet Time: "+stf.format(dataTrackings.get(position).getMeetTime()));
            holder.currentlocation.setText("Current Location: "+ Double.toString(dataTrackings.get(position).getCurrentLocationlatitude() )
                    +" "+ Double.toString(dataTrackings.get(position).getCurrentLocationlongtitude() ) );
            holder.meetlocation.setText("Meet Location: "+ Double.toString(dataTrackings.get(position).getMeetLocationlatitude() )
                    +" "+ Double.toString(dataTrackings.get(position).getMeetLocationlongtitude()) );

            holder.Edit.setOnClickListener(onEditClickListener(position));
            holder.Delete.setOnClickListener(onDeleteClickListener(position));

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

    public void SharedPrefesSAVE(String Name){
        SharedPreferences prefs = c.getSharedPreferences("NAME", 0);
        SharedPreferences.Editor prefEDIT = prefs.edit();
        prefEDIT.putString("Name", Name);
        prefEDIT.commit();
    }

    public View.OnClickListener onDeleteClickListener(final int position)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTrackingData(position);
                notifyItemRangeChanged(position,dataTrackings.size());
                notifyDataSetChanged();
                notifyItemRemoved(position);
            }
        };
    }
    public View.OnClickListener onEditClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(c);
                dialog.setTitle( "Edit" );
                dialog.setContentView(R.layout.dialog_template);
                final EditText Write = dialog.findViewById(R.id.writeTitle);
                Button SaveMyName = dialog.findViewById(R.id.SaveNow);
                final DatePicker datePicker =  dialog.findViewById(R.id.date_picker);
//                final TimePicker startTimePicker =  dialog.findViewById(R.id.start_time_picker);
                final TimePicker meetTimePicker = dialog.findViewById(R.id.meet_time_picker);
//                final EditText WriteCurrLoc = dialog.findViewById(R.id.writeCurrLoc);
//                final EditText WriteMeetLoc = dialog.findViewById(R.id.writeMeetLoc);
                Write.setEnabled(true);
                SaveMyName.setEnabled(true);

                SaveMyName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Calendar calendar = Calendar.getInstance();
//                        calendar.set( datePicker.getMonth(),datePicker.getDayOfMonth(),startTimePicker.getHour(),startTimePicker.getMinute(),0 );
//                        Date startTime = calendar.getTime();
//                        calendar.set( datePicker.getMonth(),datePicker.getDayOfMonth(),endTimePicker.getHour(),endTimePicker.getMinute(),0 );
//                        Date endTime = calendar.getTime();
                        calendar.set( datePicker.getMonth(),datePicker.getDayOfMonth(),meetTimePicker.getHour(),meetTimePicker.getMinute(),0 );
                        Date meetTime = calendar.getTime();
                        removeTrackingData(position);
//                        editTrackingData(position,position, Write.getText().toString(), startTime, endTime,meetTime, Double.parseDouble(WriteCurrLoc.getText().toString()),
//                                Double.parseDouble(WriteCurrLoc.getText().toString()),Double.parseDouble(WriteMeetLoc.getText().toString()),Double.parseDouble(WriteMeetLoc.getText().toString()));
                        editTrackingData(position,position1, Write.getText().toString(), new Date(), new Date(),meetTime, 0,
                                0,trackingData.get(position1).getLatitude(),trackingData.get(position1).getLongitude());
                        notifyItemRangeChanged(position,dataTrackings.size());
                        notifyDataSetChanged();
                        notifyItemInserted(position);
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        };
    }
    public void addTrackingData(int position,int ID, String title, Date startTime, Date endTime, Date meetTime, double currLat, double currLong,
                                double meetLat, double meetLong)
    {
        this.dataTrackings.add(0,new DataTracking(ID,title,startTime,endTime,meetTime,currLat,currLong,meetLat,meetLong));
    }
    public void editTrackingData(int position,int ID, String title, Date startTime, Date endTime, Date meetTime, double currLat, double currLong,
                                double meetLat, double meetLong)
    {
        this.dataTrackings.add(position,new DataTracking(ID,title,startTime,endTime,meetTime,currLat,currLong,meetLat,meetLong));
    }
    public void removeTrackingData(int position)
    {
        this.dataTrackings.remove(position);
    }

}