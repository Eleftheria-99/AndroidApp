package com.example.dit.hua.ergasia.a2ndproject;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.os.Process;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class Service2ndProject extends Service {
    //when online, call service from broadcast receiver, to find the location
    //find again location, every 5 min or when the location of the user changes 10 meters

    //Unlike activities, services lack a visual user interface.
    // They're used to implement long-running background operations or a rich communications API that can be called by other applications.

    //This is the base class for all services.
    // When you extend this class, it's important to create a new thread in which the service can complete all of its work;
    // the service uses your application's main thread by default, which can slow the performance of any activity that your application is running.
   // public void Service2ndProject(){}

    //FIELDS OF THE DB_TABLE
    //public static final String _ID = "id";  id id autoincrement
    public static String UNIX_TIMESTAMP = "_UNIX_TIMESTAMP";
    public static String LATITUDE = "_LATITUDE";
    public static String LONGITUDE  ="_LONGTITUDE";

    private String LOG_TAG = this.getClass().getSimpleName();
    DataContract2ndProject datacontractobject = new DataContract2ndProject();

    private ServiceHandler serviceHandler = null;

    protected Uri res = null;

    // Handler that receives messages from the thread
    final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message msg) {
            // Here we do some work.
                Log.i(LOG_TAG, "check sdk ok and begin finding the location");
                try {
                    find_location();
                }catch(Exception e){
                    e.getCause();
                    e.getLocalizedMessage();
                    e.getMessage();
                    e.getStackTrace();
                }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        Log.i(LOG_TAG, "In onCreate");


        if (Build.VERSION.SDK_INT >= 26) { //if api>26, show notification for service
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }

        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_FOREGROUND);
        thread.start();
        Log.i(LOG_TAG, "thread started");
        // Get the HandlerThread's Looper and use it for our Handler
        Looper serviceLooper = thread.getLooper();
        serviceHandler = new Service2ndProject.ServiceHandler(serviceLooper);
    }


    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //The system invokes this method by calling startService() when another component (such as an activity) requests that the service be started.
        // When this method executes, the service is started and can run in the background indefinitely.
        Log.i(LOG_TAG, "In onStartCommand");
        super.onStartCommand(intent, flags, startId);
        Log.i(LOG_TAG, "service started");

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        return START_STICKY;        // If we get killed, after returning from here, restart  : START_STICKY
    }


    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "service onDestroy");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }


    protected void find_location(){  //this method finds the location

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,   //checks for permission
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "getLocation: stopping the location service.");
            stopSelf(); //if there is no permission in manifest for fine location, stop service
            return;
        }
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
        //provider for location , when the location has changed at lest 10 meters


        @Override
        public void onLocationChanged(Location location) { //if ok, changes location
            Toast.makeText(getApplicationContext(), "Longitude: "+location.getLongitude() + " Latitude: " + location.getLatitude(), Toast.LENGTH_LONG).show();
            Log.i(TAG, "OBJECT DATA_CONTRACT_OBJECT Longitude:"+location.getLongitude() + " Latitude:" + location.getLatitude());
            save_data_to_database( String.valueOf(location.getLatitude() ), String.valueOf(location.getLongitude()));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {//when the  status of location manager changes

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
       }, Looper.myLooper());  // Looper.myLooper tells this to repeat forever until thread is destroyed


    }



    protected void save_data_to_database(String lat,String lon ){       //saving to database the data that the user wrote

        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://com.example.dit.hua.android.a2ndProject.contentproviderfor2ndproject/contacts");
        ContentValues contentValues = new ContentValues();
        String unix_timestamp = null;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            unix_timestamp  = dateFormat.format(new Date()); // Find today's date
        } catch (Exception e) {
            e.printStackTrace();
            //return ;
        }

        try {
            datacontractobject = new DataContract2ndProject(unix_timestamp,lat,lon);                 //creating an object every time that data needs to be saved
        } catch (Exception e) {
            Log.e(TAG, "OBJECT DATA_CONTRACT_OBJECT NOT CREATED");
            e.getStackTrace();
        }
        Log.i(TAG, "OBJECT DATA_CONTRACT_OBJECT : unix_time_stamp:"+datacontractobject.getUnix_timestamp()+" lat:"+datacontractobject.getLat()+"long:"+datacontractobject.getLon());
        try {
            contentValues.put(UNIX_TIMESTAMP, datacontractobject.getUnix_timestamp());  //DBHelper2nProject.UNIX_TIMESTAMP
            contentValues.put(LATITUDE,  datacontractobject.getLat());                  //DBHelper2nProject.
            contentValues.put(LONGITUDE, datacontractobject.getLon());                  //DBHelper2nProject.LONGITUDE
        }catch(Exception e ){
            Log.e(TAG, "DBHELPER:ERROR-> CREATE ENTRY  ", e);
        }

        res = resolver.insert(uri,contentValues);   //returns uri

        Toast.makeText(getApplicationContext(), "Your data were saved in database!" +res.toString(), Toast.LENGTH_SHORT).show();      //notify the user

    }

}
