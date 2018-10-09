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
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.widget.Button;
import android.widget.Toast;

import com.example.macintosh.assignmentt1.JDBC.JDBCActivity;
import com.example.macintosh.assignmentt1.ModelClass.DataModel;
import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.ModelClass.DataTrackingModel;
import com.example.macintosh.assignmentt1.R;
import com.example.macintosh.assignmentt1.ModelClass.Trackable;
import com.example.macintosh.assignmentt1.ModelClass.TrackingService;
import com.example.macintosh.assignmentt1.ViewAdapter.MyRecyclerViewAdapter;
import com.example.macintosh.assignmentt1.ViewAdapter.RecyclerViewDialogAdapter;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;

public class ShowFragment extends AppCompatActivity {
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
    private static ArrayList<ArrayList<DataTrackingModel>> dataTrackingModels;
    JDBCActivity jdbcActivity = new JDBCActivity();

    String TIME_FORMAT = "hh:mm";
    SimpleDateFormat stf = new SimpleDateFormat(TIME_FORMAT);
//    final String db = "jdbc:sqldroid:" + getDatabasePath("assignment1.db").getAbsolutePath();
    //JDBCActivity jdbcActivity = new JDBCActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        setContentView(R.layout.fraglayout);
        mIntent = getIntent();
        dataa = new ArrayList<>();
        dataTrackings = new ArrayList<>();
        trackingData = new ArrayList<>();
        dataa = (ArrayList<DataModel>)mIntent.getSerializableExtra("dataModels");
        trackingData = (ArrayList<DataTrackingModel>)mIntent.getSerializableExtra("dataTrackingM");
//        dataTrackings = (ArrayList<ArrayList<DataTracking>>) mIntent.getSerializableExtra("dataTrackings");
//        dataTrackingModels = (ArrayList<ArrayList<DataTrackingModel>>) mIntent.getSerializableExtra("dataTrackingModels");
        dataTrackings = MainActivity.getDataTrackings();
        dataTrackingModels = MainActivity.getDataTrackings2();
        addButton = findViewById(R.id.btn_add);
        rv=findViewById(R.id.mRecyerID);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //ADAPTER
        try {
            adapter=new RecyclerViewDialogAdapter(this,trackingData,dataa,mIntent.getIntExtra("CellPosition",0),this.dataTrackings.get(mIntent.getIntExtra("CellPosition",0)),"jdbc:sqldroid:" + getDatabasePath("assignment1.db").getAbsolutePath());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rv.setAdapter(adapter);
        final String db = "jdbc:sqldroid:" + getDatabasePath("assignment1.db").getAbsolutePath();

        jdbcActivity.turnOnConnection( db );
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
                            final TextView startTime = mView.findViewById(R.id.startTimeTS);
                            final TextView endTime = mView.findViewById(R.id.endTimeTS);
                            final TextView meetTime = mView.findViewById(R.id.meetTimeTS);
                            TextView currLoc = mView.findViewById(R.id.currLocTS);
                            if(dataTrackings.get(position).isEmpty()==false){
                                startTime.setText("Start time: "+stf.format(dataTrackings.get(position).get(0).getStartTime()));
                                endTime.setText("End time: "+stf.format(dataTrackings.get(position).get(0).getEndTime()));
                                meetTime.setText("Meet time: "+stf.format(dataTrackings.get(position).get(0).getMeetTime()));
                            }
                            else{
                                Date endTime2 = new Date();
                                endTime2.setTime(dataTrackingModels.get(position).get(0).getDate().getTime());
                                endTime2.setMinutes((endTime2.getMinutes()+dataTrackingModels.get(position).get(0).getStopTime()));
                                startTime.setText("Start time: "+stf.format(dataTrackingModels.get(position).get(0).getDate()));
                                endTime.setText("End time: "+stf.format(endTime2));
                                meetTime.setText("Meet time: "+stf.format(dataTrackingModels.get(position).get(0).getDate()));
                            }
                            currLoc.setText("No Data");
                            final Date startTime2 = new Date();
                            final Date meetTime2 = new Date();
                            final Date endTime2 = new Date();
                            final ImageButton chooseStartTime = mView.findViewById(R.id.browseStartTime);
                            final ImageButton chooseMeetTime = mView.findViewById(R.id.browseMeetTime);
                            Button SaveMyName = mView.findViewById(R.id.SaveNow);
                            Write.setEnabled(true);
                            SaveMyName.setEnabled(true);
                            mBuilder.setView(mView);
                            dialog = mBuilder.create();
                            chooseMeetTime.setEnabled(false);
                            chooseStartTime.setOnClickListener(new View.OnClickListener() {
                                int startTimeMinutes2 = 0;
                                @Override
                                public void onClick(View view) {
                                    final PopupMenu editStartTime = new PopupMenu(ShowFragment.this,chooseStartTime);
                                    editStartTime.getMenuInflater().inflate(R.menu.edit_meet_time_menu,editStartTime.getMenu());
                                    editStartTime.getMenu().clear();
                                    for(int i = 0; i < dataTrackingModels.get(position).size();i++){
                                        editStartTime.getMenu().add(1,R.id.timeSlot1+i,i,stf.format(dataTrackingModels.get(position).get(i).getDate()));
                                    }
                                    editStartTime.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem menuItem) {
                                            int index = menuItem.getOrder();
                                            startTimeMinutes2 = (dataTrackingModels.get(position).get(index).getDate().getMinutes()+index);
                                            //dataTrackingModels.get(position).get(menuItem.getOrder()).getDate().setMinutes(startTimeMinutes);
                                            startTime2.setTime(dataTrackingModels.get(position).get(index).getDate().getTime());
                                            meetTime2.setTime(dataTrackingModels.get(position).get(index).getDate().getTime());
                                            //startTime2.setMinutes(startTimeMinutes2);
                                            startTime.setText("Start Time: "+stf.format(dataTrackingModels.get(position).get(index).getDate()));
                                            endTime.setText("End Time: "+stf.format(dataTrackings.get(position).get(index).getEndTime()));
                                            endTime2.setTime(dataTrackings.get(position).get(index).getEndTime().getTime());
                                            //chooseStartTime.setEnabled(false);
                                            chooseMeetTime.setEnabled(true);
                                            return false;
                                        }
                                    });
                                    editStartTime.show();

                                }
                            });
                            chooseMeetTime.setOnClickListener(new View.OnClickListener() {
                                int meetTimeMinutes2 = 0;
                                @Override
                                public void onClick(View view) {
                                    PopupMenu editMeetTime = new PopupMenu(ShowFragment.this,chooseMeetTime);
                                    editMeetTime.getMenuInflater().inflate(R.menu.edit_meet_time_menu,editMeetTime.getMenu());
                                    editMeetTime.getMenu().clear();
                                    int e = 0;
                                    for(int i = 0; i < dataTrackingModels.get(position).size();i++){
                                        if(dataTrackingModels.get(position).get(i).getDate().getTime()==startTime2.getTime()){
                                            for(int j = 1; j < dataTrackingModels.get(position).get(i).getStopTime(); j++){
                                                editMeetTime.getMenu().add(1,R.id.timeSlot1+j-1,j,dataTrackingModels.get(position).get(i).getDate().getHours()+":"+(dataTrackingModels.get(position).get(i).getDate().getMinutes()+j));
                                            }
                                        }
                                    }
                                    editMeetTime.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem menuItem) {
                                            int index = menuItem.getOrder();
                                            meetTimeMinutes2 = (startTime2.getMinutes()+menuItem.getOrder());
//                                            meetTime2.setTime(dataTrackingModels.get(position).get(index).getDate().getTime());
                                            meetTime2.setMinutes((meetTimeMinutes2));
                                            meetTime.setText("Meet time: "+stf.format(meetTime2));
                                            //chooseMeetTime.setEnabled(false);
                                            return false;
                                        }
                                    });
                                    editMeetTime.show();
                                }
                            });
                            SaveMyName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        addTrackingData(mIntent.getIntExtra("CellPosition",0)+1, Write.getText().toString(), startTime2, endTime2,meetTime2, 0,
                                            0,trackingData.get(mIntent.getIntExtra("CellPosition",0)).getLatitude(),trackingData.get(mIntent.getIntExtra("CellPosition",0)).getLongitude());
                                        adapter.addTrackingData(position,mIntent.getIntExtra("CellPosition",0)+1, Write.getText().toString(), startTime2, endTime2,meetTime2, 0,
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
                            jdbcActivity.createNew(new DataTracking(mIntent.getIntExtra("CellPosition",0)+1, Write.getText().toString(), startTime2, endTime2,meetTime2, 0,
                                    0,trackingData.get(mIntent.getIntExtra("CellPosition",0)).getLatitude(),trackingData.get(mIntent.getIntExtra("CellPosition",0)).getLongitude()),"jdbc:sqldroid:" + getDatabasePath("assignment1.db").getAbsolutePath());
                            Intent updateAdd = new Intent();
                            updateAdd.putExtra("action","add");
                            updateAdd.putExtra("updateAdd",dataTrackings);
                            updateAdd.putExtra("Position",position);
                            setResult(RESULT_OK,updateAdd);

                        }
                    }
                });
                //jdbcActivity.turnOffConnection();
            }

        };

    }
    public void addTrackingData(int ID, String title, Date startTime, Date endTime, Date meetTime, double currLat, double currLong,
                                double meetLat, double meetLong){
        this.dataTrackings.get(ID).add(new DataTracking(ID,title,startTime,endTime,meetTime,currLat,currLong,meetLat,meetLong));
    }
//    @Override
//    public void onStop(){
//        this.dataTracking2 = this.dataTrackings;
//        super.onStop();
//    }
@Override
protected  void onStop(){
    super.onStop();
    //jdbcActivity.turnOffConnection();
}

}