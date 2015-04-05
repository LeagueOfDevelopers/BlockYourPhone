package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
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
    List<String> FriendNames =  new ArrayList<String>();
    List<Bitmap> FriendImages = new ArrayList<Bitmap>();
    Bitmap test;
    String[] Points = {"6420","5840","4480","3200","2100","1980","1500"};

    int img = R.drawable.zhambul;



    // массив имен атрибутов, из которых будут читаться данные
    String[] from = { ATTRIBUTE_NAME_TEXT_NAME, ATTRIBUTE_NAME_TEXT_RAITING,
            ATTRIBUTE_NAME_IMAGE };
    // массив ID View-компонентов, в которые будут вставлять данные
    int[] to = { R.id.vk_name, R.id.vk_raiting, R.id.vk_photo };

    //Дата, куда пакуем
    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
            7);//TODO: не забудь поменять
    Map<String, Object> m;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.top_tab_2,container,false);

        StartUI(v);

        //Пакуем и отправляем
        PackAndSendData(v);

        return v;
    }
    private void StartUI(View v)
    {
        type_thin= Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
    }
    private void PackAndSendData(View v)
    {
        getFriends(getActivity());
        LoadFriends(getActivity());
        for(int i=0;i< 7; i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT_NAME, FriendNames.get(i));
            m.put(ATTRIBUTE_NAME_TEXT_RAITING, Points[i] + " очков");
            m.put(ATTRIBUTE_NAME_IMAGE, FriendImages.get(i));
            data.add(m);

            // создаем адаптер
            Vk_row_adapter sAdapter2 = new Vk_row_adapter(getActivity(), data, R.layout.vk_row,
                    from, to);
            //привязываем и сетим
            VkRowListView2 = (ListView) v.findViewById(R.id.VkRowListView2);
            VkRowListView2.setAdapter(sAdapter2);
        }
    }
    public void getFriends(final Context context){
        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS,
                "id,first_name,last_name,photo_100,"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList<VKApiUser> Friends = (VKList<VKApiUser>)response.parsedModel;

                SharedPreferences prefs  = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();

                for(int i = 0; i< Friends.size();++i) {
                    editor.putString("FriendFirstName" + String.valueOf(i), Friends.get(i).first_name);
                    editor.putString("FriendLastName"+ String.valueOf(i), Friends.get(i).last_name);
                    editor.putString("FriendPhoto"+ String.valueOf(i), Friends.get(i).photo_100);
                    editor.commit();
                }
            }
        });
    }
    public void LoadFriends(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String photoUrl;
        for(int i=0;i< 7; i++){
            if (prefs != null){
                FriendNames.add(prefs.getString("FriendFirstName" + String.valueOf(i), null) +
                            " " + prefs.getString("FriendLastName" + String.valueOf(i), null));
                photoUrl = prefs.getString("FriendPhoto"+ String.valueOf(i), null);
                FriendImages.add(Account.convertUrlToImage(photoUrl));
            }
        }
    }

}