package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.List;

public class Account {
    public String access_token;
    public long user_id;

    public int getPositionInTop;
    public String Name;

    public void save(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor=prefs.edit();
        editor.putString("access_token", access_token);
        editor.putLong("user_id", user_id);
        editor.commit();
    }

    public void restore(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        access_token=prefs.getString("access_token", null);
        user_id=prefs.getLong("user_id", 0);
    }
}