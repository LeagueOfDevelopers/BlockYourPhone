package com.example.blockphone;


import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import javax.xml.transform.Result;

public class LockScreenService extends Service{
    BroadcastReceiver mReceiver;
    public static volatile boolean isMustBeLocked;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        KeyguardManager.KeyguardLock k1;

        if(isMustBeLocked) {

            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);

            mReceiver = new LockScreenReceiver();
            registerReceiver(mReceiver, filter);

            super.onCreate();

        }
        else{

            Log.e("LockScreenService","TEST");
        }

    }
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub

        super.onStart(intent, startId);
    }
    /*@Override
    public void onPause(){

    }*/
    public void destroy(){
        if(mReceiver!=null) {
            unregisterReceiver(mReceiver);
            Log.e("","TEST destroy3");
        }
        Log.e("","TEST destroy2");
    }
    @Override
    public void onDestroy() {
        if(mReceiver!=null) {
            unregisterReceiver(mReceiver);
            Log.e("","TEST destroy");
        }Log.e("","TEST destroying");
        super.onDestroy();
    }
}