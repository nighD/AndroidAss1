package com.example.macintosh.assignmentt1.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.macintosh.assignmentt1.Activities.PermissionActivity;
import com.example.macintosh.assignmentt1.R;

// uses Caspar's PermissionActivity class to test runtime permission
// WRITE_EXTERNAL_STORAGE is this case
public class TestPermissionsActivity extends PermissionActivity
{
   private String LOG_TAG = this.getClass().getName();

   // this is just an arbitrary static id for the PermissionActivity
   private static final int REQUEST_WRITE_STORAGE = 1;

   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      Log.i(LOG_TAG, "onCreate()");
      super.onCreate(savedInstanceState);
      // NOTE: this layout must have a root ID named permission_view
      setContentView(R.layout.test_permissions_activity);

      // add permission helper (see JavaDoc for PermissionActivity.addPermissionHelper()
      addPermissionHelper(REQUEST_WRITE_STORAGE,
              "we need to write storage .. coz!", Manifest.permission.WRITE_EXTERNAL_STORAGE);

      View button = findViewById(R.id.next_activity_button);
      button.setOnClickListener(v->testPermissions());
   }
   private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

      @Override
      public void onReceive(Context context, Intent intent) {
         finish();
      }

   };
   @Override
   public void onDestroy() {
      super.onDestroy();
      //unregisterReceiver(mMessageReceiver);
   }

   private void testPermissions()
   {
      Log.i(LOG_TAG, "testPermissions()");
      // call superclass method to check if permission has been granted
      // we assume we would require this permisison but are just logging in this example
      if(checkPermission(REQUEST_WRITE_STORAGE))
      {
         Log.i(LOG_TAG, "permission granted to perform action");
         // TODO do something that requires the requested permission ..
      }
      finish();
   }
}