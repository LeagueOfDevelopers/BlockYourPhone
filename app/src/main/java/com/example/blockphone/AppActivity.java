package com.example.blockphone;

import android.app.KeyguardManager;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.blockphone.Account;
import com.example.blockphone.R;

/*
 * Created by Жамбыл on 15.05.2015.
 */

abstract class AppActivity extends ActionBarActivity {

   //public  String TITLES[] = {"Блокировка","Рейтинг","Выход"};
   public  int ICONS[] = {R.drawable.ic_action,R.drawable.ic_raiting,R.drawable.ic_quit};

   public  Typeface type_thin;
   public  Typeface type;

   public  String NAME = Account.getFullName();
   public  int POINTS = Account.getPoints();
   public  byte [] PROFILE_PHOTO = Account.getPhotoAsBytes();

   public   Toolbar toolbar;
   public  RecyclerView mRecyclerView;
   public  RecyclerView.Adapter mAdapter;
   public  RecyclerView.LayoutManager mLayoutManager;
   public  DrawerLayout Drawer;
   public  LinearLayout layoutFromRecycler;
   public  ActionBarDrawerToggle mDrawerToggle;

   public  static KeyguardManager.KeyguardLock kl;
   public  static KeyguardManager km;
   public  static boolean reenabled = false;

}


