package com.mytest.myapplication.app;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    Button button;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*final LocationManager mlocManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final LocationListener mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        */
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                gps = new GPSTracker(MainActivity.this);

                if(gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(getApplicationContext(), "Location is - \nLat: " + latitude
                    + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
                }
                else{
                    gps.showSettingsAlert();
                }
            }
        });

    }

}

