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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;

import java.util.ArrayList;

/**
 * Created by Parsania Hardik on 29-Jun-17.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private Context ctx;
    private ArrayList<DataModel> dataSet;
    private Activity activity;
    int id;
    public MyRecyclerViewAdapter(ArrayList<DataModel> data,Context ctx, Activity activity) {
        this.dataSet = data;
        this.ctx = ctx;
        this.activity = activity;
    }
    @Override
    public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.cardview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(final MyRecyclerViewAdapter.MyViewHolder holder, final int position) {
        TextView textViewName = holder.name;
        TextView textViewDescription = holder.description;
        TextView textViewWebURL = holder.webURL;
        TextView textViewCategory = holder.category;
        final ImageView imageView = holder.imageView;
        textViewName.setText(dataSet.get(position).getName());
        textViewDescription.setText(dataSet.get(position).getDescription());
        textViewWebURL.setText(dataSet.get(position).getWebURL());
        textViewCategory.setText(dataSet.get(position).getCategory());
        String picImage = "pic" + Integer.toString(position+1);
        id = ctx.getResources().getIdentifier(picImage,"mipmap",ctx.getPackageName());
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

    }
    private void setDataToView(TextView name, TextView desc, TextView webURL, TextView category, int position) {
        name.setText(dataSet.get(position).getName());
        desc.setText(dataSet.get(position).getDescription());
        webURL.setText(dataSet.get(position).getWebURL());
        category.setText(dataSet.get(position).getCategory());
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView description;
        private TextView webURL;
        private TextView category;
        private ImageView imageView;
        private View container;
        private CardView cardView;

//        ItemClickListener itemClickListener;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.description = itemView.findViewById(R.id.description);
            this.webURL = itemView.findViewById(R.id.webURL);
            this.category = itemView.findViewById(R.id.category);
            this.imageView = itemView.findViewById(R.id.thumbnail);
            container = itemView.findViewById(R.id.card_view);
//            this.cardView = itemView.findViewById( R.id.card_view );

//            cardView.setOnClickListener( this );
//            imageView.setOnClickListener( this );
//            name.setOnClickListener( this );
//            description.setOnClickListener( this );
//            webURL.setOnClickListener( this );
//            category.setOnClickListener( this );


        }
//        public void setItemClickListener(ItemClickListener itemClickListener)
//        {
//            this.itemClickListener=itemClickListener;
//        }

//        @Override
//        public void onClick(View v) {
//            this.itemClickListener.onItemClick(v,getLayoutPosition());
//        }

    }
    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.cardview);
                dialog.setTitle("Position " + position);
                dialog.setCancelable(true); // dismiss when touching outside Dialog

                // set the custom dialog components - texts and image
                TextView name = (TextView) dialog.findViewById(R.id.name);
                TextView desc = (TextView) dialog.findViewById(R.id.description);
                TextView webURL = (TextView) dialog.findViewById(R.id.webURL);
                TextView category = (TextView) dialog.findViewById(R.id.category);
                ImageView icon = (ImageView) dialog.findViewById(R.id.image);

//                if(activity!=null)
//                {
                    setDataToView(name, desc, webURL, category,position);


//                }
                dialog.show();
            }
        };
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}


