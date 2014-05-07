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
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    ImageButton cookie;
    Button retry;
    TextView timer;
    GPSTracker gps;
    MyCountDown myCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*final LocationManager mlocManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final LocationListener mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        */
        cookie = (ImageButton) findViewById(R.id.cookie);
        retry = (Button) findViewById(R.id.retry);
        timer = (TextView) findViewById(R.id.timeDisplay);
        myCountDown = new MyCountDown(30000, 1000);

        retry.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){

                myCountDown.start();
                game();
                gps = new GPSTracker(MainActivity.this);

            }
        });

    }

    public class MyCountDown extends CountDownTimer{
        public MyCountDown(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish(){
            if(gps.canGetLocation()){
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                Toast.makeText(getApplicationContext(), "Location is - \nLat: " + latitude
                        + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
            }
            else{
                gps.showSettingsAlert();
            }
            timer.setText("Time: FINISHED");
        }

        @Override
        public void onTick(long millisUntilFinished){
            timer.setText("Time: " + millisUntilFinished/1000);
        }
    }
    public void game(){


        return;
    }

}

