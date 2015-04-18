package com.example.blockphone;


import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

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

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        if(isMustBeLocked) {
            KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            k1 = km.newKeyguardLock("IN");
            k1.disableKeyguard();

            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);

            mReceiver = new LockScreenReceiver();
            registerReceiver(mReceiver, filter);
            super.onCreate();

        }
        else{
            Log.e("","TEST");
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
        }
        super.onDestroy();
    }
}