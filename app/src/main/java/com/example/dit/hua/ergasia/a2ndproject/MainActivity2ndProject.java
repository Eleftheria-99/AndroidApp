package com.example.dit.hua.ergasia.a2ndproject;
import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

//THE APP RUNS SURELY IN API > 26

public class MainActivity2ndProject extends Activity {

    private android.location.LocationManager locationManager= null;
    private MyBroadcastReceiver myBroadcastReceiver = null;
    private  String LOG_TAG = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LOG_TAG = this.getClass().getSimpleName();
        Log.i(LOG_TAG, "In onCreate");
        //register broadcast receiver
        myBroadcastReceiver = new MyBroadcastReceiver();
        broadcastIntent();

    }

    public void broadcastIntent() {
        Log.i("In method: ", "broadcastIntent");
        registerReceiver(myBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));  //register the receiver, aside the filters in the manifest, declaring the intent filters on runtime is needed for apis> 26
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {  //Callback for the result from requesting permissions.
        Log.i(LOG_TAG, "In onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==7){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i(LOG_TAG, "In onRequestPermissionsResult: permission granted");
                Toast.makeText(this, "well done", Toast.LENGTH_LONG).show();

            }
        }
    }


    public static String getConnectivityStatusString(Context context) { //this method checks if the device is connected to wifi or mobile data
        Log.i("In method", "getConnectivityStatusString");

        String status = null;
        ConnectivityManager cm = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {                         //check if the network that the device is connected to , is wifi
                status = "Wifi enabled";
                return status;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {                //check if the network that the device is connected to , is mobile
                status = "Mobile data enabled";
                return status;
            }
        } else {
            status = "No internet is available";
            return status;
        }
        return status;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        if(myBroadcastReceiver != null) { //if the receiver exists
            unregisterReceiver(myBroadcastReceiver);   //unregister the receiver
        }
    }

}