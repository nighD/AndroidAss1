package com.example.macintosh.assignmentt1;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Parsania Hardik on 29-Jun-17.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context ctx;
    private ArrayList<DataModel> dataSet;
    int id;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private TextView description;
        private TextView webURL;
        private TextView category;
        private ImageView imageView;
        private CardView cardView;

        ItemClickListener itemClickListener;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.description = itemView.findViewById(R.id.description);
            this.webURL = itemView.findViewById(R.id.webURL);
            this.category = itemView.findViewById(R.id.category);
            this.imageView = itemView.findViewById(R.id.thumbnail);
            this.cardView = itemView.findViewById( R.id.card_view );

            cardView.setOnClickListener( this );
            imageView.setOnClickListener( this );
            name.setOnClickListener( this );
            description.setOnClickListener( this );
            webURL.setOnClickListener( this );
            category.setOnClickListener( this );


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
    public MyRecyclerViewAdapter(ArrayList<DataModel> data,Context ctx) {
        this.dataSet = data;
        this.ctx = ctx;
    }

    @Override
    public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyRecyclerViewAdapter.MyViewHolder holder, final int position) {
        TextView textViewName = holder.name;
        TextView textViewDescription = holder.description;
        TextView textViewWebURL = holder.webURL;
        TextView textViewCategory = holder.category;
        ImageView imageView = holder.imageView;
        textViewName.setText(dataSet.get(position).getName());
        textViewDescription.setText(dataSet.get(position).getDescription());
        textViewWebURL.setText(dataSet.get(position).getWebURL());
        textViewCategory.setText(dataSet.get(position).getCategory());
        String picImage = "pic" + Integer.toString(position+1);
        id = ctx.getResources().getIdentifier(picImage,"mipmap",ctx.getPackageName());
        imageView.setImageResource(id);
        //imageView.setImageResource(R.drawable.pic1);
        holder.setItemClickListener( new ItemClickListener() {
            @Override
            public void onItemClick(View v,int pos) {
                Intent i=new Intent(ctx,Detailactivity.class);
                i.putExtra("Name",dataSet.get( position ).getName());
                i.putExtra("Position",position);
                //i.putExtra("Image",id);
                //START DETAIL ACTIVITY
                ctx.startActivity(i);
            }
        } );

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}


