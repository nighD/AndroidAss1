package com.example.macintosh.assignmentt1.ViewAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.app.Fragment;
import android.widget.Toast;


import com.example.macintosh.assignmentt1.Activities.MainActivity;
import com.example.macintosh.assignmentt1.JDBC.JDBCActivity;
import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.Interfaces.ItemClickListener;
import com.example.macintosh.assignmentt1.ModelClass.DataModel;
import com.example.macintosh.assignmentt1.ModelClass.DataTrackingModel;
import com.example.macintosh.assignmentt1.R;
import com.example.macintosh.assignmentt1.Activities.ShowFragment;
import com.example.macintosh.assignmentt1.Activities.show_TL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>
implements Filterable{


    private Context ctx;
    private CallbackInterface mCallback;
    private ArrayList<DataModel> dataSet;
    private ArrayList<DataModel> dataSetFilter;
    private ArrayList<DataTrackingModel> dataTrackingModels;
    private static ArrayList<ArrayList<DataTracking>> dataTrackings = new ArrayList<>();
    private static ArrayList<ArrayList<DataTrackingModel>> dataTrackings2 = new ArrayList<>();
    private String databasePath;
    public DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    private Activity activity;
    private RecyclerViewAdapterListener listener;
    JDBCActivity jdbcActivity = new JDBCActivity();
    int id;

    public interface CallbackInterface{

        void onHandleSelection(int position, ArrayList<ArrayList<DataTracking>> updatedArrayList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private TextView description;
        private TextView webURL;
        private TextView category;
        private ImageView imageView;
        private View container;
        private CardView cardView;
        private ImageButton popMenu;

        private ImageButton removeButton;



        ItemClickListener itemClickListener;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.description = itemView.findViewById(R.id.description);
            this.webURL = itemView.findViewById(R.id.webURL);
            this.category = itemView.findViewById(R.id.category);
            this.imageView = itemView.findViewById(R.id.thumbnail);
            this.popMenu = itemView.findViewById(R.id.ic_pop_menu);

            container = itemView.findViewById(R.id.card_view);
            popMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(ctx, popMenu);
                    popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getItemId()==R.id.show_tracking_list){
                                Intent showTL = new Intent().setClass(ctx,show_TL.class);
                                showTL.putExtra("CellPosition", getAdapterPosition());
                                showTL.putExtra("dataModels",dataSetFilter);
                                showTL.putExtra("dataTrackingModels",dataTrackings2);
                                ctx.startActivity(showTL);
                            }
                            else{
                                Intent showFrag = new Intent().setClass(MyRecyclerViewAdapter.this.activity,ShowFragment.class);
                                showFrag.putExtra("CellPosition",getAdapterPosition());
                                showFrag.putExtra("dataTrackings",dataTrackings);
                                showFrag.putExtra("dataModels",dataSetFilter );
                                showFrag.putExtra("dataTrackingM",dataTrackingModels);
                                showFrag.putExtra("dataTrackingModels",dataTrackings2);
                                MyRecyclerViewAdapter.this.activity.startActivityForResult(showFrag,1);
//                                if(mCallback != null){
//                                    mCallback.onHandleSelection(getAdapterPosition(), dataTrackings);
//                                }
                            }
                            Toast.makeText(ctx, "Clicked", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {

            this.itemClickListener.onItemClick(v,getLayoutPosition() );

        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if( requestCode == 1888 ) {
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                ((ImageView)inflatedView.findViewById(R.id.image)).setImageBitmap(photo);
            }
        }

    }

    public void updateAdapter(/* gets params like TITLES,ICONS,NAME,EMAIL,PROFILE*/){

        // update adapter element like NAME, EMAIL e.t.c. here

        // then in order to refresh the views notify the RecyclerView
        notifyDataSetChanged();
    }


    public MyRecyclerViewAdapter(ArrayList<DataModel> data, ArrayList<DataTrackingModel> dataTracking,ArrayList<ArrayList<DataTrackingModel>> dataTrackings2, ArrayList<ArrayList<DataTracking>> dataTrackings,Context ctx, Activity activity, String databasePath) throws ParseException {
        this.dataSet = data;
        this.ctx = ctx;
        this.activity = activity;
        this.dataSetFilter = data;
        this.dataTrackingModels = dataTracking;
        this.databasePath = databasePath;
        this.dataTrackings = dataTrackings;
        this.dataTrackings2 = dataTrackings2;
        try{
            mCallback = (CallbackInterface) ctx;
        }catch(ClassCastException ex){
            //.. should log the error or throw and exception
            Log.e("MyAdapter","Must implement the CallbackInterface in the Activity", ex);
        }
    }
    @Override
    public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.cardview, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyRecyclerViewAdapter.MyViewHolder holder,final int position) {

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

//        if(mCallback != null){
//            mCallback.onHandleSelection(position, dataTrackings);
//        }


        try {
            String picImage = "pic" + Integer.parseInt(dataModel.getImage());
            id = ctx.getResources().getIdentifier(picImage, "mipmap", ctx.getPackageName());
        }
        catch(NumberFormatException e){}
        imageView.setImageResource(id);



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


