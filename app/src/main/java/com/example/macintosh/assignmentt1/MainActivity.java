package com.example.macintosh.assignmentt1;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> dataa = new ArrayList<DataModel>();
    static View.OnClickListener myOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Trackable trackable = new Trackable();
        trackable.parseFile(getApplicationContext());
        //recyclerView.setHasFixedSize(true);

        for (int i = 0; i < trackable.trackableList.size(); i++) {
            dataa.add(new DataModel(
                    trackable.trackableList.get(i).name,
                    trackable.trackableList.get(i).description,
                    trackable.trackableList.get(i).webURL,
                    trackable.trackableList.get(i).Category

            ));
        }
        recyclerView = findViewById(R.id.my_recycler_view);
        adapter = new MyRecyclerViewAdapter(dataa, getApplicationContext());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

}







