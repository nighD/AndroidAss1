package com.example.macintosh.assignmentt1.ViewAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
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

    public void onBindViewHolder(final ViewHolder holder, final int position) {
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

            holder.Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu editTime = new PopupMenu(c,holder.Edit);
                    editTime.getMenuInflater().inflate(R.menu.edit_meet_time_menu, editTime.getMenu());
                    editTime.getMenu().clear();
                    for (int i = 1; i < (dataTrackings.get(position).getEndTime().getMinutes()-dataTrackings.get(position).getStartTime().getMinutes()); i++){
                        editTime.getMenu().add(1,R.id.timeSlot1,i,dataTrackings.get(position).getStartTime().getHours()+":"+dataTrackings.get(position).getStartTime().getMinutes()+i);
                    }
                    editTime.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            dataTrackings.get(position).getMeetTime().setMinutes((dataTrackings.get(position).getMeetTime().getMinutes()+menuItem.getItemId()+1));
                            holder.meettime.setText("Meet Time: "+stf.format(dataTrackings.get(position).getMeetTime()));
                            notifyItemRangeChanged(position,dataTrackings.size());
//                        notifyDataSetChanged();
//                        notifyItemInserted(position);
                            return true;
                        }
                    });
                    editTime.show();
                }
            });
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
    public void addTrackingData(int position,int ID, String title, Date startTime, Date endTime, Date meetTime, double currLat, double currLong,
                                double meetLat, double meetLong)
    {
        this.dataTrackings.add(0,new DataTracking(ID,title,startTime,endTime,meetTime,currLat,currLong,meetLat,meetLong));
    }
    public void removeTrackingData(int position)
    {
        this.dataTrackings.remove(position);
    }
}