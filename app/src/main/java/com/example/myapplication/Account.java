package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.perm.kate.api.User;

import java.util.List;

public class Account extends User {
    public static String access_token;
    public static long user_id;
    private static Account account = new Account();
    private static Vk vk = new Vk();
    private static int PositionInTop;
    private static String Name = "Жамбыл Ермагамбет";
    private static int Points = 6420; //TODO: запись в бд всех очков удаленно

    public static void save(Context context){
        //if(account.first_name != null ) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            Editor editor = prefs.edit();
            editor.putString("access_token", access_token);
            editor.putLong("user_id", user_id);
            editor.putString("AccountFirstName", account.first_name);
            editor.putString("AccountLastName", account.last_name);
            editor.commit();
       // }
        // else
        //Toast.makeText(context,R.string.InternetError,Toast.LENGTH_LONG);
    }
  /*  public static void exit(Context context){ //TODO: НЕ ПРАВИЛЬНО! МОЖНО ЛИ ЗАПИСАТЬ ЧЕРЕЗ ТОТ ЖЕ САМЫЙ КЛЮЧ!?
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putString("access_token", null);
        editor.putLong("user_id", 0);
        editor.putString("AccountFirstName", null);
        editor.putString("AccountLastName", null);
        editor.commit();
    }*/
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