package com.example.macintosh.assignmentt1;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macintosh.assignmentt1.MainActivity;
import com.example.macintosh.assignmentt1.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Parsania Hardik on 29-Jun-17.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context ctx;
    private ArrayList<DataModel> dataSet;
    int id;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView description;
        private TextView webURL;
        private TextView category;
        private ImageView imageView;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.description = itemView.findViewById(R.id.description);
            this.webURL = itemView.findViewById(R.id.webURL);
            this.category = itemView.findViewById(R.id.category);
            this.imageView = itemView.findViewById(R.id.thumbnail);


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

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}


