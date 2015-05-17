package com.example.blockphone;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.vk.sdk.VKSdk;

import db.VK_Friends;

/*
 * Created by Жамбыл on 15.05.2015.
 */

 public abstract class AppActivity extends ActionBarActivity implements LocalUI{

   public  String TITLES[] = {"Блокировка","Рейтинг","Выход"};
   public  int ICONS[] = {R.drawable.ic_action,R.drawable.ic_raiting,R.drawable.ic_quit};

   public  Typeface type_thin;
   public  Typeface type;

   public  String NAME = Account.getFullName();
   public  int POINTS = Account.getPoints();
   public  byte [] PROFILE_PHOTO = Account.getPhotoAsBytes();

   public  Toolbar toolbar;
   public  RecyclerView mRecyclerView;
   public  RecyclerView.Adapter mAdapter;
   public  RecyclerView.LayoutManager mLayoutManager;
   public  DrawerLayout Drawer;
   public  LinearLayout layoutFromRecycler;
   public  ActionBarDrawerToggle mDrawerToggle;

   public  static KeyguardManager.KeyguardLock kl;
   public  static KeyguardManager km;
   public  static boolean reenabled = false;

   /*
      Methods
    */
   Context context;
   int toolbarId;
   int recyclerViewId;
   String toolbarText;
   int drawerId;
   int layoutFromRecyclerId;
   int ActivityId;

   public AppActivity(){}
   public AppActivity(int toolbarId,String toolbarText,int recyclerViewId,
                      int drawerId,int layoutFromRecyclerId,int ActivityId){

      this.context = AppActivity.this;
      this.toolbarId = toolbarId;
      this.toolbarText = toolbarText;
      this.drawerId = drawerId;
      this.recyclerViewId = recyclerViewId;
      this.layoutFromRecyclerId = layoutFromRecyclerId;
      this.ActivityId = ActivityId;
   }
   protected void startUI(){

      if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         Window window = getWindow();
         window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
         window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
         //window.setStatusBarColor(getResources().getColor(R.color.ColorPrimary));
         window.setStatusBarColor(Color.BLACK);
      }

      km = ((KeyguardManager)getSystemService(KEYGUARD_SERVICE));
      kl = km.newKeyguardLock(getPackageName());

      type = Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Light.ttf");
      type_thin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");

      toolbar = (Toolbar) findViewById(toolbarId);
      setSupportActionBar(toolbar);
      getSupportActionBar().setTitle(toolbarText);

      mRecyclerView = (RecyclerView) findViewById(recyclerViewId);
      mRecyclerView.setHasFixedSize(true);

      mAdapter = new DrawableAdapter(TITLES,ICONS,NAME,POINTS,PROFILE_PHOTO,context);
      mRecyclerView.setAdapter(mAdapter);

      mLayoutManager = new LinearLayoutManager(context);
      mRecyclerView.setLayoutManager(mLayoutManager);

      Drawer = (DrawerLayout) findViewById(drawerId);

      mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){
         @Override
         public void onDrawerOpened(View drawerView) {super.onDrawerOpened(drawerView);}
         @Override
         public void onDrawerClosed(View drawerView) {super.onDrawerClosed(drawerView);}
      };
      Drawer.setDrawerListener(mDrawerToggle);
      mDrawerToggle.syncState();

      layoutFromRecycler = (LinearLayout)findViewById(layoutFromRecyclerId);

      final GestureDetector mGestureDetector =
              new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                 @Override public boolean onSingleTapUp(MotionEvent e){
                    return true;
                 }
              });

      mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
         @Override
         public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
            if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
               Drawer.closeDrawers();
               switch(recyclerView.getChildPosition(child))
               {
                  case 1:
                     if(ActivityId==2) finish();
                     break;
                  case 2:
                     if(ActivityId==1) startActivity(new Intent(context, Top.class));
                     break;
                  case 3:
                     new AlertDialog.Builder(context)
                             .setTitle("Выход")
                             .setMessage("Вы уверены, что хотите выйти из аккаунта?")
                             .setNegativeButton(android.R.string.no, null)
                             .setPositiveButton("Да",
                                     new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                           VKSdk.logout();
                                           VK_Friends.wipeFriendsData(context);
                                           startActivity(new Intent(context, MainActivity.class));
                                           Log.i("App", "Logining out");
                                           finish();
                                        }
                                     }).create().show();
                     break;
               }
               return true;
            }
            return false;
         }
         @Override
         public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

         }
      });
   }




}


