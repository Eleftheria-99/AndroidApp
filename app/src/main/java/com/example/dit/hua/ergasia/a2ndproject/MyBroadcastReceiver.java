package com.example.dit.hua.ergasia.a2ndproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


public class MyBroadcastReceiver extends BroadcastReceiver {

    MainActivity2ndProject mainActivity2ndProject = null;
    Service2ndProject service2ndProject = null;
    private String LOG_TAG = null;
    private String status = null;
    private String package_name="com.example.dit.hua.ergasia.a2ndproject";

    @Override
    public void onReceive(Context context, Intent intent) {
        LOG_TAG = this.getClass().getSimpleName();
        Log.i(LOG_TAG, "BroadcastReceiver: In onCreate");

        //declare service class
        intent = new Intent(context, Service2ndProject.class);
        mainActivity2ndProject = new MainActivity2ndProject();
        service2ndProject = new Service2ndProject();

        status = mainActivity2ndProject.getConnectivityStatusString(context);
        Log.i( LOG_TAG, "broadcast status "+status);

            if(status.isEmpty()) {                                                                  //if the status is empty , it means that there is no internet connection
                status="No Internet Connection";
                Toast.makeText(context, status, Toast.LENGTH_LONG).show();

                //stop the service because there is no internet
                context.stopService(intent);
            }

        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
        if(status.contentEquals("No internet is available")){//if the method returns this string, means that there is no internet connection
            //stop the service because there is no internet
              context.stopService(intent);
        }

        if(status.contentEquals("Wifi enabled") || status.contentEquals("Mobile data enabled")  ) { //if the internet returns one of those strings, it means that there is internet connection

            //BEGIN SERVICE, explicit way, because there is connection to the internet
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            }else{
                context.startService(intent);}

        }
    }





}
