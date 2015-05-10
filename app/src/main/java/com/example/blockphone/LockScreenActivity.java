package com.example.blockphone;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

//import com.todddavies.components.progressbar.ProgressWheel;

public class LockScreenActivity extends Activity {

    /** Called when the activity is first created. */
    KeyguardManager.KeyguardLock k1;
    View decorView;
    int windowwidth;
    int windowheight;
    TextView Unlock;
    TextView TimeLeft;
    TextView CurrentTime;
    private int longClickDuration = 500;
    private int longLockDuration = 0;
    private boolean isLongPress = false;
    int progress = 0;
    boolean running;
    LayoutParams layoutParams;
    ProgressWheel pw;
    WindowManager wm;
    ViewGroup mTopView;
    WindowManager.LayoutParams params;
    /*
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
         getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }*/
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_screen_container);
        Log.i("","Lock Screen ON");
        String lcd[] = App.dropdown1value.split(" ");
        String lld[] = App.dropdown2value.split(" ");
        longClickDuration = Integer.valueOf(lcd[0]) * 1000;
        longLockDuration  = Integer.valueOf(lld[0]);

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        Paint paint = new Paint();
        ColorFilter filter = new ColorMatrixColorFilter(cm);

// ... prepare a color filter
        filter = new PorterDuffColorFilter(Color.rgb(34, 136, 201), PorterDuff.Mode.OVERLAY);

        //////////////////////////////////////////////////////////////////////////////////
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR ,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
              | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
              | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        mTopView = (ViewGroup) getLayoutInflater().inflate(R.layout.lock_screen, null);
        getWindow().setAttributes(params);
        wm.addView(mTopView, params);

        /*
            0 - Progress Wheel
            1 - Round Text View
            2 - Upper TextView
            3 - Downer TextView  - TimeLeft
            4 - Test Button
         */

        /////////////////////////////////////////////////////////////////////////////////

        WallpaperManager myWallpaperManager = WallpaperManager
                .getInstance(getApplicationContext());

        //background
        RelativeLayout parentLayout = (RelativeLayout) mTopView.getChildAt(3).getParent();
        Drawable myDrawable = myWallpaperManager.getDrawable();
        myDrawable.setAlpha(100);
        //parentLayout.setBackground(myDrawable);
        parentLayout.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryDark));


        //nav bar color
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.ColorPrimary));
        //window.setStatusBarColor(Color.BLACK);


        TimeLeft = (TextView) mTopView.getChildAt(3);
        TimeLeft.setTextColor(Color.WHITE);

        CurrentTime = (TextView) mTopView.getChildAt(2);
        CurrentTime.setTextColor(Color.WHITE);

        Unlock = (TextView) mTopView.getChildAt(1);
        Unlock.setText("Разблокировать");


        Thread myThread = null;
        Runnable myRunnableThread = new CountDownRunner();
        myThread= new Thread(myRunnableThread);
        myThread.start();

        pw = (ProgressWheel) mTopView.getChildAt(0);
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
                        Thread.sleep(longClickDuration/360);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                running = false;
            }
        };
        final Thread s = new Thread(r);
        //s.start();


        //UnlockText.set
        //UnlockText.setTextColor(Color.WHITE);

        if(getIntent () !=null&&getIntent().hasExtra (" kill")&&getIntent().getExtras().getInt("kill")==1){
            finish();
        }

        try{
            startService(new Intent(this,LockScreenService.class));

            StateListener phoneStateListener = new StateListener();
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);

          //  windowwidth=getWindowManager().getDefaultDisplay().getWidth();
            //System.out.println("windowwidth"+windowwidth);
           /// windowheight=getWindowManager().getDefaultDisplay().getHeight();
            //System.out.println("windowheight"+windowheight);

           // MarginLayoutParams marginParams2 = new MarginLayoutParams(Unlock.getLayoutParams());

           // marginParams2.setMargins((windowwidth/24)*10,((windowheight/32)*8),0,0);

            //marginParams2.setMargins(((windowwidth-droid.getWidth())/2),((windowheight/32)*8),0,0);
            //RelativeLayout.LayoutParams layoutdroid = new RelativeLayout.LayoutParams(marginParams2);

            //Unlock.setLayoutParams(layoutdroid);

            Unlock.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(final View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //s.start();
                        //s.start();
                        if(longLockDuration != 1)
                            Unlock.setText("Удерживайте " + String.valueOf(longLockDuration) + " секунды");
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
                        //pw.
                        //s.stop();
                        //s.interrupt();
                        //pw.resetCount();
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

    //new
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
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
                    wm.updateViewLayout(mTopView,params);
                    CurrentTime.setText(curTime);
                }catch (Exception e) {}
            }
        });
    }
    private void Unlock(){
        progress = 0;
        if(mTopView!=null && wm!=null & mTopView.isShown()) {
            wm.removeView(mTopView);
        }
        Log.e("","Lock Screen OFF");
        App.reenable();
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
    }/*
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
*/
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