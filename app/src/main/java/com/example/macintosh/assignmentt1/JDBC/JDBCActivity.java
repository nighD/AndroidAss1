package com.example.macintosh.assignmentt1.JDBC;



import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.example.macintosh.assignmentt1.ModelClass.DataTrackingModel;
import com.example.macintosh.assignmentt1.ModelClass.TrackingService;
import com.example.macintosh.assignmentt1.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class JDBCActivity extends Activity
{
    private String LOG_TAG = this.getClass().getName();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // DB operations should go in separate Thread
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                // create database /data/data/com.example.macintosh.assignmentt1/databases/test.db
                String db = "jdbc:sqldroid:" + getDatabasePath("ass1.db").getAbsolutePath();

                try
                {
                    Class.forName("org.sqldroid.SQLDroidDriver");
                    Log.i(LOG_TAG, String.format("opening: %s", db));

                    Connection con = DriverManager.getConnection(db);
                    Statement st = con.createStatement();
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

                    // Add records to employee

                    SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
                    try (Scanner scanner = new Scanner(getApplicationContext().getResources().openRawResource( R.raw.tracking_data)))
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
//                    st.executeUpdate("INSERT INTO employee VALUES ('133','Joe')");
//                    st.executeUpdate("INSERT INTO employee VALUES ('112233','John')");
//                    st.executeUpdate("INSERT INTO employee VALUES ('445566','Mary')");
//                    st.executeUpdate("INSERT INTO employee VALUES ('199','Jack')");
//                    Log.i(LOG_TAG, "*** Inserted records");
//
//                    // Query and display results //Step 5
                    ResultSet rs = st.executeQuery("SELECT * FROM trackingdata");
                    Log.i(LOG_TAG, "*** Query results:");
                    while (rs.next())
                    {
                        Log.i(LOG_TAG, "Date " + rs.getString(1) + ", ");
                        Log.i(LOG_TAG, "TIME " + rs.getString(2));
                    }

                    // Delete table: employee //Step 6
                    st.executeUpdate("Drop table trackingdata");
                    Log.i(LOG_TAG, "*** Deleted table: trackingdata");

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

    // this is a workaround for SQLDroid cannot create in database path
    // no longer needed on API 27 AVD s2 2018
//   private void createEmptyDBWithAndroid()
//   {
//      File file = getDatabasePath("test.db");
//      String db = "jdbc:sqldroid:" + file;
//      if (!file.getParentFile().exists())
//         file.getParentFile().mkdirs();
//      SQLiteDatabase dbase = SQLiteDatabase.openOrCreateDatabase(file, null);
//      dbase.close();
//   }
}
