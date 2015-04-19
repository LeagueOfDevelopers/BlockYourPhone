package com.example.blockphone;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;


public class Account{
    //public static String access_token;
   // public static long Id;

    public static int PositionInTop;
    public static String FirstName;
    public static String LastName;
    public static String vk_id;
    public static byte[] PhotoAsBytes;
    public static int Points = 6420; //TODO: запись в бд всех очков удаленно

  public static void setAccountData(String _fn, String _lm, String _vi, String enc) {
          Log.e("","Setting Account data");
          FirstName = _fn;
          LastName = _lm;
          vk_id = _vi;
          String PhotoEncoded = enc;
          if(PhotoEncoded !=null){
            byte[] b = PhotoEncoded.getBytes();
            PhotoAsBytes = Base64.decode(b,Base64.DEFAULT);}

          Log.e("FName",FirstName);
          Log.e("LName",LastName);


      }
  }
/*
    public static String getName(){return  FirstName +" "+ LastName;}
    public static int getPosition(){return PositionInTop;}
    public static int getPoints(){return Points;}
    public static byte[] getPhotoAsBytes(){return PhotoAsBytes;}
    public static void setPoints(int p){Points = p;}
    public static void addPoints(int p){Points =+ p;}
*/
