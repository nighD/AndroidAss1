package com.example.macintosh.assignmentt1.Activities;
import android.Manifest;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;

import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.macintosh.assignmentt1.Divider.MyDividerItemDecoration;
import com.example.macintosh.assignmentt1.JDBC.JDBCActivity;
import com.example.macintosh.assignmentt1.ModelClass.CurrentMeetLocationModel;
import com.example.macintosh.assignmentt1.ModelClass.DataModel;
import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.ModelClass.DataTrackingModel;
import com.example.macintosh.assignmentt1.ModelClass.NotificationModel;
import com.example.macintosh.assignmentt1.NotificationScheduler.NotificationScheduler;
import com.example.macintosh.assignmentt1.R;
import com.example.macintosh.assignmentt1.ModelClass.Trackable;
import com.example.macintosh.assignmentt1.ModelClass.TrackingService;
import com.example.macintosh.assignmentt1.Service.GPS_Service;
import com.example.macintosh.assignmentt1.ViewAdapter.MyRecyclerViewAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //    private Activity activity;

    MyRecyclerViewAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ArrayList<DataModel> dataa;
    private ArrayList<DataTrackingModel> trackingData;
    private static ArrayList<ArrayList<DataTracking>> dataTrackings;
    private static ArrayList<ArrayList<DataTrackingModel>> dataTrackings2;
    private SearchView searchView;
    private ProgressBar bar = null;
    private WebView webView = null;
    private BroadcastReceiver broadcastReceiver;
    private String LOG_TAG = this.getClass().getName();
    private ImageButton locationBtn;
    private Button testAct;
    private String string0;
    JDBCActivity jdbcActivity = new JDBCActivity();
    private static final int REMINDER_TIME = 10;
    private static final int CHECK_TIME = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Intent myIntent = new Intent();
        myIntent.setClass(MainActivity.this,TestPermissionsActivity.class);
        this.startActivity(myIntent);
        ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(MainActivity.this, "Internet is connected",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Internet is disconnected",Toast.LENGTH_SHORT).show();
        }
        setContentView( R.layout.activity_main );
        locationBtn = findViewById(R.id.browseLocation);
        testAct = findViewById(R.id.testAddActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle( "Search" );
        Trackable trackable = new Trackable();
        trackable.parseFile( getApplicationContext() );
        TrackingService trackingService = new TrackingService();
        trackingService.parseFile(getApplicationContext());
        final String db = "jdbc:sqldroid:" + getDatabasePath("assignment1.db").getAbsolutePath();
        jdbcActivity.turnOnConnection( db );
        jdbcActivity.trackingDataDatabase(getApplicationContext(),db);
        //jdbcActivity.takeLatLng( db );
        jdbcActivity.createServiceDatabase(db);
        Date date = new Date(  );

        //jdbcActivity.createNew(new DataTracking(1,"No data",date,date,date,0,0,0,0  ),db  );

        dataa = new ArrayList<>();
        trackingData = new ArrayList<>();
        dataTrackings = new ArrayList<>();
        dataTrackings2 = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationIntent = new Intent();
                locationIntent.setClass(MainActivity.this,MapsActivity.class);
                MainActivity.this.startActivity(locationIntent);
            }
        });


        for (int i = 0; i < trackable.trackableList.size(); i++) {
            dataa.add( new DataModel(
                    trackable.trackableList.get( i ).name,
                    trackable.trackableList.get( i ).description,
                    trackable.trackableList.get( i ).webURL,
                    trackable.trackableList.get( i ).Category,
                    trackable.trackableList.get( i ).getID()

            ) );
        }
        for (int i = 0; i < trackingService.trackingList.size(); i++){
            trackingData.add(new DataTrackingModel(trackingService.trackingList.get(i).date,
                    trackingService.trackingList.get(i).date.getTime(),
                    trackingService.trackingList.get(i).trackableId,
                    trackingService.trackingList.get(i).stopTime,
                    trackingService.trackingList.get(i).latitude,
                    trackingService.trackingList.get(i).longitude));
        }
        recyclerView = findViewById( R.id.recycler_view );
        for(int i = 0; i < dataa.size(); i++)
        {
            dataTrackings2.add(new ArrayList<DataTrackingModel>());
        }
        for(int i = 0; i < dataa.size(); i++)
        {
            dataTrackings.add(new ArrayList<DataTracking>());
        }
        ArrayList<DataTracking> databaseData = new ArrayList<>();
        databaseData = jdbcActivity.getData(db);
        if(databaseData.isEmpty()){
            Log.i(LOG_TAG,"DATABASE IS EMPTY !!!!!");
            for (int i = 0; i < dataa.size(); i++ ) {
                for(int j = 0; j < trackingData.size(); j++){
                    if((trackingData.get(j).getTrackableId()==i+1)&&(trackingData.get(j).getStopTime()!=0)){
                        Date StartTime = new Date();
                        Date Endtime = new Date();
                        Date MeetTime = new Date();
                        StartTime.setTime(trackingData.get(j).getDate().getTime());
                        MeetTime.setTime(trackingData.get(j).getDate().getTime());
                        Endtime.setTime(trackingData.get(j).getDate().getTime());
                        Endtime.setMinutes((Endtime.getMinutes()+trackingData.get(j).getStopTime()));
                        this.dataTrackings2.get(i).add(trackingData.get(j));
//                        jdbcActivity.createNew(new DataTracking(trackingData.get(j).getTrackableId(), "No Tracking Data",
//                                StartTime,
//                                Endtime,
//                                MeetTime
//                                , 0, 0, trackingData.get(j).getLatitude(), trackingData.get(j).getLongitude()),db);
                    }
                }
                Log.i(LOG_TAG,"DataModels size: "+this.dataTrackings2.size());
            }
            for (int i = 0; i < dataa.size(); i++ ) {
                if (dataTrackings2.get(i).isEmpty()) {
                    this.dataTrackings2.get(i).add(new DataTrackingModel(new Date(),0,i+1,5,0,0));
                }
            }
//
//            for (int i = 0; i < dataa.size(); i++ ) {
//                for (int j = 0; j < dataTrackings2.get(i).size(); j++){
//                    Date StartTime = new Date();
//                    Date Endtime = new Date();
//                    Date MeetTime = new Date();
//                    StartTime.setTime(dataTrackings2.get(i).get(j).getDate().getTime());
//                    MeetTime.setTime(dataTrackings2.get(i).get(j).getDate().getTime());
//                    Endtime.setTime(dataTrackings2.get(i).get(j).getDate().getTime());
//                    Endtime.setMinutes((Endtime.getMinutes()+dataTrackings2.get(i).get(j).getStopTime()));
//                    this.dataTrackings.get(i).add(new DataTracking(dataTrackings2.get(i).get(j).getTrackableId(), "No Tracking Data",
//                            StartTime,
//                            Endtime,
//                            MeetTime
//                            , 0, 0, dataTrackings2.get(i).get(j).getLatitude(), dataTrackings2.get(i).get(j).getLongitude()));
//
//                    Log.i("HEREE",Integer.toString( i ));
//                }
//            }
        }
        else {
//            Log.i(LOG_TAG,"DATABASE DATA = "+databaseData.get(4).getTrackableId());
//            Log.i(LOG_TAG,"DATABASE DATA = "+databaseData.get(1).getStartTime().toString());
            for (int i = 0; i < dataa.size(); i++ ) {
                for(int j = 0; j < trackingData.size(); j++){
                    if((trackingData.get(j).getTrackableId()==i+1)&&(trackingData.get(j).getStopTime()!=0)){
                        dataTrackings2.get(i).add(trackingData.get(j));
                    }
                }
            }
            for (int i = 0; i < dataa.size(); i++ ) {
                if (dataTrackings2.get(i).isEmpty()) {
                    this.dataTrackings2.get(i).add(new DataTrackingModel(new Date(),0,i+1,5,0,0));
                }
            }
            for (int i = 0;  i < dataa.size(); i++){
                this.dataTrackings.get(i).clear();
            }
            for(int i = 0; i < databaseData.size(); i++){
                for (int j = 0; j < this.dataa.size(); j++ ){
                    if( databaseData.get(i).getTrackableId() == j+1){
                        Log.i(LOG_TAG," J  " + j);
                        Log.i(LOG_TAG," ID  " + databaseData.get(i).getTrackableId());
                        this.dataTrackings.get(j).add(databaseData.get(i));
                    }
                }
            }
        }
        Log.i(LOG_TAG,"DataModels size: "+this.dataTrackings2.size());
        testAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG_TAG,jdbcActivity.getData(db).toString());
                Intent testIntent = new Intent();
                Date EndTime2 = new Date();
                EndTime2.setTime(trackingData.get(2).getDate().getTime());
                EndTime2.setMinutes((EndTime2.getMinutes()+trackingData.get(2).getStopTime()));
                testIntent.setClass(MainActivity.this,AddTrackingServiceActivity.class);
                testIntent.putExtra("dataTrackingModel1",trackingData.get(2));
                testIntent.putExtra("dataTracking1", new DataTracking(trackingData.get(2).getTrackableId(),"No Data",
                        trackingData.get(2).getDate(),EndTime2,trackingData.get(2).getDate(),0.0,0.0,
                        trackingData.get(2).getLatitude(),trackingData.get(2).getLongitude()));

                MainActivity.this.startActivityForResult(testIntent,2);
            }
        });
        try {
            adapter = new MyRecyclerViewAdapter( this.dataa,this.trackingData,this.dataTrackings2, this.dataTrackings, getApplicationContext(), this,db);
        }
        catch (ParseException e){}
        layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setItemAnimator( new DefaultItemAnimator() );
        recyclerView.addItemDecoration( new MyDividerItemDecoration( this, DividerItemDecoration.VERTICAL, 36 ) );
        recyclerView.setAdapter( adapter );

        //NotificationScheduler.setReminder(MainActivity.this, REMINDER_TIME);
        NotificationScheduler.setReminderNoti( MainActivity.this,CHECK_TIME );
    }
    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu, menu );

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE );
        searchView = (SearchView) menu.findItem( R.id.action_search )
                .getActionView();
        searchView.setSearchableInfo( searchManager
                .getSearchableInfo( getComponentName() ) );
        searchView.setMaxWidth( Integer.MAX_VALUE );
        // listening to search query text change
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter( query );
                System.out.println( query );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter( query );
                return false;
            }
        } );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }


        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified( true );
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckAvailability.activityResumed();// On Resume notify the Application
        //runtime_permissions();

    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG,"STOP");
        jdbcActivity.turnOffConnection();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    @Override
    protected void onPause() {

        super.onPause();
        CheckAvailability.activityPaused();// On Pause notify the Application
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 25 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }
            @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Intent intent =new Intent(getApplicationContext(),GPS_Service.class);
                startService(intent);
            }else {
                runtime_permissions();
            }
        }
    }

    public void displayNotification(NotificationModel notificationModel, Context context, CurrentMeetLocationModel currentMeetLocationModel)
    {
        ArrayList<DataModel> dataa1 = new ArrayList<DataModel>(  );
        Trackable trackable = new Trackable();
        trackable.parseFile(context );
        for (int i = 0; i < trackable.trackableList.size(); i++) {
            dataa1.add( new DataModel(
                    trackable.trackableList.get( i ).name,
                    trackable.trackableList.get( i ).description,
                    trackable.trackableList.get( i ).webURL,
                    trackable.trackableList.get( i ).Category,
                    trackable.trackableList.get( i ).getID()

            ) );
        }
        int id  = currentMeetLocationModel.getTrackableId() - 1;
        String truckName = dataa1.get(id).getName();
        Log.i(LOG_TAG,"Position " + id);
        NotificationScheduler.showNotification(context,currentMeetLocationModel
                ,truckName,id, notificationModel);
    }
    public void displayNoti(Context context, DataTracking dataTracking, NotificationModel notificationModel, LatLng latLng,LatLng latLng1){
        NotificationScheduler.showNoti( context,dataTracking,notificationModel,latLng,latLng1 );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){

            case RESULT_OK:

                // ... Check for some data from the intent
                if(requestCode == 1){
                    // .. lets toast again
                    int position = -1;
                    if(data != null){
                        position = data.getIntExtra("Position", 0);
                        adapter.notifyDataSetChanged();
                    }

                    if(position != -1) {
                        Toast.makeText(this, "Handled the result successfully at position " + position, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Failed to get data from intent" , Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case RESULT_CANCELED:

                // ... Handle this situation
                break;
        }
    }

    public static ArrayList<ArrayList<DataTracking>> getDataTrackings() {
        return dataTrackings;
    }

    public static ArrayList<ArrayList<DataTrackingModel>> getDataTrackings2() {return dataTrackings2;}
}












