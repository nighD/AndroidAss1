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

public class ShowTrackingListAdapter extends RecyclerView.Adapter<ShowTrackingListAdapter.MyViewHolder> {


    private Context ctx;
    private ArrayList<DataModel> dataSet;
    private ArrayList<DataModel> dataSetFilter;
    private ArrayList<DataTrackingModel> dataTrackingModels;
    private static ArrayList<ArrayList<DataTrackingModel>> dataTrackings = new ArrayList<>();
    public DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    private Activity activity;
    private RecyclerViewAdapterListener listener;
    int position;
    int id;


    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView trackableID;
        private TextView trackingDate;
        private TextView startTime;
        private TextView stopTime;
        private TextView meetLocation;
        private View container;
        private CardView cardView;




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

    public ShowTrackingListAdapter(ArrayList<DataModel> data, ArrayList<DataTrackingModel> dataTracking,int position, Context ctx, Activity activity) throws ParseException {
        this.dataSet = data;
        this.ctx = ctx;
        this.position = position;
        this.activity = activity;
        this.dataSetFilter = data;
        this.dataTrackingModels = dataTracking;
//        this.dataTrackings = dataTrackings;
        for(int i = 0; i < dataTracking.size(); i++)
        {
            dataTrackings.add(new ArrayList<DataTrackingModel>());
        }
        for (int i = 0; i < dataTracking.size(); i++ ) {
            for(int j = 0; j < dataTracking.size(); j++){
                if(dataTracking.get(i).getTrackableId()==i+1){
                    dataTrackings.get(i).add(dataTracking.get(i));
                }
            }
            if (dataTrackings.get(i).isEmpty()) {
                this.dataTrackings.get(i).add(new DataTrackingModel(new Date(),0,i,0,0,0));
            }
        }
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
            textViewDate.setText(dataTrackingModels.get(position).getDate().toString());
            textViewStartTime.setText(Long.toString(dataTrackingModels.get(position).getStartTime()));
            textViewStopTime.setText(Integer.toString(dataTrackingModels.get(position).getStopTime()));
            textViewMeetLoc.setText(dataTrackingModels.get(position).getLatitude() + ", " + dataTrackingModels.get(position).getLongitude());
    }

    @Override
    public int getItemCount() {
        return dataSetFilter.size();
    }
    public interface RecyclerViewAdapterListener {
        void onContactSelected(DataModel dataModel);
    }
}


