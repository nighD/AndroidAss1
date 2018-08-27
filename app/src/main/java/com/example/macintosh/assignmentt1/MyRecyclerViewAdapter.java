package com.example.macintosh.assignmentt1;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macintosh.assignmentt1.MainActivity;
import com.example.macintosh.assignmentt1.R;

import java.util.ArrayList;

/**
 * Created by Parsania Hardik on 29-Jun-17.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context ctx;
    private ArrayList<DataModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView description;
        private TextView webURL;
        private TextView category;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.description = itemView.findViewById(R.id.description);
            this.webURL = itemView.findViewById(R.id.webURL);
            this.category = itemView.findViewById(R.id.category);


        }


    }
    public MyRecyclerViewAdapter(ArrayList<DataModel> data) {
        this.dataSet = data;
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
        textViewName.setText(dataSet.get(position).getName());
        textViewDescription.setText(dataSet.get(position).getDescription());
        textViewWebURL.setText(dataSet.get(position).getWebURL());
        textViewCategory.setText(dataSet.get(position).getCategory());


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}


