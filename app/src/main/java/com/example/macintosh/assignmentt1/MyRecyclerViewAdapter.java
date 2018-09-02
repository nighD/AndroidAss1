package com.example.macintosh.assignmentt1;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.TextViewCompat;
import android.content.Intent;
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

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Parsania Hardik on 29-Jun-17.
 */
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
    Dialog dialog;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private TextView description;
        private TextView webURL;
        private TextView category;
        private TextView trackingDate;
        private TextView trackableID;
        private TextView stopTime;
        private TextView latitude;
        private TextView longitude;
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
            this.trackingDate = itemView.findViewById(R.id.trackingDate);
            this.trackableID = itemView.findViewById(R.id.trackableID);
            this.stopTime = itemView.findViewById(R.id.StopTime);
            this.latitude = itemView.findViewById(R.id.Latitude);
            this.longitude = itemView.findViewById(R.id.Longitude);
            removeButton = (ImageButton) itemView.findViewById(R.id.ib_remove);
            container = itemView.findViewById(R.id.card_view);

            //this.cardView = itemView.findViewById( R.id.card_view );
//            cardView.setOnClickListener( this );
//            imageView.setOnClickListener( this );
//            name.setOnClickListener( this );
//            description.setOnClickListener( this );
//            webURL.setOnClickListener( this );
//            category.setOnClickListener( this );


        }
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
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

        holder.container.setOnClickListener(onClickListener(  holder.getAdapterPosition()));
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
        TextView textViewDate = holder.trackingDate;
        TextView textViewID = holder.trackableID;
        TextView textViewStopTime = holder.stopTime;
        TextView textViewLatitude = holder.latitude;
        TextView textViewLongitude = holder.longitude;
        final ImageView imageView = holder.imageView;
        textViewName.setText(dataModel.getName());
        textViewDescription.setText(dataModel.getDescription());
        textViewWebURL.setText(dataModel.getWebURL());
        textViewCategory.setText(dataModel.getCategory());
        try{
            textViewDate.setText(dateFormat.format(dataTrackingModel.getDate()));
            textViewID.setText(String.valueOf(dataTrackingModel.getTrackableId()));
            textViewStopTime.setText(String.valueOf(dataTrackingModel.getStopTime()));
            textViewLatitude.setText(Double.toString(dataTrackingModel.getLatitude()));
            textViewLongitude.setText(Double.toString(dataTrackingModel.getLongitude()));
        }
        catch (NullPointerException e){}
        try {
            String picImage = "pic" + Integer.parseInt(dataModel.image);
            id = ctx.getResources().getIdentifier(picImage, "mipmap", ctx.getPackageName());
        }
        catch(NumberFormatException e){}
        textViewName.setOnClickListener(onClickListener(position));
        textViewDescription.setOnClickListener(onClickListener(position));
        textViewWebURL.setOnClickListener(onClickListener(position));
        textViewCategory.setOnClickListener(onClickListener(position));
        imageView.setOnClickListener(onClickListener(position));
        imageView.setImageResource(id);
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog);
        holder.setItemClickListener( new ItemClickListener() {
            @Override
            public void onItemClick(View v,int pos) {
                Intent i=new Intent(ctx,Detailactivity.class);
                i.putExtra("Name",dataModel.getName());
                i.putExtra("Position",position);
                ctx.startActivity(i);
            }
        } );

        holder.container.setOnClickListener(onClickListener(position));
        imageView.setImageResource(id);
        //imageView.setImageResource(R.drawable.pic1);
//        holder.setItemClickListener( new ItemClickListener() {
//            @Override
//            public void onItemClick(View v,int pos) {
//                Intent i=new Intent(ctx,Detailactivity.class);
//                i.putExtra("Name",dataSet.get( position ).getName());
//                i.putExtra("Position",position);
//                //i.putExtra("Image",id);
//                //START DETAIL ACTIVITY
//                ctx.startActivity(i);
//            }
//        } );
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemLabel = dataTrackingModels.get(position).toString();
                dataTrackingModels.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,dataTrackingModels.size());
                Toast.makeText(ctx,"Removed : " + itemLabel,Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setDataToView(TextView trackingDate, TextView trackableID, TextView stopTime, TextView latitude, TextView longitude, int position) {
        trackingDate.setText(dataTrackingModels.get(position).getDate().toString());
        trackableID.setText(String.valueOf(dataTrackingModels.get(position).getTrackableId()));
        stopTime.setText(String.valueOf(dataTrackingModels.get(position).getStopTime()));
        latitude.setText(Double.toString(dataTrackingModels.get(position).getLatitude()));
        longitude.setText(Double.toString(dataTrackingModels.get(position).getLongitude()));
    }

    public View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                final Dialog dialog = new Dialog( activity );
//
//                dialog.setContentView(R.layout.activity_main);
//                dialog.setTitle("Position " + position);
//                dialog.setCancelable(true); // dismiss when touching outside Dialog
//
//                // set the custom dialog components - texts and image




                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("Position " + position);
                dialog.setCancelable(true); // dismiss when touching outside Dialog

                // set the custom dialog components - texts and image
                TextView trackingDate = (TextView) dialog.findViewById(R.id.trackingDate);
                TextView trackableID = (TextView) dialog.findViewById(R.id.trackableID);
                TextView stopTime = (TextView) dialog.findViewById(R.id.StopTime);
                TextView latitude = (TextView) dialog.findViewById(R.id.Latitude);
                TextView longitude = (TextView) dialog.findViewById(R.id.Longitude);

//                if(activity!=null)
//                {
                    setDataToView(trackingDate, trackableID, stopTime, latitude, longitude,position);


                    //setDataToView(name, desc, webURL, category,position);


//                }

                dialog.show();
                Toast.makeText( ctx,"Test :"+ String.valueOf( position ), Toast.LENGTH_SHORT ).show();
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
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                ||row.getCategory().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
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


