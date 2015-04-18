package com.example.blockphone;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

//import com.todddavies.components.progressbar.ProgressWheel;

public class LockScreenActivity extends Activity {

    /** Called when the activity is first created. */
    KeyguardManager.KeyguardLock k1;

    int windowwidth;
    int windowheight;
    TextView Unlock;
    TextView TimeLeft;
    TextView CurrentTime;
    private int longClickDuration = 500;
    private double longLockDuration = 0;
    private boolean isLongPress = false;
    int progress = 0;
    boolean running;
    LayoutParams layoutParams;
    ProgressWheel pw;

    /*
    @Override
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }/*
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
         getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }*/
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_screen);
        Log.i("","Lock Screen ON");
        longClickDuration = (int) App.dropdown2double * 1000;
        longLockDuration  = App.dropdown1double;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            |WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        //Время
        TimeLeft = (TextView)findViewById(R.id.TimeLeft);
        TimeLeft.setTextColor(Color.WHITE);

        CurrentTime = (TextView)findViewById(R.id.CurrentTime);
        CurrentTime.setTextColor(Color.WHITE);

        Unlock = (TextView)findViewById(R.id.Unlock);
        Unlock.setText("Разблокировать");


        Thread myThread = null;
        Runnable myRunnableThread = new CountDownRunner();
        myThread= new Thread(myRunnableThread);
        myThread.start();


        pw = (ProgressWheel) findViewById(R.id.pw_spinner);
        progress = 0;
        //pw.setW
        //pw.setContourSize(0);
        pw.setContourColor(Color.TRANSPARENT);
        pw.setRimColor(Color.TRANSPARENT);
        pw.setBackgroundColor(Color.TRANSPARENT);

        final Runnable r = new Runnable() {
            public void run() {
                running = true;
                while(progress<360) {
                    pw.incrementProgress();
                    progress++;
                    try {
                        Thread.sleep((int)longLockDuration * 3600000 / 360 );
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                running = false;
            }
        };
        Thread s = new Thread(r);
        s.start();

        //UnlockText.set
        //UnlockText.setTextColor(Color.WHITE);

        if(getIntent () !=null&&getIntent().hasExtra (" kill")&&getIntent().getExtras().getInt("kill")==1){
            //Toast.makeText(this, "" + "kill activityy", Toast.LENGTH_SHORT).show();
            finish();
        }

        try{
            startService(new Intent(this,LockScreenService.class));

          /*KeyguardManager km =(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
            k1 = km.newKeyguardLock("IN");
            k1.disableKeyguard();*/
            StateListener phoneStateListener = new StateListener();
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);

            windowwidth=getWindowManager().getDefaultDisplay().getWidth();
            //System.out.println("windowwidth"+windowwidth);
            windowheight=getWindowManager().getDefaultDisplay().getHeight();
            //System.out.println("windowheight"+windowheight);

            MarginLayoutParams marginParams2 = new MarginLayoutParams(Unlock.getLayoutParams());

            marginParams2.setMargins((windowwidth/24)*10,((windowheight/32)*8),0,0);

            //marginParams2.setMargins(((windowwidth-droid.getWidth())/2),((windowheight/32)*8),0,0);
            RelativeLayout.LayoutParams layoutdroid = new RelativeLayout.LayoutParams(marginParams2);

            //Unlock.setLayoutParams(layoutdroid);

            Unlock.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(final View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if((int)App.dropdown2double != 1)
                            Unlock.setText("Удерживайте " + String.valueOf((int)App.dropdown2double) + " секунды");
                        else
                            Unlock.setText("Удерживайте 1 секунду");

                        isLongPress = true;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isLongPress) {
                                    //TODO вибрацию сделать
                                    //Vibrator vibrator = (Vibrator) LockScreen.this.getSystemService(Context.VIBRATOR_SERVICE);
                                    //vibrator.vibrate(100);
                                     Unlock();
                                }
                            }
                        }, longClickDuration);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        //longClickDuration = (int)App.dropdown2double*1000;
                        Unlock.setText("Разблокировать");
                        isLongPress = false;
                    }
                    return true;
                }
            });
        }catch (Exception e) {
            // TODO: handle exception
        }
    }
    public void setTime() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    String curTime = null;
                    if(minutes < 10){
                        String thisminutes = "0" + String.valueOf(minutes);
                        curTime = String.valueOf(hours) + ":" + thisminutes;}
                    else
                        curTime = hours + ":" + minutes;

                    CurrentTime.setText(curTime);
                }catch (Exception e) {}
            }
        });
    }
    private void Unlock(){
        progress = 0;
        Log.i("","Lock Screen OFF");
        LockScreenService.isMustBeLocked = false;
        finish();
    }

    public void setLeftTime(){
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    new CountDownTimer((int)longLockDuration * 3600000, 1000){
                        public void onTick(long millisUntilFinished) {
                            long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                            if(hours!=0)
                                TimeLeft.setText("" + String.format("%d часов,  %d минут",
                                        hours,minutes)+" осталось");
                            else if(minutes!=0)
                                TimeLeft.setText("" + String.format("%d минут",
                                        minutes)+" осталось");
                            else
                                TimeLeft.setText("" + String.format("%d секунд",
                                        seconds)+" осталось");
                      }
                        public void onFinish() {
                            //TODO зачисление очков
                            Unlock();
                        }
                    }.start();
                }catch (Exception e) {}
            }
        });
    }


    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            setLeftTime();
            while(!Thread.currentThread().isInterrupted()){
                try {
                    setTime();
                    Thread.sleep(1000 * 5); // Pause of 5 sec
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }


    @Override
    protected void onUserLeaveHint() {
        Log.e("","onUserLeaveHint");

        super.onUserLeaveHint();
    }


    class StateListener extends PhoneStateListener{//TODO handle
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    System.out.println("call Activity off hook");
                    finish();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }
        }
    };
    public void onSlideTouch( View view, MotionEvent event )
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int x_cord = (int)event.getRawX();
                int y_cord = (int)event.getRawY();

                if(x_cord>windowwidth){x_cord=windowwidth;}
                if(y_cord>windowheight){y_cord=windowheight;}

                layoutParams.leftMargin = x_cord -25;
                layoutParams.topMargin = y_cord - 75;

                view.setLayoutParams(layoutParams);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // Don't allow back to dismiss.
        return;
    }

    //only used in lockdown mode
    @Override
    protected void onPause() {
        super.onPause();

        // Don't hang around.
        // finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Don't hang around.
        // finish(
    }

    /*
    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)||(keyCode == KeyEvent.KEYCODE_POWER)||(keyCode == KeyEvent.KEYCODE_VOLUME_UP)||(keyCode == KeyEvent.KEYCODE_CAMERA)) {
            //this is where I can do my stuff
            return true; //because I handled the event
        }
        if((keyCode == KeyEvent.KEYCODE_HOME)){
            Intent intent = new Intent(this, LoActivityckScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Log.e("","HOME");
            return true;
        }
        return false;    }
*/
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER ||(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)||(event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
            //Intent i = new Intent(this, NewActivity.class);
            //startActivity(i);
            return false;
        }
        if((event.getKeyCode() == KeyEvent.KEYCODE_HOME)){  //WHY ALWAYS FUCKING FALSE
            Log.e("","HOME");
            System.out.println("alokkkkkkkkkkkkkkkkk");
            return true;
        }
        return false;
    }

    public void onDestroy(){
        // k1.reenableKeyguard();

        super.onDestroy();
    }

}