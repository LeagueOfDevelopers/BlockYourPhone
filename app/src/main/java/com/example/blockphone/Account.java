package com.example.blockphone;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import db.DB_read_all;
import db.DB_update_by_id;


public class Account {

    private static String FirstName;
    private static String LastName;
    private static String VkId;
    private static byte[] PhotoAsBytes;
    private static String PhotoUrl;
    private static int Points = -1;


    public static void setAccountData(Context context, String _fn, String _lm, String _vi, String enc, String _pu) {

        FirstName = _fn;
        LastName = _lm;
        VkId = _vi;
        PhotoUrl = _pu;

        if (enc != null) {
            byte[] b = enc.getBytes();
            PhotoAsBytes = Base64.decode(b, Base64.DEFAULT);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        if (!FirstName.equals(null) & !LastName.equals(null) & !VkId.equals(null) & PhotoAsBytes != null) {
            editor.putString("AccountFirstName", FirstName);
            editor.putString("AccountLastName", LastName);
            editor.putString("AccountId", VkId);
            editor.putString("AccountPhoto", Base64.encodeToString(PhotoAsBytes, Base64.DEFAULT));
            editor.apply();

            Log.e("FName", FirstName);
            Log.e("LName", LastName);
           // Log.e("Account Points is", String.valueOf(DB_read_all.GetPoints(VkId)));

            //new DB_read_by_id(VkId,context,-1).execute();
        }
    }

    public static void restore(Context context,boolean safe) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs != null) {
            FirstName = prefs.getString("AccountFirstName", null);
            LastName = prefs.getString("AccountLastName", null);
            VkId = prefs.getString("AccountId", null);
            Points = prefs.getInt("AccountPoints", 0);
            if(!safe)
                PhotoAsBytes = Base64.decode(prefs.getString("AccountPhoto",null).getBytes(), Base64.DEFAULT);

        }
    }
    public static String getVkId() {
        return VkId;
    }
    public static String getPhotoUrl() {
        return PhotoUrl;
    }
    public static String getFirstName() {
        return FirstName;
    }
    public static String getLastName() {
        return LastName;
    }
    public static String getFullName() {
        return FirstName + " " + LastName;
    }

    public static int getPoints() {
        return Points;
    }

    public static byte[] getPhotoAsBytes() {
        return PhotoAsBytes;
    }

    public static void setPoints(Context context, int p,boolean needToBeUpdate) {
        if(p>=0)
        {
            Points = p;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("AccountPoints",Points);
            editor.apply();
            if(needToBeUpdate)
                new DB_update_by_id(context,String.valueOf(Points),Account.VkId).execute();
        }
    }

    public static void addPoints(Context context, int p) {
        if(p>=0)
        {
            Points =+ p;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("AccountPoints",Points);
            editor.apply();
            new DB_update_by_id(context,"2000",Account.VkId).execute();
        }
    }


}