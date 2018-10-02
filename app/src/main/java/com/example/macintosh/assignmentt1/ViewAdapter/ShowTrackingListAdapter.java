package com.example.macintosh.assignmentt1.ViewAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macintosh.assignmentt1.Activities.ShowFragment;
import com.example.macintosh.assignmentt1.Interfaces.ItemClickListener;
import com.example.macintosh.assignmentt1.ModelClass.DataModel;
import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.ModelClass.DataTrackingModel;
import com.example.macintosh.assignmentt1.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ShowTrackingListAdapter extends RecyclerView.Adapter<ShowTrackingListAdapter.MyViewHolder> {


    private Context ctx;
    private ArrayList<DataModel> dataSet;
    private ArrayList<DataTrackingModel> dataTrackingModels;
    private Activity activity;
    String DATE_FORMAT = "MM/dd/yyyy";
    String TIME_FORMAT = "hh:mm";
    SimpleDateFormat stf = new SimpleDateFormat(TIME_FORMAT);
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView trackableID;
        private TextView trackingDate;
        private TextView startTime;
        private TextView stopTime;
        private TextView meetLocation;
        private View container;




        ItemClickListener itemClickListener;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.trackableID = itemView.findViewById(R.id.trackable_id);
            this.trackingDate = itemView.findViewById(R.id.tracking_date);
            this.startTime = itemView.findViewById(R.id.startTime);
            this.stopTime = itemView.findViewById(R.id.stopTimee);
            this.meetLocation = itemView.findViewById(R.id.meet_loc);
            container = itemView.findViewById(R.id.card_view);
        }


    }

    public ShowTrackingListAdapter(ArrayList<DataModel> data, ArrayList<DataTrackingModel> dataTracking,Context ctx, Activity activity) throws ParseException {
        this.dataSet = data;
        this.ctx = ctx;
        this.activity = activity;
        this.dataTrackingModels = dataTracking;
    }
    @Override
    public ShowTrackingListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.trackinglist_item_cardview,parent,false);
        final MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ShowTrackingListAdapter.MyViewHolder holder,int position) {
        TextView textViewID = holder.trackableID;
        TextView textViewDate = holder.trackingDate;
        TextView textViewStartTime = holder.startTime;
        TextView textViewStopTime = holder.stopTime;
        TextView textViewMeetLoc = holder.meetLocation;
            textViewID.setText("ID: "+dataTrackingModels.get(position).getTrackableId());
            textViewDate.setText("Date: "+sdf.format(dataTrackingModels.get(position).getDate()));
            textViewStartTime.setText("Start time: "+stf.format(dataTrackingModels.get(position).getDate()));
            textViewStopTime.setText("Stop time: "+Integer.toString(dataTrackingModels.get(position).getStopTime())+" mins");
            textViewMeetLoc.setText("Meet Location: "+dataTrackingModels.get(position).getLatitude() + ", " + dataTrackingModels.get(position).getLongitude());
    }

    @Override
    public int getItemCount() {
        return dataTrackingModels.size();
    }
    public interface RecyclerViewAdapterListener {
        void onContactSelected(DataModel dataModel);
    }
}


