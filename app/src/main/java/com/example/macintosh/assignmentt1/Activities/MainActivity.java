package com.example.macintosh.assignmentt1.Activities;
import android.Manifest;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.macintosh.assignmentt1.AlarmReceiver.AlarmReceiver;
import com.example.macintosh.assignmentt1.HTTP.AbstractHttpAsyncTask;
import com.example.macintosh.assignmentt1.HTTP.HttpClientApacheAsyncTask;
import com.example.macintosh.assignmentt1.JDBC.JDBCActivity;
import com.example.macintosh.assignmentt1.ModelClass.DataModel;
import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.ModelClass.DataTrackingModel;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Intent myIntent = new Intent();
        myIntent.setClass(MainActivity.this,TestPermissionsActivity.class);
        this.startActivity(myIntent);
        setContentView( R.layout.activity_main );
        locationBtn = findViewById(R.id.browseLocation);
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
        dataa = new ArrayList<>();
        trackingData = new ArrayList<>();
        dataTrackings = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //new HttpClientApacheAsyncTask(this).execute();

        if(!runtime_permissions()) {
            Log.i(LOG_TAG,"HERE1");
            Intent intent =new Intent(getApplicationContext(),GPS_Service.class);
            startService(intent);
            Log.i(LOG_TAG,"HERE0");
        }




        NotificationScheduler.setReminder(MainActivity.this, 10);

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
            jdbcActivity.createNew(new DataTracking(0,
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
//        jdbcActivity.getData(0,db);
        for(int i = 0; i < trackingService.trackingList.size(); i++){
            for (int j = 0; j < trackable.trackableList.size(); j++){
                if (trackingService.trackingList.get(i).trackableId==j+1){
                    this.dataTrackings.get(j).add(jdbcActivity.getData(j,db));
                }
            }
        }
        for (int i = 0; i < dataTrackings.size(); i++){
            if (dataTrackings.get(i).isEmpty()){
                dataTrackings.get(i).add(new DataTracking());
            }
        }
        this.dataTrackings.get(0);
//        addTrackingData(1, "lala", new Date(), new Date(),new Date(), 0.0,0.1,0.2,0.3);
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
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    Log.i(LOG_TAG,"\n" +intent.getExtras().get("coordinates"));

                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
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

    public void displayHTML(String htmlText)
    {
        // if you just wanted to display directly from URL without progress
        // webView.loadUrl(AbstractHttpAsyncTask.TEST_URL);
        //webView.loadData(htmlText,
//              "text/html", null);
        //webView.loadDataWithBaseURL( AbstractHttpAsyncTask.DistanceURL, htmlText,
              //  "text/html", null, null);
    }
    }












