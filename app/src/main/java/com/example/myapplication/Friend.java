package com.example.myapplication;

/**
 * Created by Жамбыл on 02.04.2015.
 */
public class Friend {

    private String Name;
    private int PositionInTop;
    private String dateFormat;

    public Friend(String _Name, int _PositionInTop, String _dateFormat ){
        Name = _Name;
        PositionInTop = _PositionInTop;
        dateFormat = _dateFormat;
    }
    public String getFriendName(){
        return Name;
    }
    public int getFriendPosition(){
        return PositionInTop;
    }

    public String getFriendBD(){
        return dateFormat;
    }


}
