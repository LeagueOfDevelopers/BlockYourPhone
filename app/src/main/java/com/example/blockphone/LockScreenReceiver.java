package com.example.blockphone;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LockScreenReceiver extends BroadcastReceiver  {
    public static boolean unlocked = false;

    @Override
    public void onReceive(Context context, Intent intent) {
       if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) ) { // TODO Заблокировать

/*
            wasScreenOn=false;
            Intent intent11 = new Intent(context,LockScreenActivity.class);
            intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent11);
*/
           } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
           if(!LockScreenService.isMustBeLocked){

               Log.i("Receiver", "Not must be locked");
               try {
                   this.abortBroadcast();
               } catch (Throwable throwable) {
                   throwable.printStackTrace();
               }
           }
           else if(intent.getAction().equals(Intent.ACTION_ALL_APPS)){
               Log.e("","asdasdsasad");
           }


            //wasScreenOn=true;
            /*Intent intent11 = new Intent(context,LockScreenActivity.class);
            intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(intent11);*/
            //Toast.makeText(context, "" + "start activity", Toast.LENGTH_SHORT).show();

            // wasScreenOn = true;
        }
        else if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
        }

    }


}