package com.example.macintosh.assignmentt1;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
public class MainActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> dataa = new ArrayList<DataModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Trackable trackable = new Trackable();
        trackable.parseFile(getApplicationContext());
        //recyclerView.setHasFixedSize(true);
        for ( int i = 0 ; i < trackable.trackableList.size();i++){
            dataa.add(new DataModel(
                    trackable.trackableList.get(i).name,
                    trackable.trackableList.get(i).description,
                    trackable.trackableList.get(i).webURL,
                    trackable.trackableList.get(i).Category

            ));
        }
        recyclerView = findViewById(R.id.my_recycler_view);
        adapter = new MyRecyclerViewAdapter(dataa);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


    }
}




// simulated tracking service by Caspar for MAD s2, 2018
// Usage: add this class to project in appropriate package
// add tracking_data.txt to res/raw folder
// see: TestTrackingService.test() method for example

// NOTE: you may need to explicitly add the import for the generated some.package.R class
// which is based on your package declaration in the manifest






