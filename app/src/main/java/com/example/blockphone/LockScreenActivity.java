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

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

//import com.todddavies.components.progressbar.ProgressWheel;

public class LockScreenActivity extends Activity {

    /** Called when the activity is first created. */
    KeyguardManager.KeyguardLock k1;
    View decorView;
    int windowwidth;
    int windowheight;
    TextView Unlock, TimeLeft, CurrentTime, dayOfTheMonth, dayOfTheWeek;
    private int longClickDuration = 500;
    private int longLockDuration = 0;
    private boolean isLongPress = false;
    private long then;

    int progress = 0;
    boolean running;
    LayoutParams layoutParams;
    ProgressWheel pw;
    WindowManager wm;
    ViewGroup mTopView;
    WindowManager.LayoutParams params;
    boolean isCanBeUnlocked = false;
    Thread spinningThread;
    String day,month;

    boolean mustSpeen = true;
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
        String lld[] = App.dropdown1value.split(" ");
        String lcd[] = App.dropdown2value.split(" ");
        longLockDuration  = Integer.valueOf(lld[0]);
        longClickDuration = Integer.valueOf(lcd[0]) * 1000;

        int dayOfTheWeekCal = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        switch (dayOfTheWeekCal){
            case Calendar.MONDAY:
                day = "понедельник";
                break;
            case Calendar.TUESDAY:
                day = "вторник";
                break;
            case Calendar.WEDNESDAY:
                day = "среда";
                break;
            case Calendar.THURSDAY:
                day = "четверг";
                break;
            case Calendar.FRIDAY:
                day = "пятница";
                break;
            case Calendar.SATURDAY:
                day = "суббота";
                break;
            case Calendar.SUNDAY:
                day = "воскресенье";
                break;
        }
        int dayOfTheMonthCal = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int monthCal = Calendar.getInstance().get(Calendar.MONTH);
        switch (monthCal){
            case Calendar.JANUARY:
                month ="января";
                break;
            case Calendar.FEBRUARY:
                month ="февраля";
                break;
            case Calendar.MARCH:
                month ="марта";
                break;
            case Calendar.APRIL:
                month ="апреля";
                break;
            case Calendar.MAY:
                month ="мая";
                break;
            case Calendar.JUNE:
                month ="июня";
                break;
            case Calendar.JULY:
                month ="июля";
                break;
            case Calendar.AUGUST:
                month ="августа";
                break;
            case Calendar.SEPTEMBER:
                month ="сентября";
                break;
            case Calendar.OCTOBER:
                month ="октября";
                break;
            case Calendar.NOVEMBER:
                month ="ноября";
                break;
            case Calendar.DECEMBER:
                month ="декабря";
                break;
        }
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
        /*Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.ColorPrimary));*/
        //window.setStatusBarColor(Color.BLACK);

        dayOfTheMonth =  (TextView) mTopView.getChildAt(5);
        dayOfTheMonth.setText(day);
        dayOfTheMonth.setTextColor(Color.WHITE);

        dayOfTheWeek =  (TextView) mTopView.getChildAt(4);
        dayOfTheWeek.setText(dayOfTheMonthCal + " "+ month);
        dayOfTheWeek.setTextColor(Color.WHITE);

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
        int myColor = getResources().getColor(R.color.ColorPrimary);
        pw.setContourColor(Color.TRANSPARENT);
        pw.setRimColor(Color.TRANSPARENT); //change
        pw.setBackgroundColor(Color.TRANSPARENT);

        spinningThread = new Thread() {
            public void run() {
                //up
                while (true) {
                    while (!isCanBeUnlocked)
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    //down

                }
            }
        };
        spinningThread.start();



        //UnlockText.set
        //UnlockText.setTextColor(Color.WHITE);

        if(getIntent () !=null&&getIntent().hasExtra (" kill")&&getIntent().getExtras().getInt("kill")==1){
            finish();
        }

        try {
            startService(new Intent(this, LockScreenService.class));

            StateListener phoneStateListener = new StateListener();
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

            //  windowwidth=getWindowManager().getDefaultDisplay().getWidth();
            //System.out.println("windowwidth"+windowwidth);
            /// windowheight=getWindowManager().getDefaultDisplay().getHeight();
            //System.out.println("windowheight"+windowheight);

            // MarginLayoutParams marginParams2 = new MarginLayoutParams(Unlock.getLayoutParams());

            // marginParams2.setMargins((windowwidth/24)*10,((windowheight/32)*8),0,0);

            //marginParams2.setMargins(((windowwidth-droid.getWidth())/2),((windowheight/32)*8),0,0);
            //RelativeLayout.LayoutParams layoutdroid = new RelativeLayout.LayoutParams(marginParams2);

            //Unlock.setLayoutParams(layoutdroid);
        }
        catch (Exception ignored){}
        Log.e("LongLockDuration",String.valueOf(longLockDuration));
            Unlock.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final boolean[] isUnlocked = {false};

                    if (longClickDuration/1000 == 1)
                        Unlock.setText("Удерживайте 1 секунду");
                    else
                        Unlock.setText("Удерживайте " + String.valueOf(longClickDuration/1000) + " секунды");

                    //DOWN
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        isCanBeUnlocked = true;

                        Thread thread1 = new Thread() {
                            public void run() {
                                progress = 0;
                                while (progress < 361) {
                                    if(!isCanBeUnlocked) break;
                                    pw.incrementProgress();
                                    progress++;
                                   // Log.e("progress",String.valueOf(progress));

                                    /*if(progress == 360){
                                        mustSpeen = false;
                                        break;
                                    }*/
                                    try {
                                        Thread.sleep(longClickDuration/ 360);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                        }}};
                        thread1.start();
                        /////////////////////////////////////////////

                        //запускает новый поток, который каждые 50 мсек  будет проверять
                        then = System.currentTimeMillis();

                        Thread thread = new Thread() {
                            public void run() {
                                while(!isUnlocked[0]) {
                                    if(!spinningThread.isAlive()) Log.e("LockScreenActivity","SpinningThread is DEAD");
                                    if ((System.currentTimeMillis() - then) > longClickDuration) {
                                        if(isCanBeUnlocked){
                                            Unlock();
                                        }
                                        isUnlocked[0] = true;
                                    }
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        thread.start();
                    //UP
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        //mustSpeen = true;
                        pw.stopSpinning();
                        isCanBeUnlocked = false;
                        Unlock.setText("Разблокировать");
                        return false;
                    }
                    return true;
                }
            });
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
                    new CountDownTimer(longLockDuration * 3600000, 1000){
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
                    Thread.sleep(1000); // Pause of 1 sec
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