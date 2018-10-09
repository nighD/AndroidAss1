package com.example.macintosh.assignmentt1.JDBC;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.macintosh.assignmentt1.ModelClass.CurrentMeetLocationModel;
import com.example.macintosh.assignmentt1.ModelClass.DataTracking;
import com.example.macintosh.assignmentt1.ModelClass.DataTrackingModel;
import com.example.macintosh.assignmentt1.ModelClass.TrackingService;
import com.example.macintosh.assignmentt1.R;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class JDBCActivity
{
    private String LOG_TAG = this.getClass().getName();
     private Connection connection;
    public JDBCActivity(){

    }
    public void turnOnConnection(final String db){
        try {
            Class.forName("org.sqldroid.SQLDroidDriver");
            connection = DriverManager.getConnection( db );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void turnOffConnection(){
        try {
            Class.forName("org.sqldroid.SQLDroidDriver");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void trackingDataDatabase(final Context context, final String db){
        // DB operations should go in separate Thread
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));
                    //Connection con = DriverManager.getConnection(db);
                    Statement st = connection.createStatement();
                    //st.executeUpdate("Drop table trackingdata");
                    // Create table:
                    st.executeUpdate("create table trackingdata( " +
                            "date0 date not null, " +
                            "time0 time not null, " +
                            "trackableID int not null, " +
                            "stoptime int , " +
                            "latitude double , " +
                            "longtitude double )");
                    Log.i(LOG_TAG, "*** Created table: trackingdata");
                    SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
                    try (Scanner scanner = new Scanner(context.getResources().openRawResource( R.raw.tracking_data)))
                    {
                        // match comma and 0 or more whitespace OR trailing space and newline
                        scanner.useDelimiter(",\\s*|\\s*\\n+");
                        int begin = 0;
                        while (scanner.hasNext())
                        {
                            Date trackingDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse(scanner.next());
                           // Log.i(LOG_TAG, sdf.format( e ));
                            Scanner scanner0 = new Scanner(sdf.format(trackingDate));
                            scanner0.useDelimiter( "\\s" );
                            String date = scanner0.next();
                            String time0 = scanner0.next();
                            int trackableID = Integer.parseInt(scanner.next());
                            int stopTime = Integer.parseInt(scanner.next());
                            double latitude = Double.parseDouble(scanner.next());
                            String next=scanner.next();
                            int commentPos;
                            // strip trailing comment
                            if((commentPos=next.indexOf("//")) >=0)
                                next=next.substring(0, commentPos);
                            double longtitude = Double.parseDouble(next);
                            st.execute( "INSERT INTO trackingdata VALUES ('"+date+"', '"+time0+"', "+
                                                                                    trackableID+", "+stopTime+", "
                                                                                    +latitude +", " + longtitude+")");
                            begin ++;
                            Log.i(LOG_TAG," SIze of trackingdata " + begin);
                        }
                    }
                    catch (Resources.NotFoundException e)
                    {
                        Log.i(LOG_TAG, "File Not Found Exception Caught");
                    }
                    // Query and display results //Step 5
                    //ResultSet rs = st.executeQuery("SELECT * FROM trackingdata");
//                    Log.i(LOG_TAG, "*** Query results:");
//                    while (rs.next())
//                    {
//                        Log.i(LOG_TAG, "Date " + rs.getString(1) + ", ");
//                        Log.i(LOG_TAG, "TIME " + rs.getString(2));
//                    }
                    // Delete table: employee //Step 6
                    //st.executeUpdate("Drop table trackingdata");
                    //Log.i(LOG_TAG, "*** Deleted table: trackingdata");

                    // Release resources //Step 7
                    //rs.close();

                    st.close();
                    ////con.close();

                } catch (SQLException sqlEx)
                {
                    while (sqlEx != null)
                    {
                        Log.i(LOG_TAG,
                                "[SQLException] " + "SQLState: " + sqlEx.getSQLState()
                                        + ", Message: " + sqlEx.getMessage()
                                        + ", Vendor: " + sqlEx.getErrorCode());
                        sqlEx = sqlEx.getNextException();
                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        }).start();
    }

    public void createServiceDatabase(final String db){
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));
                    //Connection con = DriverManager.getConnection(db);
                    Statement st = connection.createStatement();
                    //st.executeUpdate("Drop table servicedata");
                    // Create table:
                    st.executeUpdate("create table servicedata( " +
                            "ID int not null, " +
                            "title char[100], " +
                            "starttime date not null, " +
                            "endtime date not null, " +
                            "meettime date not null, " +
                            "currentlatitude double, " +
                            "currentlongtitude double, " +
                            "meetlatitude double, " +
                            "meetlongtitude double)");
                    Log.i(LOG_TAG, "*** Created table: servicedata");
                    st.close();
                    //con.close();

                } catch (SQLException sqlEx)
                {
                    while (sqlEx != null)
                    {
                        Log.i(LOG_TAG,
                                "[SQLException] " + "SQLState: " + sqlEx.getSQLState()
                                        + ", Message: " + sqlEx.getMessage()
                                        + ", Vendor: " + sqlEx.getErrorCode());
                        sqlEx = sqlEx.getNextException();
                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void createNew(final DataTracking dataTracking, final String db){
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));
                    //Connection con = DriverManager.getConnection(db);
                    Statement st = connection.createStatement();
                    st.execute( "INSERT INTO servicedata VALUES("+ dataTracking.getTrackableId() +",' "
                                                                       +dataTracking.getTitle() +"','"+dataTracking.getStartTime()+"','"
                                                                        +dataTracking.getEndTime()+"', '"+dataTracking.getMeetTime()+"', "
                                                                         +dataTracking.getCurrentLocationlatitude()+", " +dataTracking.getCurrentLocationlongtitude()
                                                                          + ", "+dataTracking.getMeetLocationlatitude()+", "
                                                                           + dataTracking.getMeetLocationlongtitude()+")");
                    Log.i(LOG_TAG,"Add new data successfully");
                    st.close();
                    //con.close();
                } catch (SQLException sqlEx)
                {
                    Log.i(LOG_TAG, "Failed to add new data");
                    while (sqlEx != null)
                    {
                        Log.i(LOG_TAG,
                                "[SQLException] " + "SQLState: " + sqlEx.getSQLState()
                                        + ", Message: " + sqlEx.getMessage()
                                        + ", Vendor: " + sqlEx.getErrorCode());
                        sqlEx = sqlEx.getNextException();
                    }
                } catch (Exception ex)
                {
                    Log.i(LOG_TAG, "Failed to add new data !!!!!");
                    ex.printStackTrace();
                }

            }
        }).start();
    }
    public void changeMeetTime(final int ID,final Date date,final String db, String title){
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));
                    //Connection con = DriverManager.getConnection(db);
                    turnOnConnection( db );
                    Statement st = connection.createStatement();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                    String dateStart = sdf.format(date);
                    st.executeUpdate("update servicedata set meettime = '" + dateStart+ "' where ID =" + Integer.toString( ID ));

                    Log.i(LOG_TAG, "*** Update query: ID " +Integer.toString(ID));
                    st.close();
                    //con.close();
                    Log.i("CHANGE MEETTIME","Success");
                } catch (SQLException sqlEx)
                {
                    while (sqlEx != null)
                    {
                        Log.i(LOG_TAG,
                                "[SQLException] " + "SQLState: " + sqlEx.getSQLState()
                                        + ", Message: " + sqlEx.getMessage()
                                        + ", Vendor: " + sqlEx.getErrorCode());
                        sqlEx = sqlEx.getNextException();
                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void changetitle(final int ID,final String title,final String db,String oldTitle){
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));
                    //Connection con = DriverManager.getConnection(db);
                    turnOnConnection( db );
                    Statement st = connection.createStatement();
                    SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
                    st.executeUpdate("update servicedata set title = '"+ title +"' where title = '"+oldTitle+"'");
                    Log.i(LOG_TAG, "*** Update query: ID " +Integer.toString(ID));
                    st.close();
                    //con.close();
                    Log.i("CHANGE TITLE", "Success");

                } catch (SQLException sqlEx)
                {
                    while (sqlEx != null)
                    {
                        Log.i(LOG_TAG,
                                "[SQLException] " + "SQLState: " + sqlEx.getSQLState()
                                        + ", Message: " + sqlEx.getMessage()
                                        + ", Vendor: " + sqlEx.getErrorCode());
                        sqlEx = sqlEx.getNextException();
                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public ArrayList<DataTracking> getData(final String db){
        final ArrayList<DataTracking> dataTracking = new ArrayList<>();
                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));
                    //Connection con = DriverManager.getConnection(db);
                    Statement st = connection.createStatement();

                    // Query and display results //Step 5
                    ResultSet rs = st.executeQuery("SELECT * FROM servicedata") ;
                    Log.i(LOG_TAG, "*** Query results getData:");
                    DataTracking dataTracking0 = new DataTracking();
                    while (rs.next())
                    {
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                        Date dateStart = sdf.parse(rs.getString(3));
                        Date dateEnd = sdf.parse(rs.getString(4));
                        Date dateMeet = sdf.parse(rs.getString(5));
                        dataTracking.add(new DataTracking(Integer.parseInt( rs.getString(1) ),rs.getString( 2 ),
                                dateStart, dateEnd, dateMeet,
                                Double.parseDouble(rs.getString(6)),Double.parseDouble(rs.getString(7)),
                                Double.parseDouble(rs.getString(8)),Double.parseDouble(rs.getString(9))));
                        Log.i(LOG_TAG,"Result found");
                    }
                    Log.i(LOG_TAG, "*** query result:");


                    // Release resources //Step 7
                    rs.close();
                    st.close();
                    //con.close();

                } catch (SQLException sqlEx)
                {
                    while (sqlEx != null)
                    {
                        Log.i(LOG_TAG,
                                "[SQLException] " + "SQLState: " + sqlEx.getSQLState()
                                        + ", Message: " + sqlEx.getMessage()
                                        + ", Vendor: " + sqlEx.getErrorCode());
                        sqlEx = sqlEx.getNextException();
                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }

        Log.i(LOG_TAG,"SHITE " + dataTracking.size());
        return dataTracking;
    }

    public CurrentMeetLocationModel[] takeLatLng(final String db, Date date){
        final CurrentMeetLocationModel[] currentMeetLocationModels = new CurrentMeetLocationModel[6];
        CurrentMeetLocationModel[] currentMeetLocationModels1 = new CurrentMeetLocationModel[3];
        SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
        Scanner scanner0 = new Scanner(sdf.format(date));
        scanner0.useDelimiter( "\\s" );
        String date0 = scanner0.next();
        String startTime = scanner0.next();
        long[] compare = new long[6];
                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));
                    //Connection con = DriverManager.getConnection(db);
                    Statement st = connection.createStatement();

                    // Query and display results //Step 5
                    ResultSet rs = st.executeQuery("SELECT * FROM trackingdata where stoptime > 0");
                    Log.i(LOG_TAG, "*** Query results:");
                    //DataTracking dataTracking0 = null;
                    int begin = 0;
                    while (rs.next())
                    {
                        String endTime = rs.getString("time0");;
                        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                        Date d1 = sdf1.parse(startTime);
                        Date d2 = sdf1.parse(endTime);
                        long elapsed = d2.getTime() - d1.getTime();
                        compare[begin] = elapsed;

                        int stopTime = Integer.parseInt(rs.getString("stoptime"));
                        String convert = String.format("%02d:%02d", 0, stopTime );
                        Date stoptime1 = sdf1.parse( convert );

                        currentMeetLocationModels[begin] = new CurrentMeetLocationModel(
                                Integer.parseInt( rs.getString( "trackableID" ))
                                ,d2,stoptime1
                                ,Double.parseDouble( rs.getString("latitude"))
                                ,Double.parseDouble( rs.getString("longtitude")));
                        //Log.i(LOG_TAG,Integer.toString( currentMeetLocationModels[begin].getTrackableId() ));
                        begin++;
                        Log.i(LOG_TAG,"size of time > 0 " + begin);
                    }

                    int currentID = currentMeetLocationModels[0].getTrackableId();
                    int currentposition = 0;
                    int begin0 = 0;
                    for ( int i = 1; i <currentMeetLocationModels.length;i++){
                        Log.i(LOG_TAG,"TrackableID " +Integer.toString( currentMeetLocationModels[i].getTrackableId() ));
                        Log.i(LOG_TAG,"CurrentID " +Integer.toString(  currentID));
                        if (currentID == currentMeetLocationModels[i].getTrackableId()){
                            if(compare[currentposition] < compare[i] && compare[currentposition] > 0 && compare[i] > 0){
                                currentMeetLocationModels1[begin0] = currentMeetLocationModels[currentposition];

                                begin0 ++;
                            }
                            else if (compare[currentposition] > compare[i] && compare[currentposition] > 0 && compare[i] > 0){
                                currentMeetLocationModels1[begin0] = currentMeetLocationModels[i];

                                begin0 ++;
                            }
                            else if (compare[currentposition] < 0){
                                currentMeetLocationModels1[begin0] = currentMeetLocationModels[i];

                                begin0 ++;
                            }
                            else if (compare[i] < 0){
                                currentMeetLocationModels1[begin0] = currentMeetLocationModels[currentposition];

                                begin0 ++;
                            }
                        }
                        else if (currentID != currentMeetLocationModels[i].getTrackableId()){
                            currentID = currentMeetLocationModels[i].getTrackableId();
                            currentposition = i;
                        }
                    }
                    Log.i(LOG_TAG, "*** query result: ");

                    // Release resources //Step 7
                    rs.close();
                    st.close();
                    //con.close();





                } catch (SQLException sqlEx)
                {
                    while (sqlEx != null)
                    {
                        Log.i(LOG_TAG,
                                "[SQLException] " + "SQLState: " + sqlEx.getSQLState()
                                        + ", Message: " + sqlEx.getMessage()
                                        + ", Vendor: " + sqlEx.getErrorCode());
                        sqlEx = sqlEx.getNextException();
                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
        return currentMeetLocationModels1;
    }



    public void deleteCol ( final int ID,final String title, final String db){
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));
                    //Connection con = DriverManager.getConnection(db);
                    turnOnConnection( db );
                    Statement st = connection.createStatement();
                    Log.i(LOG_TAG,"title " + title +"'");
                    st.executeUpdate("delete from servicedata where title = '"+ title +"'");

                    Log.i(LOG_TAG, "*** Delete query : ID " +Integer.toString(ID));
                    st.close();
                    //con.close();

                } catch (SQLException sqlEx)
                {
                    while (sqlEx != null)
                    {
                        Log.i(LOG_TAG,
                                "[SQLException] " + "SQLState: " + sqlEx.getSQLState()
                                        + ", Message: " + sqlEx.getMessage()
                                        + ", Vendor: " + sqlEx.getErrorCode());
                        sqlEx = sqlEx.getNextException();
                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }).start();
        turnOffConnection();
    }



}
