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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Жамбыл on 29.03.2015.
 */
public class Top_Tab2 extends Fragment {
    Typeface type_thin;
    ListView VkRowListView2;

    //Данные для вк_адаптера
    final String ATTRIBUTE_NAME_TEXT_NAME = "text_name";
    final String ATTRIBUTE_NAME_TEXT_RAITING = "text_place";
    final String ATTRIBUTE_NAME_IMAGE = "image";
    final int NUMBER_OF_SHOWING_USERS = 11;//TODO change

    List<String> FriendNames =  new ArrayList<String>();
    List<Integer> PointsList = new ArrayList<Integer>();
    List<byte[]>PhotoAsBytesList = new ArrayList<byte[]>();

    // массив имен атрибутов, из которых будут читаться данные
    String[] from = { ATTRIBUTE_NAME_TEXT_NAME, ATTRIBUTE_NAME_TEXT_RAITING,
            ATTRIBUTE_NAME_IMAGE };

    // массив ID View-компонентов, в которые будут вставлять данные
    int[] to = { R.id.vk_name, R.id.vk_raiting, R.id.vk_photo };

    //Дата, куда пакуем
    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(NUMBER_OF_SHOWING_USERS);
    Map<String, Object> m;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.top_tab_2,container,false);

        StartUI(v);
        LoadFriends(getActivity());

        //Пакуем и отправляем
        PackAndSendData(v);

        return v;
    }
    private void StartUI(View v)
    {
        type_thin= Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
    }
    //TODO динамическая загрузка
    private void PackAndSendData(View v)
    {
        //Collections.sort(PointsList);
        for(int i=0;i< NUMBER_OF_SHOWING_USERS; i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT_NAME, FriendNames.get(i));
            m.put(ATTRIBUTE_NAME_TEXT_RAITING, PointsList.get(i));
            m.put(ATTRIBUTE_NAME_IMAGE, PhotoAsBytesList.get(i));
            data.add(m);
           // Log.e("", String.valueOf(m.get(ATTRIBUTE_NAME_TEXT_RAITING)));

        }
        VkRowListView2 = (ListView) v.findViewById(R.id.VkRowListView2);
        // создаем адаптер
        MapComparator(data);
        Vk_row_adapter sAdapter2 = new Vk_row_adapter(getActivity(), data, R.layout.vk_row,
                from, to);
        //привязываем и сетим

        VkRowListView2.setAdapter(sAdapter2);
        //Show();
    }
    private void MapComparator(ArrayList<Map<String, Object>> input){
        //массив
        for(int i = 0; i<input.size();i++)
            for(int j = 0; j < input.size() - i - 1; j++)
                if((int)input.get(j).get(ATTRIBUTE_NAME_TEXT_RAITING)
                        <(int)input.get(j+1).get(ATTRIBUTE_NAME_TEXT_RAITING))
                         Collections.swap( input, j, j+1);

    }

    public void LoadFriends(Context context){
        //TODO Progress dialog
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        for(int i=0;i<NUMBER_OF_SHOWING_USERS; i++){
            if (prefs != null){
                String str = prefs.getString("UserFirstName" + String.valueOf(i), null);
                if(str != null && !str.isEmpty()) {
               FriendNames.add(prefs.getString("UserFirstName" + String.valueOf(i), null) +
                        " " + prefs.getString("UserLastName" + String.valueOf(i), null));
                String PhotoEncoded = prefs.getString("UserPhoto"+ String.valueOf(i), null);

                /*if(PhotoEncoded.equals(null))
                    Log.e("Top_tab_2","PhotoEncoded = null");
                else*/
                   // Log.e("Top_tab_2 PhotoEncoded",PhotoEncoded);
                byte[] b = PhotoEncoded.getBytes();
                byte[] PhotoAsBytes = Base64.decode(b, Base64.DEFAULT);
                if(PhotoAsBytes == null)
                    Log.e("Top_tab_2 PhotoAsBytes","PhotoAsBytes = null " + String.valueOf(i));
                PhotoAsBytesList.add(PhotoAsBytes);

                int points =  Integer.parseInt(prefs.getString("UserPoints" + String.valueOf(i), null));
                PointsList.add(points);

/////////////////////////////
     /*           FriendNames.add(prefs.getString("FriendFirstName" + String.valueOf(i), null) +
                          " " + prefs.getString("FriendLastName" + String.valueOf(i), null));
                String PhotoEncoded = prefs.getString("FriendPhoto"+ String.valueOf(i), null);
                //Log.e("Tob_tab2",PhotoEncoded);
                byte[] b = PhotoEncoded.getBytes();
                byte[] PhotoAsBytes = Base64.decode(b, Base64.DEFAULT);
                PhotoAsBytesList.add(PhotoAsBytes);
*/}
            }
        }
        Log.i("Top_Tab2","Loading Friends");
    }

}
