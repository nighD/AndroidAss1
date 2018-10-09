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

import java.text.ParseException;
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
    private DataTracking dataTracking1;
    private DataTrackingModel dataTrackingModel1;
   private JDBCActivity jdbcActivity = new JDBCActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.mIntent = getIntent();
        final String db = "jdbc:sqldroid:" + getDatabasePath("assignment1.db").getAbsolutePath();

        jdbcActivity.trackingDataDatabase(getApplicationContext(),db);
        jdbcActivity.turnOnConnection( db );
        jdbcActivity.createServiceDatabase(db);
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
        startTime.setText("Start time: "+stf.format(dataTracking1.getStartTime()));
        endTime.setText("End time: "+convert);
        meetTime.setText("Meet time: "+stf.format(dataTracking1.getMeetTime()));

        Log.i(LOG_TAG,"END TIME "+convert);
        currLoc.setText("No Data");
        final Date startTime2 = new Date();
        final Date meetTime2 = new Date();
        final Date endTime2 = parseDate( convert );
        Log.i(LOG_TAG,"END TIME "+endTime2.toString());
        startTime2.setTime(dataTracking1.getStartTime().getTime());
        meetTime2.setTime(startTime2.getTime());
        editTitle.setEnabled(true);
        Save.setEnabled(true);
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
                    Log.i(LOG_TAG,"END TIME " + endTime2.getTime());
                jdbcActivity.createNew(new DataTracking(dataTracking1.getTrackableId(),editTitle.getText().toString(),dataTracking1.getStartTime(),endTime2,meetTime2,0.0,0.0,
                        dataTracking1.getMeetLocationlatitude(),dataTracking1.getMeetLocationlongtitude()),db);
                }
                catch (NumberFormatException e){}

                finish();
            }
        });

    }
    @Override
    public void onDestroy() {
        //jdbcActivity.turnOffConnection();
        super.onDestroy();
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("hh:mm").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
