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
import android.os.StrictMode;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.*;
import java.util.Enumeration;

import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    ImageButton cookie;
    Button retry;
    TextView timer;
    GPSTracker gps;
    MyCountDown myCountDown;
    TextView myScore;

    int score;
    int timerIsRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = 0;
        timerIsRunning = 0;
        /*final LocationManager mlocManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final LocationListener mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        */
        cookie = (ImageButton) findViewById(R.id.cookie);
        retry = (Button) findViewById(R.id.retry);
        timer = (TextView) findViewById(R.id.timeDisplay);
        myScore = (TextView) findViewById(R.id.score);
        gps = new GPSTracker(MainActivity.this);
        myCountDown = new MyCountDown(30000, 1000);

        cookie.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                score++;
                myScore.setText("Score: " + score);
            }
        });
        retry.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){

                game();



            }
        });

    }

    public class MyCountDown extends CountDownTimer{
        public MyCountDown(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish(){
            cookie.setVisibility(View.INVISIBLE);
            timer.setText("Time: FINISHED");

            // Show score at location
            double latitude = 0.0f;
            double longitude = 0.0f;
            if(gps.canGetLocation()){
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                Toast.makeText(getApplicationContext(), "Score is - \n" + score + "\nLocation is - \nLat: " + latitude
                        + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
            }
            else{
                gps.showSettingsAlert();
            }

            // build MAC address
            // referenced from http://www.mkyong.com/java/how-to-get-mac-address-in-java/
            if (android.os.Build.VERSION.SDK_INT > 9) {
                // allow for networking on main thread
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            StringBuilder sbMAC = new StringBuilder();
            try {
                InetAddress ip = InetAddress.getLocalHost();
                Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
                while(networks.hasMoreElements()) {
                    if(sbMAC.length() > 0) break;
                    NetworkInterface network = networks.nextElement();
                    byte[] mac = network.getHardwareAddress();
                    if(mac == null) continue;
                    for (int i = 0; i < mac.length; i++) {
                        sbMAC.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e){
                e.printStackTrace();
            }

            /* Build message to server in the form
             * "SAVE;*user*;*password*;*score*;*MAC*;*latitude*;*longitude"
             * "GET;*user*;*password*;*MAC*
             */
            try {
                // Open socket to connect to the server; first argument is the host, second argument is the port
                String host = "sslab01.cs.purdue.edu";
                Socket serverCon = new Socket(host, 3001);

                StringBuilder sbServerUpdate = new StringBuilder();
                StringBuilder sbServerGet = new StringBuilder();

                //SAVE
                sbServerUpdate.append("SAVE;user;password;");
                sbServerUpdate.append(Integer.toString(score)+";");
                sbServerUpdate.append(sbMAC.toString()+";");
                sbServerUpdate.append(Long.toString((long)latitude)+";");
                sbServerUpdate.append(Long.toString((long)longitude));
                //GET
                sbServerGet.append("GET;user;password;");
                sbServerGet.append(sbMAC.toString());

                // Open a PrintWriter to be able to write (string) messages to the server
                java.io.PrintWriter writer = new java.io.PrintWriter(serverCon.getOutputStream(), true);

                // Write the command to the server's input pipe (server uses readLine, so use writer.println())
                writer.println(sbServerUpdate.toString());

                // Send the data to the server
                writer.flush();

                // Close the connection to the server
                serverCon.close();
            } catch (Exception e) {}
        }

        @Override
        public void onTick(long millisUntilFinished){
            timerIsRunning = (int)millisUntilFinished/1000;
            timer.setText("Time: " + millisUntilFinished/1000);
        }

    }
    public void game(){

        score = 0;
        myScore.setText("Score: " + score);
        cookie.setVisibility(View.VISIBLE);
        myCountDown.start();


        return;
    }

}

