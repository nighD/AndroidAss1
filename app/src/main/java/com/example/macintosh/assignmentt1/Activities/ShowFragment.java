package com.example.macintosh.assignmentt1.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.widget.Button;
import android.widget.Toast;

import com.example.macintosh.assignmentt1.ModelClass.DataModel;
import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.ModelClass.DataTrackingModel;
import com.example.macintosh.assignmentt1.R;
import com.example.macintosh.assignmentt1.ModelClass.Trackable;
import com.example.macintosh.assignmentt1.ModelClass.TrackingService;
import com.example.macintosh.assignmentt1.ViewAdapter.RecyclerViewDialogAdapter;

import java.text.ParseException;
import java.util.ArrayList;

public class ShowFragment extends AppCompatActivity {
    private Trackable trackable;
    private TrackingService trackingService;
    private Button addButton;
    public AlertDialog.Builder mBuilder;
    public AlertDialog dialog;
    RecyclerView rv;
    public Intent mIntent;
    Context ctx;
    RecyclerViewDialogAdapter adapter;
    private ArrayList<DataModel> dataa;
    private ArrayList<DataTrackingModel> trackingData;
    private static ArrayList<ArrayList<DataTracking>> dataTrackings;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        setContentView(R.layout.fraglayout);
        mIntent = getIntent();
        dataa = new ArrayList<>();
        dataTrackings = new ArrayList<>();
        trackingData = new ArrayList<>();
        trackable = new Trackable();
        trackable.parseFile( this.getApplicationContext() );
        for (int i = 0; i < trackable.trackableList.size(); i++) {
            dataa.add( new DataModel(
                    trackable.trackableList.get( i ).name,
                    trackable.trackableList.get( i ).description,
                    trackable.trackableList.get( i ).webURL,
                    trackable.trackableList.get( i ).Category,
                    trackable.trackableList.get( i ).getID()

            ) );
        }
        trackingService = new TrackingService();
        trackingService.parseFile(this.getApplicationContext());
        for (int i = 0; i < trackingService.trackingList.size(); i++){
            trackingData.add(new DataTrackingModel(trackingService.trackingList.get(i).date,
                    trackingService.trackingList.get(i).date.getTime(),
                    trackingService.trackingList.get(i).trackableId,
                    trackingService.trackingList.get(i).stopTime,
                    trackingService.trackingList.get(i).latitude,
                    trackingService.trackingList.get(i).longitude));
        }
        for(int i = 0; i < dataa.size(); i++)
        {
            dataTrackings.add(new ArrayList<DataTracking>());
        }
        try {
            for (int i = 0; i < dataa.size(); i++) {
                if (dataTrackings.get(i).isEmpty()) {
                    this.dataTrackings.get(i).add(new DataTracking(i + 1, "No Tracking Data",
                            DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse("00/00/0000 0:00:00 AM"),
                            DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse("00/00/0000 0:00:00 AM"),
                            DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse("00/00/0000 0:00:00 AM")
                            , 0, 0, 0, 0));
                }
            }
        }
        catch (ParseException e){}
        addButton = findViewById(R.id.btn_add);
        rv=findViewById(R.id.mRecyerID);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //ADAPTER
        try {
            adapter=new RecyclerViewDialogAdapter(this,trackingData,dataa,mIntent.getIntExtra("CellPosition",0),this.dataTrackings.get(mIntent.getIntExtra("CellPosition",0)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rv.setAdapter(adapter);

        addButton.setOnClickListener(onAddClickListener(mIntent.getIntExtra("CellPosition",0)));
    }
   public View.OnClickListener onAddClickListener(final int position) {
        final Context c = this.getApplicationContext();
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!isFinishing()){
                            mBuilder = new AlertDialog.Builder(ShowFragment.this);
                            final View mView = getLayoutInflater().inflate(R.layout.dialog_template,null);
                            final EditText Write = mView.findViewById(R.id.writeTitle);
                            Button SaveMyName = mView.findViewById(R.id.SaveNow);
                            final DatePicker datePicker =  mView.findViewById(R.id.date_picker);
//                final TimePicker startTimePicker =  dialog.findViewById(R.id.start_time_picker);
                            final TimePicker meetTimePicker = mView.findViewById(R.id.meet_time_picker);
//                final EditText WriteCurrLoc = dialog.findViewById(R.id.writeCurrLoc);
//                final EditText WriteMeetLoc = dialog.findViewById(R.id.writeMeetLoc);
                            Write.setEnabled(true);
                            SaveMyName.setEnabled(true);
                            mBuilder.setView(mView);
                            dialog = mBuilder.create();

                            SaveMyName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Calendar calendar = Calendar.getInstance();
//                        calendar.set( datePicker.getMonth(),datePicker.getDayOfMonth(),startTimePicker.getHour(),startTimePicker.getMinute(),0 );
//                        Date startTime = calendar.getTime();
//                        calendar.set( datePicker.getMonth(),datePicker.getDayOfMonth(),endTimePicker.getHour(),endTimePicker.getMinute(),0 );
//                        Date endTime = calendar.getTime();
                                    calendar.set( datePicker.getMonth(),datePicker.getDayOfMonth(),meetTimePicker.getHour(),meetTimePicker.getMinute(),0 );
                                    Date meetTime = calendar.getTime();
                                    try {
//                            adapter.addTrackingData(position, position, Write.getText().toString(), startTime, endTime, meetTime, Double.parseDouble(WriteCurrLoc.getText().toString()),
//                                    Double.parseDouble(WriteCurrLoc.getText().toString()), Double.parseDouble(WriteMeetLoc.getText().toString()), Double.parseDouble(WriteMeetLoc.getText().toString()));
                                        adapter.addTrackingData(position,mIntent.getIntExtra("CellPosition",0), Write.getText().toString(), new Date(), new Date(),meetTime, 0,
                                                0,trackingData.get(mIntent.getIntExtra("CellPosition",0)).getLatitude(),trackingData.get(mIntent.getIntExtra("CellPosition",0)).getLongitude());
                                        adapter.notifyItemRangeChanged(position, dataTrackings.size());
                                        adapter.notifyDataSetChanged();
                                        adapter.notifyItemInserted(position);
                                        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    }
                                    catch (NumberFormatException e){}
                                    dialog.cancel();
                                }
                            });
                            dialog.show();
                        }
                    }
                });
            }
        };
    }

}