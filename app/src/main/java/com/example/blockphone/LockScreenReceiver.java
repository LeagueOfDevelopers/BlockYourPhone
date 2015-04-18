package com.example.blockphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LockScreenReceiver extends BroadcastReceiver  {
    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
       if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) ) { // TODO Заблокировать
            //Toast.makeText(context, "" + "screeen off", Toast.LENGTH_SHORT).show();
           /* if(!LockScreenService.isMustBeLocked){
                    Log.i("Receiver","Not must be locked");
                try {
                    LockScreenReceiver.this.wait(); // ERROR
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }*/

/*
            wasScreenOn=false;
            Intent intent11 = new Intent(context,LockScreenActivity.class);
            intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent11);
*/
           } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            wasScreenOn=true;
            Intent intent11 = new Intent(context,LockScreenActivity.class);
            intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(intent11);
            //Toast.makeText(context, "" + "start activity", Toast.LENGTH_SHORT).show();

            // wasScreenOn = true;
        }
        else if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
        }

    }


}