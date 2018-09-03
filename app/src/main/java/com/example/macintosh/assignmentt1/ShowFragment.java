package com.example.macintosh.assignmentt1;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

import android.widget.Button;
import java.time.LocalDateTime;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class ShowFragment extends DialogFragment  {
    int finalPosition;
    private Button addButton;
    private Dialog dialog;
    private Trackable trackable;
    private TrackingService trackingService;
    RecyclerView rv;
    RecyclerViewDialogAdapter adapter;
    private ArrayList<DataTrackingModel> trackingData;
    private ArrayList<DataModel> dataa;
    private static ArrayList<DataTracking> dataTrackings = new ArrayList<>();
    public ShowFragment newInstance(final int num, ArrayList<DataTracking> dataTrackings) {
       ShowFragment f = new ShowFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        finalPosition = num;
        System.out.println("Num is: "+num);
        this.dataTrackings = dataTrackings;
        return f;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //String strtext=getArguments().getString("ID");

        Bundle aa = this.getArguments();
        if (aa != null) {


        finalPosition = aa.getInt("num");}
        System.out.println("finalPosition is: "+finalPosition);
        final View rootView=inflater.inflate(R.layout.fraglayout,container);

        //RECYCER
        dataa = new ArrayList<>( );
        trackingData = new ArrayList<>();
        trackable = new Trackable();
        trackable.parseFile( this.getContext() );
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
        trackingService.parseFile(this.getContext());
        for (int i = 0; i < trackingService.trackingList.size(); i++){
            trackingData.add(new DataTrackingModel(trackingService.trackingList.get(i).date,
                    trackingService.trackingList.get(i).trackableId,
                    trackingService.trackingList.get(i).stopTime,
                    trackingService.trackingList.get(i).latitude,
                    trackingService.trackingList.get(i).longitude));
        }
//     dataTrackings = new ArrayList<>(  );

        addButton = (Button) rootView.findViewById(R.id.btn_add);
//        addTrackingData(1, "lala", new Date(), new Date(),new Date(), 0.0,0.1,0.2,0.3);
        rv= (RecyclerView) rootView.findViewById(R.id.mRecyerID);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        getDialog().setTitle("Tracking data: "+ "\r" + trackable.trackableList.get(finalPosition).name);
        //ADAPTER
        try {
            adapter=new RecyclerViewDialogAdapter(this.getActivity(),trackingData,dataa,finalPosition,this.dataTrackings);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rv.setAdapter(adapter);
         addButton.setOnClickListener(onClickListener(finalPosition));
        getDialog().setCancelable(true);
        return rootView;
    }
   public View.OnClickListener onClickListener(final int position) {
        final Context c = this.getContext();
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(c);
                dialog.setTitle( "Add" );
                dialog.setContentView(R.layout.dialog_template);
                final EditText Write = dialog.findViewById(R.id.writeTitle);
                Button SaveMyName = dialog.findViewById(R.id.SaveNow);
                final DatePicker datePicker =  dialog.findViewById(R.id.date_picker);
                 final TimePicker startTimePicker =  dialog.findViewById(R.id.start_time_picker);
                 final TimePicker endTimePicker = dialog.findViewById(R.id.end_time_picker);
                final EditText WriteMeetLong = dialog.findViewById(R.id.writeMeetLong);
                Write.setEnabled(true);
                SaveMyName.setEnabled(true);

                SaveMyName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        dataTrackings.get( 0 ).title = Write.getText().toString();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set( datePicker.getMonth(),datePicker.getDayOfMonth(),endTimePicker.getHour() - startTimePicker.getHour(),endTimePicker.getMinute()-startTimePicker.getMinute(),0 );
                        Date date = calendar.getTime();
//                        dataTrackings.get( 0 ).meettime = date;
//                        addTrackingData(position, Write.getText().toString(), date, date,date, 0.0,0.1,0.2,0.3);
                        adapter.addTrackingData(position,position, Write.getText().toString(), date, date,date, 0.0,0.1,0.2,0.3);
                        adapter.notifyItemRangeChanged(position,dataTrackings.size());
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemInserted(position);
                        rv.setLayoutManager(new LinearLayoutManager(getContext()));
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        };
    }
    public void addTrackingData(int ID, String title, Date startTime, Date endTime, Date meetTime, double currLat, double currLong,
                                double meetLat, double meetLong)
    {
        this.dataTrackings.add(0,new DataTracking(ID,title,startTime,endTime,meetTime,currLat,currLong,meetLat,meetLong));
    }
}