package com.example.macintosh.assignmentt1.ViewAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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

public class ShowTrackingListAdapter extends RecyclerView.Adapter<ShowTrackingListAdapter.MyViewHolder>
implements Filterable{


    private Context ctx;
    private ArrayList<DataModel> dataSet;
    private ArrayList<DataModel> dataSetFilter;
    private ArrayList<DataTrackingModel> dataTrackingModels;
    private static ArrayList<ArrayList<DataTracking>> dataTrackings = new ArrayList<>();
    public DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    private Activity activity;
    private RecyclerViewAdapterListener listener;
    int id;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
//            this.trackableID = itemView.findViewById(R.id.trackable_id);
//            this.trackingDate = itemView.findViewById(R.id.tracking_date);
//            this.startTime = itemView.findViewById(R.id.startTime);
//            this.stopTime = itemView.findViewById(R.id.stopTimee);
//            this.meetLocation = itemView.findViewById(R.id.meet_loc);
            container = itemView.findViewById(R.id.card_view);
        }
//        public void setItemClickListener(ItemClickListener itemClickListener)
//        {
//            this.itemClickListener=itemClickListener;
//        }

        @Override
        public void onClick(View v) {

            this.itemClickListener.onItemClick(v,getLayoutPosition() );

        }

    }

    public ShowTrackingListAdapter(ArrayList<DataModel> data, ArrayList<DataTrackingModel> dataTracking, Context ctx, Activity activity) throws ParseException {
        this.dataSet = data;
        this.ctx = ctx;
        this.activity = activity;
        this.dataSetFilter = data;
//        this.dataTrackingSet = dataTracking;
        this.dataTrackingModels = dataTracking;
//        this.dataTrackings = dataTrackings;
        for(int i = 0; i < data.size(); i++)
        {
            dataTrackings.add(new ArrayList<DataTracking>());
        }
        for (int i = 0; i < data.size(); i++ ) {
            if (dataTrackings.get(i).isEmpty()) {
                this.dataTrackings.get(i).add(new DataTracking(i + 1, "No Tracking Data",
                        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse("00/00/0000 0:00:00 AM"),
                        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse("00/00/0000 0:00:00 AM"),
                        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse("00/00/0000 0:00:00 AM")
                        , 0, 0, dataTracking.get(i).getLatitude(), dataTracking.get(i).getLongitude()));
            }
        }
    }
    @Override
    public ShowTrackingListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.cardview, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ShowTrackingListAdapter.MyViewHolder holder, final int position) {

        final DataModel dataModel = dataSetFilter.get(position);
        final DataTrackingModel dataTrackingModel = dataTrackingModels.get(position);
        TextView textViewID = holder.trackableID;
        TextView textViewDate = holder.trackingDate;
        TextView textViewStartTime = holder.startTime;
        TextView textViewStopTime = holder.stopTime;
        TextView textViewMeetLoc = holder.meetLocation;
        textViewID.setText(dataModel.getName());
//        textViewDescription.setText(dataModel.getDescription());
//        textViewWebURL.setText(dataModel.getWebURL());
//        textViewCategory.setText(dataModel.getCategory());
//        try {
//            String picImage = "pic" + Integer.parseInt(dataModel.image);
//            id = ctx.getResources().getIdentifier(picImage, "mipmap", ctx.getPackageName());
//        }
//        catch(NumberFormatException e){}
//        final ShowFragment tv=new ShowFragment();
//        textViewName.setOnClickListener(onClickListener(tv,position));
//        textViewDescription.setOnClickListener(onClickListener(tv,position));
//        textViewWebURL.setOnClickListener(onClickListener(tv,position));
//        textViewCategory.setOnClickListener(onClickListener(tv,position));
//        imageView.setOnClickListener(onClickListener(tv,position));
//        imageView.setImageResource(id);
    }

    public View.OnClickListener onClickListener(final ShowFragment showFragment,final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create and show the dialog.
                FragmentManager manager = activity.getFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Fragment prev = manager.findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                showFragment.newInstance(position,dataTrackings.get(position));
                showFragment.show(manager,"dialog");
            }
        };
    }

    @Override
    public int getItemCount() {
        return dataSetFilter.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataSetFilter = dataSet;
                } else {
                    ArrayList<DataModel> filteredList = new ArrayList<>();
                    for (DataModel row : dataSet) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        try{
                            if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                    ||row.getCategory().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        catch (NullPointerException e){}
                    }

                    dataSetFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataSetFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataSetFilter = (ArrayList<DataModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface RecyclerViewAdapterListener {
        void onContactSelected(DataModel dataModel);
    }

    public void addTrackingData(int position,int ID, String title, Date startTime, Date endTime, Date meetTime, double currLat, double currLong,
                                double meetLat, double meetLong)
    {
        this.dataTrackings.get(position).add(0,new DataTracking(ID,title,startTime,endTime,meetTime,currLat,currLong,meetLat,meetLong));
    }


}


