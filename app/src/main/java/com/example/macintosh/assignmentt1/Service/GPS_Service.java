package com.example.macintosh.assignmentt1.Service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.example.macintosh.assignmentt1.Activities.MapsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class GPS_Service extends Service {

    private LocationListener listener;
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    MapsActivity mapsActivity;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                //get the location name from latitude and longitude
                Geocoder geocoder = new Geocoder( getApplicationContext() );
                try {
                    List<Address> addresses =
                            geocoder.getFromLocation( latitude, longitude, 1 );
                    String result = addresses.get( 0 ).getLocality() + ":";
                    result += addresses.get( 0 ).getCountryName();
                    LatLng latLng = new LatLng( latitude, longitude );

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent( "location_update" );
                i.putExtra( "coordinates", location.getLongitude() + " " + location.getLatitude() );
                sendBroadcast( i );
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
                i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity( i );
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService( Context.LOCATION_SERVICE );

        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(mapsActivity, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //locationManager.requestSing
        locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, listener );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }
}