package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.List;

public final class Account {
    public static String access_token;
    public static long user_id;

    private static int PositionInTop;
    private static String Name = "Жамбыл Ермагамбет";
    private static int Points = 6420; //TODO: запись в бд всех очков удаленно

    public static void save(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putString("access_token", access_token);
        editor.putLong("user_id", user_id);
        editor.commit();
    }

    public static void restore(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if( prefs != null) {
            access_token = prefs.getString("access_token", null);
            user_id = prefs.getLong("user_id", 0);
        }

    }

    public static String getName(){return Name;}
    public static int getPosition(){return PositionInTop;}
    public static int getPoints(){return Points;}
    public static void setPoints(int p){Points = p;}
    public static void addPoints(int p){Points =+ p;}

}