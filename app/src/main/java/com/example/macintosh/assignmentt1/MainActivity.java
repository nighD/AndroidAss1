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

import java.lang.reflect.Field;
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
        adapter = new MyRecyclerViewAdapter(dataa,getApplicationContext());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //String pic0 = "pic2";
        //int id = getApplicationContext().getResources().getIdentifier(pic0,"drawable",getPackageName());
        //TextView textView = findViewById(R.id.name);
        //TextView textView1 = findViewById(R.id.description);
        //textView.setText(id);
        //ImageView imageView = findViewById(R.id.thumbnail);
//        textView1.setText("android:resource//"+getPackageName()+"/");
        //imageView.setImageResource(R.drawable.pic2);


    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static Drawable getAndroidDrawable(String pDrawableName){
        int resourceId=Resources.getSystem().getIdentifier(pDrawableName, "drawable", "android");
        if(resourceId==0){
            return null;
        } else {
            return Resources.getSystem().getDrawable(resourceId);
        }
    }
}




// simulated tracking service by Caspar for MAD s2, 2018
// Usage: add this class to project in appropriate package
// add tracking_data.txt to res/raw folder
// see: TestTrackingService.test() method for example

// NOTE: you may need to explicitly add the import for the generated some.package.R class
// which is based on your package declaration in the manifest






