package com.example.blockphone;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;


public class Account{
    //public static String access_token;
   // public static long Id;

    private static int PositionInTop;
    private static String FirstName;
    private static String LastName;
    private static byte[] PhotoAsBytes;
    private static int Points = 6420; //TODO: запись в бд всех очков удаленно

  public static void setAccountData(Context c) {
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
      if( prefs != null ) {
         // if(!buf.equals(null) || FirstName != buf){
              FirstName = prefs.getString("AccountFirstName", null);
              LastName = prefs.getString("AccountLastName", null);
              //Id = prefs.getLong("AccountId", 0);
              String PhotoEncoded = prefs.getString("AccountPhoto",null);

              byte[] b = PhotoEncoded.getBytes();
              PhotoAsBytes = Base64.decode(b,Base64.DEFAULT);
      }
  }

    public static String getName(){return  FirstName +" "+ LastName;}
    public static int getPosition(){return PositionInTop;}
    public static int getPoints(){return Points;}
    public static byte[] getPhotoAsBytes(){return PhotoAsBytes;}
    public static void setPoints(int p){Points = p;}
    public static void addPoints(int p){Points =+ p;}

}