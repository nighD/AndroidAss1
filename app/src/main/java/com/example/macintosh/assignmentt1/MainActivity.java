package com.example.macintosh.assignmentt1;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.SearchView;

import android.app.Dialog;
import android.widget.AdapterView;
import java.lang.reflect.Field;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    //    private Activity activity;
    MyRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ArrayList<DataModel> dataa;
    private SearchView searchView;
    private RecyclerView listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle( "Search" );
        Trackable trackable = new Trackable();
        trackable.parseFile( getApplicationContext() );
        //recyclerView.setHasFixedSize(true);
        dataa = new ArrayList<>();
        for (int i = 0; i < trackable.trackableList.size(); i++) {
            dataa.add( new DataModel(
                    trackable.trackableList.get( i ).name,
                    trackable.trackableList.get( i ).description,
                    trackable.trackableList.get( i ).webURL,
                    trackable.trackableList.get( i ).Category,
                    trackable.trackableList.get( i ).getID()

            ) );
        }
        recyclerView = findViewById( R.id.recycler_view );
        //fab = (FloatingActionButton) findViewById(R.id.fab);

        adapter = new MyRecyclerViewAdapter( this.dataa, getApplicationContext(), this);

        //fab = (FloatingActionButton) findViewById(R.id.fab);
        //adapter = new MyRecyclerViewAdapter(dataa,getApplicationContext(),this);

        layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setItemAnimator( new DefaultItemAnimator() );
        recyclerView.addItemDecoration( new MyDividerItemDecoration( this, DividerItemDecoration.VERTICAL, 36 ) );
        recyclerView.setAdapter( adapter );


        //fab.setOnClickListener(onAddingListener());
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
                final Dialog dialog = new Dialog( MainActivity.this );
                dialog.setContentView( R.layout.dialog_add ); //layout for dialog
                dialog.setTitle( "Add a new friend" );
                dialog.setCancelable( false ); //none-dismiss when touching outside Dialog

                // set the custom dialog components - texts and image
                EditText name = (EditText) dialog.findViewById( R.id.name );
                //EditText job = (EditText) dialog.findViewById( R.id.job );
                Spinner spnGender = (Spinner) dialog.findViewById( R.id.gender );
                View btnAdd = dialog.findViewById( R.id.btn_ok );
                View btnCancel = dialog.findViewById( R.id.btn_cancel );

                //set spinner adapter
                ArrayList<String> gendersList = new ArrayList<>();
                gendersList.add( "Male" );
                gendersList.add( "Female" );
                ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>( MainActivity.this,
                        android.R.layout.simple_dropdown_item_1line, gendersList );
                spnGender.setAdapter( spnAdapter );

                //set handling event for 2 buttons and spinner
                spnGender.setOnItemSelectedListener( onItemSelectedListener() );
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
        //adapter.get
        // listening to search query text change
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                //adapter.
                adapter.getFilter().filter( query );
                //adapter.getFilter().convertResultToString( adapter.getFilter(). )
                System.out.println( query );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter( query );
                //System.out.println(query);
                return false;
            }
        } );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility( flags );
            getWindow().setStatusBarColor( Color.WHITE );
        }
    }


}











