package com.example.macintosh.assignmentt1.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.macintosh.assignmentt1.JDBC.JDBCActivity;
import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.ModelClass.DataTrackingModel;
import com.example.macintosh.assignmentt1.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddTrackingServiceActivity extends AppCompatActivity {
    private ImageButton browseStartTime;
    private ImageButton browseMeetTime;
    private EditText editTitle;
    private TextView startTime;
    private TextView endTime;
    private TextView meetTime;
    private TextView currLoc;
    private Button Save;
    private Intent mIntent;
    Context ctx;
    String TIME_FORMAT = "hh:mm";
    SimpleDateFormat stf = new SimpleDateFormat(TIME_FORMAT);
    SimpleDateFormat sdf = new SimpleDateFormat( "mm" );
    SimpleDateFormat sdf1 = new SimpleDateFormat( "hh" );
    private String LOG_TAG = this.getClass().getName();
//    private ArrayList<DataTrackingModel> trackingData;
//    private ArrayList<ArrayList<DataTrackingModel>> dataTrackingModels;
//    private ArrayList<ArrayList<DataTracking>> dataTrackings;
    private DataTracking dataTracking1;
    private DataTrackingModel dataTrackingModel1;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.mIntent = getIntent();
        final String db = "jdbc:sqldroid:" + getDatabasePath("assignment1.db").getAbsolutePath();
        JDBCActivity jdbcActivity = new JDBCActivity();
        jdbcActivity.trackingDataDatabase(getApplicationContext(),db);
        //jdbcActivity.takeLatLng( db );
        jdbcActivity.createServiceDatabase(db);
//        this.dataTrackingModels = new ArrayList<>();
//        this.dataTrackings = new ArrayList<>();
//        this.trackingData = new ArrayList<>();
        setContentView(R.layout.add_tracking_service_acti);
        this.browseMeetTime = findViewById(R.id.browseMeetTime2);
        this.editTitle = findViewById(R.id.writeTitle2);
        this.startTime = findViewById(R.id.startTimeTS2);
        this.endTime = findViewById(R.id.endTimeTS2);
        this.meetTime = findViewById(R.id.meetTimeTS2);
        this.currLoc = findViewById(R.id.currLocTS2);
        this.currLoc.setText("No Data");
        this.Save = findViewById(R.id.SaveNow2);
        this.dataTracking1 = (DataTracking) mIntent.getSerializableExtra("dataTracking1");
        int startMinute = Integer.parseInt( sdf.format( dataTracking1.getStartTime()));
        int startHours = Integer.parseInt( sdf1.format( dataTracking1.getStartTime()));
        int endTIME = Integer.parseInt(sdf.format( dataTracking1.getEndTime() ));
        int endTIMEMinute = startMinute +endTIME;
        String convert = String.format("%02d:%02d", startHours, endTIMEMinute );
        //this.dataTrackingModel1 = (DataTrackingModel) mIntent.getSerializableExtra("dataTrackingModel1");
//        trackingData = (ArrayList<DataTrackingModel>)mIntent.getSerializableExtra("dataTrackingM");
//        dataTrackings = (ArrayList<ArrayList<DataTracking>>) mIntent.getSerializableExtra("dataTrackings");
//        dataTrackingModels = (ArrayList<ArrayList<DataTrackingModel>>) mIntent.getSerializableExtra("dataTrackingModels");
        startTime.setText("Start time: "+stf.format(dataTracking1.getStartTime()));
        endTime.setText("End time: "+convert);
        meetTime.setText("Meet time: "+stf.format(dataTracking1.getMeetTime()));

        Log.i(LOG_TAG,"END TIME "+endTIME);
        currLoc.setText("No Data");
        final Date startTime2 = new Date();
        final Date meetTime2 = new Date();
        startTime2.setTime(dataTracking1.getStartTime().getTime());
        meetTime2.setTime(startTime2.getTime());
        editTitle.setEnabled(true);
        Save.setEnabled(true);
//        browseStartTime.setOnClickListener(new View.OnClickListener() {
//            int startTimeMinutes2 = 0;
//            @Override
//            public void onClick(View view) {
//                final PopupMenu editStartTime = new PopupMenu(AddTrackingServiceActivity.this,browseStartTime);
//                editStartTime.getMenuInflater().inflate(R.menu.edit_meet_time_menu,editStartTime.getMenu());
//                editStartTime.getMenu().clear();
//                for(int i = 0; i < dataTrackingModels.get(position).size();i++){
//                    editStartTime.getMenu().add(1,R.id.timeSlot1+i,i,stf.format(dataTrackingModels.get(position).get(i).getDate()));
//                }
//                editStartTime.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        int index = menuItem.getOrder();
//                        startTimeMinutes2 = (dataTrackingModels.get(position).get(index).getDate().getMinutes()+index);
//                        //dataTrackingModels.get(position).get(menuItem.getOrder()).getDate().setMinutes(startTimeMinutes);
//                        startTime2.setTime(dataTrackingModels.get(position).get(index).getDate().getTime());
//                        meetTime2.setTime(dataTrackingModels.get(position).get(index).getDate().getTime());
//                        //startTime2.setMinutes(startTimeMinutes2);
//                        startTime.setText("Start Time: "+stf.format(dataTrackingModels.get(position).get(index).getDate()));
//                        endTime.setText("End Time: "+stf.format(dataTrackings.get(position).get(index).getEndTime()));
//                        endTime2.setTime(dataTrackings.get(position).get(index).getEndTime().getTime());
//                        //chooseStartTime.setEnabled(false);
//                        browseMeetTime.setEnabled(true);
//                        return false;
//                    }
//                });
//                editStartTime.show();
//            }
//        });
        browseMeetTime.setOnClickListener(new View.OnClickListener() {
            int meetTimeMinutes2 = 0;
            @Override
            public void onClick(View view) {
                PopupMenu editMeetTime = new PopupMenu(AddTrackingServiceActivity.this,browseMeetTime);
                editMeetTime.getMenuInflater().inflate(R.menu.edit_meet_time_menu,editMeetTime.getMenu());
                editMeetTime.getMenu().clear();
                for(int j = 1; j < endTIME; j++){
                    editMeetTime.getMenu().add(1,R.id.timeSlot1+j-1,j,dataTracking1.getStartTime().getHours()+":"+(dataTracking1.getStartTime().getMinutes()+j));
                }
                editMeetTime.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        meetTimeMinutes2 = (startTime2.getMinutes()+menuItem.getOrder());
                        meetTime2.setMinutes((meetTimeMinutes2));
                        meetTime.setText("Meet time: "+stf.format(meetTime2));
                        return false;
                    }
                });
                editMeetTime.show();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //
//                    addTrackingData(mIntent.getIntExtra("CellPosition",0)+1, editTitle.getText().toString(), startTime2, endTime2,meetTime2, 0,
//                            0,trackingData.get(mIntent.getIntExtra("CellPosition",0)).getLatitude(),trackingData.get(mIntent.getIntExtra("CellPosition",0)).getLongitude());
                jdbcActivity.createNew(new DataTracking(dataTracking1.getTrackableId(),editTitle.getText().toString(),dataTracking1.getStartTime(),dataTracking1.getEndTime(),meetTime2,0.0,0.0,
                        dataTracking1.getMeetLocationlatitude(),dataTracking1.getMeetLocationlongtitude()),db);
                }
                catch (NumberFormatException e){}
                finish();
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(mMessageReceiver);
    }
}
