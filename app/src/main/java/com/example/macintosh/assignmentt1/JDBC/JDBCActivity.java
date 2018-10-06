package com.example.macintosh.assignmentt1.JDBC;



import android.app.Activity;
import android.content.Context;
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

    public JDBCActivity(){

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
                    Connection con = DriverManager.getConnection(db);
                    Statement st = con.createStatement();
                    st.executeUpdate("Drop table trackingdata");
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
                    con.close();

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
                    Connection con = DriverManager.getConnection(db);
                    Statement st = con.createStatement();
                    st.executeUpdate("Drop table servicedata");
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
                    con.close();

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
                    Connection con = DriverManager.getConnection(db);
                    Statement st = con.createStatement();
                    st.execute( "INSERT INTO servicedata VALUES("+ dataTracking.getTrackableId() +",' "
                                                                       +dataTracking.getTitle() +"','"+dataTracking.getStartTime()+"','"
                                                                        +dataTracking.getEndTime()+"', '"+dataTracking.getMeetTime()+"', "
                                                                         +dataTracking.getCurrentLocationlatitude()+", " +dataTracking.getCurrentLocationlongtitude()
                                                                          + ", "+dataTracking.getMeetLocationlatitude()+", "
                                                                           + dataTracking.getMeetLocationlongtitude()+")");
                    Log.i(LOG_TAG,"Add new data successfully");
                    st.close();
                    con.close();
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
    public void changeMeetTime(final int ID,final Date date,final String db){
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));
                    Connection con = DriverManager.getConnection(db);
                    Statement st = con.createStatement();
                    st.execute("update servicedata set meettime = '"+date+"' where ID= "+Integer.toString(ID));
                    Log.i(LOG_TAG, "*** Update query: ID " +Integer.toString(ID));
                    st.close();
                    con.close();

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

    public void changetitle(final int ID,final String title,final String db){
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));
                    Connection con = DriverManager.getConnection(db);
                    Statement st = con.createStatement();
                    SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
                    st.execute("update servicedata set title = '"+ title +"' where ID= "+Integer.toString(ID));
                    Log.i(LOG_TAG, "*** Update query: ID " +Integer.toString(ID));
                    st.close();
                    con.close();

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

    public ArrayList<DataTracking> getData(final int ID, final String db){
        final ArrayList<DataTracking> dataTracking = new ArrayList<>();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));
                    Connection con = DriverManager.getConnection(db);
                    Statement st = con.createStatement();

                    // Query and display results //Step 5
                    ResultSet rs = st.executeQuery("SELECT * FROM servicedata WHERE ID="+ID) ;
                    Log.i(LOG_TAG, "*** Query results:");
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
                    Log.i(LOG_TAG, "*** query result:ID "+ Integer.toString(ID));
                    Log.i(LOG_TAG, dataTracking.toString());

                    // Release resources //Step 7
                    rs.close();
                    st.close();
                    con.close();

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
        return dataTracking;
    }

    public CurrentMeetLocationModel[] takeLatLng(final String db){
        final CurrentMeetLocationModel[] currentMeetLocationModels = new CurrentMeetLocationModel[6];
                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));
                    Connection con = DriverManager.getConnection(db);
                    Statement st = con.createStatement();

                    // Query and display results //Step 5
                    ResultSet rs = st.executeQuery("SELECT * FROM trackingdata where stoptime > 0");
                    Log.i(LOG_TAG, "*** Query results:");
                    //DataTracking dataTracking0 = null;
                    int begin = 0;
                    while (rs.next())
                    {
                        currentMeetLocationModels[begin] = new CurrentMeetLocationModel(Integer.parseInt( rs.getString( "trackableID" ) )
                                ,Double.parseDouble( rs.getString("latitude"))
                                                  ,Double.parseDouble( rs.getString("longtitude")));
                        //Log.i(LOG_TAG,Integer.toString( currentMeetLocationModels[begin].getTrackableId() ));
                        begin++;
                    }
                    Log.i(LOG_TAG, "*** query result: ");

                    // Release resources //Step 7
                    rs.close();
                    st.close();
                    con.close();

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
        return currentMeetLocationModels;
    }



    public void deleteCol ( final int ID, final String db){
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));
                    Connection con = DriverManager.getConnection(db);
                    Statement st = con.createStatement();
                    st.execute("delete from servicedata from ID = "+Integer.toString(ID));
                    Log.i(LOG_TAG, "*** Delete query : ID " +Integer.toString(ID));
                    st.close();
                    con.close();

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



}
