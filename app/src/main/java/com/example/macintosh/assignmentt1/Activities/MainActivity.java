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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {

    //    private Activity activity;
    private static final String YES_ACTION = "com.example.macintosh.assignmentt1.Activities.YES_ACTION";
    private static final String MAYBE_ACTION = "com.example.macintosh.assignmentt1.Activities.MAYBE_ACTION";
    private static final String NO_ACTION = "com.example.macintosh.assignmentt1.Activities.NO_ACTION";
    MyRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ArrayList<DataModel> dataa;
    private ArrayList<DataTrackingModel> trackingData;
    private ArrayList<ArrayList<DataTracking>> dataTrackings;
    private SearchView searchView;
    private ProgressBar bar = null;
    private WebView webView = null;
    private BroadcastReceiver broadcastReceiver;
    private String LOG_TAG = this.getClass().getName();
    private ImageButton locationBtn;
    private Button testAct;
    private String string0;
    private static final int REMINDER_TIME = 10;
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
        final String db = "jdbc:sqldroid:" + getDatabasePath("ass1.db").getAbsolutePath();
        JDBCActivity jdbcActivity = new JDBCActivity();
        jdbcActivity.trackingDataDatabase(getApplicationContext(),db);
        //jdbcActivity.takeLatLng( db );
        jdbcActivity.createServiceDatabase(db);
        Date date = new Date(  );
        Log.i(LOG_TAG,"Create NEW");
        jdbcActivity.createNew(new DataTracking(0,"No data",date,date,date,0,0,0,0  ),db  );
        dataa = new ArrayList<>();
        trackingData = new ArrayList<>();
        dataTrackings = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //new HttpClientApacheAsyncTask(this,"https://maps.googleapis.com/maps/api/distancematrix/json?origins=-37.81289833333334,144.0&destinations=-37.8207,144.958&key=AIzaSyCDbBGQ8CaRLa4rvOhsaG-LO0Rxy0CUGxI").execute();




        NotificationScheduler.setReminder(MainActivity.this, REMINDER_TIME);

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
        for (int i = 0; i < trackingService.trackingList.size(); i++){
            jdbcActivity.createNew(new DataTracking(trackingService.trackingList.get(i).trackableId,
                    "No Data",
                    trackingService.trackingList.get(i).date,
                    trackingService.trackingList.get(i).date,
                    trackingService.trackingList.get(i).date,
                    0.0,0.0,
                    trackingService.trackingList.get(i).latitude,
                    trackingService.trackingList.get(i).longitude),db);
        }

        recyclerView = findViewById( R.id.recycler_view );
        for (int i = 0; i < trackable.trackableList.size(); i++){
            this.dataTrackings.add(new ArrayList<DataTracking>());
        }
//        for(int i = 0; i < trackingService.trackingList.size(); i++){
//            for (int j = 0; j < trackable.trackableList.size(); j++){
//                if (trackingService.trackingList.get(i).trackableId==j+1){
//                    jdbcActivity.getData(j+1,db);
//                    for(int k = 0; k < jdbcActivity.getData(j+1,db).size(); k++){
//                        this.dataTrackings.get(j).add(jdbcActivity.getData(j+1,db).get(k));
//                    }
//                    if(jdbcActivity.getData(i,db) == null){
//                        Log.i(LOG_TAG,"Failed to get data !!!");
//                        jdbcActivity.createNew(new DataTracking(i,
//                                "No Data",
//                                trackingService.trackingList.get(0).date,
//                                trackingService.trackingList.get(0).date,
//                                trackingService.trackingList.get(0).date,
//                                0.0,0.0,
//                                0.0,
//                                0.0),db);
//                    }
//                }
//            }
//        }
        for (int i = 0; i < trackable.trackableList.size(); i++){
            ArrayList<DataTracking> newArray = jdbcActivity.getData(i,db);
            for (int j = 0; j < newArray.size(); j++){
                this.dataTrackings.get(i).add(newArray.get(j));
            }
        }
        for (int i = 0; i < dataTrackings.size(); i++){
            if (dataTrackings.get(i).isEmpty()){
                this.dataTrackings.get(i).add(new DataTracking());
            }
        }
//        this.dataTrackings = dataTrackings;
        this.dataTrackings.get(0);
//        addTrackingData(1, "lala", new Date(), new Date(),new Date(), 0.0,0.1,0.2,0.3);
        dataTrackings.get(5).get(0).getTrackableId();
        testAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG_TAG,jdbcActivity.getData(0,db).toString());
                Intent testIntent = new Intent();
                Date EndTime2 = new Date();
                EndTime2.setTime(trackingData.get(2).getDate().getTime());
                EndTime2.setMinutes((EndTime2.getMinutes()+trackingData.get(2).getStopTime()));
                testIntent.setClass(MainActivity.this,AddTrackingServiceActivity.class);
                testIntent.putExtra("dataTrackingModel1",trackingData.get(2));
                testIntent.putExtra("dataTracking1", new DataTracking(trackingData.get(2).getTrackableId(),"No Data",
                        trackingData.get(2).getDate(),EndTime2,trackingData.get(2).getDate(),0.0,0.0,
                        trackingData.get(2).getLatitude(),trackingData.get(2).getLongitude()));

                MainActivity.this.startActivity(testIntent);
            }
        });
        try {
            adapter = new MyRecyclerViewAdapter( this.dataa, this.trackingData,this.dataTrackings, getApplicationContext(), this);
        }
        catch (ParseException e){}
        layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setItemAnimator( new DefaultItemAnimator() );
        recyclerView.addItemDecoration( new MyDividerItemDecoration( this, DividerItemDecoration.VERTICAL, 36 ) );
        recyclerView.setAdapter( adapter );


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


    public void updateProgress(int progress)
    {
        //bar.setProgress(progress);
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

       // int position = notificationModel.getID();
        int id  = currentMeetLocationModel.getTrackableId() - 1;
        String truckName = dataa1.get(id).getName();
        Log.i(LOG_TAG,"Position " + id);
        NotificationScheduler.showNotification(context,currentMeetLocationModel
                ,truckName,id, notificationModel);
    }



}












