package com.example.macintosh.assignmentt1;
import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.TextViewCompat;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toolbar;
import android.app.Fragment;
import org.w3c.dom.Text;

import android.os.Bundle;
import android.app.DialogFragment;


import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>
implements Filterable{


    private Context ctx;
    private ArrayList<DataModel> dataSet;
    private ArrayList<DataModel> dataSetFilter;
    private ArrayList<DataTrackingModel> dataTrackingModels;
//    private ArrayList<DataTrackingModel> dataTrackingSet;
    public DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    private Activity activity;
    private RecyclerViewAdapterListener listener;
    int id;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private TextView description;
        private TextView webURL;
        private TextView category;
        private ImageView imageView;
        private View container;
        private CardView cardView;

        private ImageButton removeButton;



        ItemClickListener itemClickListener;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.description = itemView.findViewById(R.id.description);
            this.webURL = itemView.findViewById(R.id.webURL);
            this.category = itemView.findViewById(R.id.category);
            this.imageView = itemView.findViewById(R.id.thumbnail);
            container = itemView.findViewById(R.id.card_view);
            //removeButton = (ImageButton) itemView.findViewById(R.id.ib_remove);
            container = itemView.findViewById(R.id.card_view);
        }
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {

            this.itemClickListener.onItemClick(v,getLayoutPosition() );

        }

    }

    public MyRecyclerViewAdapter(ArrayList<DataModel> data, ArrayList<DataTrackingModel> dataTracking,Context ctx, Activity activity) {
        this.dataSet = data;
        this.ctx = ctx;
        this.activity = activity;
        this.dataSetFilter = data;
//        this.dataTrackingSet = dataTracking;
        this.dataTrackingModels = dataTracking;
    }
    @Override
    public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.cardview, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyRecyclerViewAdapter.MyViewHolder holder, final int position) {

        final DataModel dataModel = dataSetFilter.get(position);
        final DataTrackingModel dataTrackingModel = dataTrackingModels.get(position);
        TextView textViewName = holder.name;
        TextView textViewDescription = holder.description;
        TextView textViewWebURL = holder.webURL;
        TextView textViewCategory = holder.category;
        final ImageView imageView = holder.imageView;
        textViewName.setText(dataModel.getName());
        textViewDescription.setText(dataModel.getDescription());
        textViewWebURL.setText(dataModel.getWebURL());
        textViewCategory.setText(dataModel.getCategory());



        try {
            String picImage = "pic" + Integer.parseInt(dataModel.image);
            id = ctx.getResources().getIdentifier(picImage, "mipmap", ctx.getPackageName());
        }
        catch(NumberFormatException e){}
        final  ShowFragment tv=new ShowFragment();
        textViewName.setOnClickListener(onClickListener(tv,position));
        textViewDescription.setOnClickListener(onClickListener(tv,position));
        textViewWebURL.setOnClickListener(onClickListener(tv,position));
        textViewCategory.setOnClickListener(onClickListener(tv,position));
        imageView.setOnClickListener(onClickListener(tv,position));
        imageView.setImageResource(id);


        imageView.setImageResource(id);



    }
    private void setDataToView(TextView trackingDate, TextView trackableID, TextView stopTime, TextView latitude, TextView longitude, int position) {

        trackingDate.setText(dataTrackingModels.get(position).getDate().toString());

        trackableID.setText(Integer.toString(dataTrackingModels.get(position).getTrackableId()));
        stopTime.setText(Integer.toString(dataTrackingModels.get(position).getStopTime()));

        trackableID.setText(String.valueOf(dataTrackingModels.get(position).getTrackableId()));
        stopTime.setText(String.valueOf(dataTrackingModels.get(position).getStopTime()));

        latitude.setText(Double.toString(dataTrackingModels.get(position).getLatitude()));
        longitude.setText(Double.toString(dataTrackingModels.get(position).getLongitude()));
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
                showFragment.newInstance(position);

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




}


