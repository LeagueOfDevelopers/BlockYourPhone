package com.example.blockphone;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Жамбыл on 29.03.2015.
 */
public final class Top_Tab2 extends Fragment {
    Typeface type_thin;
    ListView VkRowListView2;

    final String ATTRIBUTE_NAME_TEXT_NAME = "text_name";
    final String ATTRIBUTE_NAME_TEXT_RAITING = "text_place";
    final String ATTRIBUTE_NAME_IMAGE = "image";
    final String ATTRIBUTE_NAME_VKID = "vk_id";
    public static final int NUMBER_OF_SHOWING_USERS = 12;//TODO change
    View _v;

    List<String> FriendNames =  new ArrayList<String>();
    List<Integer> PointsList = new ArrayList<Integer>();
    List<String> VkIdList = new ArrayList<String>();
    List<byte[]>PhotoAsBytesList = new ArrayList<byte[]>();

    String[] from = { ATTRIBUTE_NAME_TEXT_NAME, ATTRIBUTE_NAME_TEXT_RAITING,
            ATTRIBUTE_NAME_IMAGE,ATTRIBUTE_NAME_VKID};

    int[] to = { R.id.vk_name, R.id.vk_raiting, R.id.vk_photo };

    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(NUMBER_OF_SHOWING_USERS);
    Map<String, Object> m;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.top_tab_2,container,false);
        _v = v;
        StartUI(v);

        Thread thread = new Thread() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadFriends(getActivity());
                        PackAndSendData(_v);
                    }
                });
            }
        };
        thread.start();

        return v;
    }
    final Runnable r = new Runnable() {
        public void run() {
            LoadFriends(getActivity());
            PackAndSendData(_v);
        }
    };

    private void StartUI(View v)
    {
        type_thin= Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
    }
    //TODO динамическая загрузка
    private void PackAndSendData(View v)
    {
        for(int i=0;i< FriendNames.size(); i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT_NAME, FriendNames.get(i));
            m.put(ATTRIBUTE_NAME_TEXT_RAITING, PointsList.get(i));
            m.put(ATTRIBUTE_NAME_IMAGE, PhotoAsBytesList.get(i));
            m.put(ATTRIBUTE_NAME_VKID , VkIdList.get(i));
            data.add(m);

        }
        VkRowListView2 = (ListView) v.findViewById(R.id.VkRowListView2);
        Vk_row_adapter sAdapter2 = new Vk_row_adapter(getActivity(), data, R.layout.vk_row,
                from, to,2);
        VkRowListView2.setAdapter(sAdapter2);
    }

    public void LoadFriends(Context context){
        //TODO Progress dialog
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        for(int i=0;i<NUMBER_OF_SHOWING_USERS; i++){
            if (prefs != null){
                String str = prefs.getString("UserPhoto" + String.valueOf(i), null);
                if(str != null && !str.isEmpty()) {
                    try {
                        FriendNames.add(prefs.getString("UserFirstName" + String.valueOf(i), null) +
                                " " + prefs.getString("UserLastName" + String.valueOf(i), null));
                        PointsList.add(Integer.parseInt(prefs.getString("UserPoints" + String.valueOf(i), null)));
                        VkIdList.add((prefs.getString("UserVkId" + String.valueOf(i), null)));
                        String PhotoEncoded = prefs.getString("UserPhoto" + String.valueOf(i), null);
                        byte[] b = PhotoEncoded.getBytes();
                        byte[] PhotoAsBytes = Base64.decode(b, Base64.DEFAULT);
                        if (PhotoAsBytes == null)
                            Log.e("Top_tab_2 PhotoAsBytes", "PhotoAsBytes = null " + String.valueOf(i));
                        PhotoAsBytesList.add(PhotoAsBytes);
                    } catch (Exception e){
                        Logger logger = Logger.getAnonymousLogger();
                        logger.log(Level.SEVERE, "an exception was thrown", e);
                    }
                }
            }
        }
        Log.i("Top_Tab2","Loading Friends");
    }

}
