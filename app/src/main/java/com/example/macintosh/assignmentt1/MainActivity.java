package com.example.macintosh.assignmentt1;
import android.app.Activity;
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
<<<<<<< HEAD
import android.app.Dialog;
import android.widget.AdapterView;
import java.lang.reflect.Field;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
=======
>>>>>>> 2bab2eb4900f98b7db6b47f4b3ccd1e2ba92f5bf
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    private Activity activity;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private FloatingActionButton fab;
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
<<<<<<< HEAD
        fab = (FloatingActionButton) findViewById(R.id.fab);
        adapter = new MyRecyclerViewAdapter(dataa,getApplicationContext(),this);
=======
        adapter = new MyRecyclerViewAdapter(dataa, getApplicationContext());
>>>>>>> 2bab2eb4900f98b7db6b47f4b3ccd1e2ba92f5bf
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
<<<<<<< HEAD
        fab.setOnClickListener(onAddingListener());
        //String pic0 = "pic2";
        //int id = getApplicationContext().getResources().getIdentifier(pic0,"drawable",getPackageName());
        //TextView textView = findViewById(R.id.name);
        //TextView textView1 = findViewById(R.id.description);
        //textView.setText(id);
        //ImageView imageView = findViewById(R.id.thumbnail);
//        textView1.setText("android:resource//"+getPackageName()+"/");
        //imageView.setImageResource(R.drawable.pic2);


    }

    private View.OnClickListener onAddingListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_add); //layout for dialog
                dialog.setTitle("Add a new friend");
                dialog.setCancelable(false); //none-dismiss when touching outside Dialog

                // set the custom dialog components - texts and image
                EditText name = (EditText) dialog.findViewById(R.id.name);
                EditText job = (EditText) dialog.findViewById(R.id.job);
                Spinner spnGender = (Spinner) dialog.findViewById(R.id.gender);
                View btnAdd = dialog.findViewById(R.id.btn_ok);
                View btnCancel = dialog.findViewById(R.id.btn_cancel);

                //set spinner adapter
                ArrayList<String> gendersList = new ArrayList<>();
                gendersList.add("Male");
                gendersList.add("Female");
                ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_dropdown_item_1line, gendersList);
                spnGender.setAdapter(spnAdapter);

                //set handling event for 2 buttons and spinner
                spnGender.setOnItemSelectedListener(onItemSelectedListener());
//                btnAdd.setOnClickListener(onConfirmListener(name, job, dialog));
//                btnCancel.setOnClickListener(onCancelListener(dialog));

                dialog.show();
            }
        };
    }
    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
//                if (position == 0) {
//                    gender = true;
//                } else {
//                    gender = false;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        };
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
=======
    }

>>>>>>> 2bab2eb4900f98b7db6b47f4b3ccd1e2ba92f5bf
}







