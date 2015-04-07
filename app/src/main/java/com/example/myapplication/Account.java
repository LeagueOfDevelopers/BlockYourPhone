package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



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

    public static Bitmap convertUrlToImage(String photo){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(photo);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assert url != null;
        try {
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
       return image;
    }

    public static String getName(){return  FirstName +" "+ LastName;}
    public static int getPosition(){return PositionInTop;}
    public static int getPoints(){return Points;}
    public static byte[] getPhotoAsBytes(){return PhotoAsBytes;}
    public static void setPoints(int p){Points = p;}
    public static void addPoints(int p){Points =+ p;}

}