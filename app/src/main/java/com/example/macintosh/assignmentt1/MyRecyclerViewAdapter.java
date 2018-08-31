package com.example.macintosh.assignmentt1;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toolbar;

import java.util.ArrayList;

/**
 * Created by Parsania Hardik on 29-Jun-17.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>
implements Filterable{

    private LayoutInflater inflater;
    private Context ctx;
    private ArrayList<DataModel> dataSet;
    private ArrayList<DataModel> dataSetFilter;
    private Activity activity;
    private RecyclerViewAdapterListener listener;
    int id;
    Dialog dialog;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private TextView description;
        private TextView webURL;
        private TextView category;
        private ImageView imageView;
        private View container;
        private CardView cardView;


        ItemClickListener itemClickListener;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.description = itemView.findViewById(R.id.description);
            this.webURL = itemView.findViewById(R.id.webURL);
            this.category = itemView.findViewById(R.id.category);
            this.imageView = itemView.findViewById(R.id.thumbnail);
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
    public MyRecyclerViewAdapter(ArrayList<DataModel> data,Context ctx, Activity activity) {
        this.dataSet = data;
        this.ctx = ctx;
        this.activity = activity;
        this.dataSetFilter = data;
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
        TextView textViewName = holder.name;
        TextView textViewDescription = holder.description;
        TextView textViewWebURL = holder.webURL;
        TextView textViewCategory = holder.category;
        final ImageView imageView = holder.imageView;
        textViewName.setText(dataModel.getName());
        textViewDescription.setText(dataModel.getDescription());
        textViewWebURL.setText(dataModel.getWebURL());
        textViewCategory.setText(dataModel.getCategory());
        String picImage = "pic" + Integer.parseInt( dataModel.image );
        id = ctx.getResources().getIdentifier(picImage,"mipmap",ctx.getPackageName());
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
                TextView name1=  dialog.findViewById(R.id.dianame);
                TextView desc1 =  dialog.findViewById(R.id.diadesc);
                TextView webURL1 =  dialog.findViewById(R.id.diaweburl);
                TextView category1 =  dialog.findViewById(R.id.diacategory);
                name1.setText(dataSetFilter.get(position).getName());
                desc1.setText(dataSetFilter.get(position).getDescription());
                webURL1.setText(dataSetFilter.get(position).getWebURL());
                category1.setText(dataSetFilter.get(position).getCategory());


                    //setDataToView(name, desc, webURL, category,position);

                dialog.show();
                Toast.makeText( ctx,"Test :"+ String.valueOf( position ), Toast.LENGTH_SHORT ).show();
            }
        };
    }
    public void setDataToView(TextView name, TextView desc, TextView webURL, TextView category, int position) {


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


