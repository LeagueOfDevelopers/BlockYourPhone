package com.example.blockphone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKWallPostResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Жамбыл on 29.03.2015.
 */
public class Top_Tab1 extends Fragment {
    ListView VkRowListView1;
    Button PostToWallButton;
    TextView NoFriendsText;
    Typeface type_thin;
    Boolean isPosted = false;
    //Данные для вк_адаптера
    final String ATTRIBUTE_NAME_TEXT_NAME = "text_name";
    final String ATTRIBUTE_NAME_TEXT_PLACE = "text_place";
    final String ATTRIBUTE_NAME_IMAGE = "image";
    final int NUMBER_OF_SHOWING_USERS = 1;//TODO change


    List<String> FriendNames =  new ArrayList<String>();
    List<Integer> PointsList = new ArrayList<Integer>();
    List<byte[]>PhotoAsBytesList = new ArrayList<byte[]>();


    String[] Places = {"6420","5840","4480","3200","2100","1980","1500"};
    int img = R.drawable.zhambul;

    // массив имен атрибутов, из которых будут читаться данные
    String[] from = {ATTRIBUTE_NAME_TEXT_NAME, ATTRIBUTE_NAME_TEXT_PLACE,
            ATTRIBUTE_NAME_IMAGE };
    // массив ID View-компонентов, в которые будут вставлять данные
    int[] to = { R.id.vk_name, R.id.vk_raiting, R.id.vk_photo };
    SimpleAdapter sAdapter1;

    //Дата, куда пакуем
        ArrayList<Map<String, Object>> data;
        Map<String, Object> m;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.top_tab_1,container,false);

        StartUI(v);
        LoadFriends(getActivity());
        //Names.add("Жамбыл");
        //TODO: проверка установил ли друг приложения

        //проверяем на наличие друзей
        if(FriendNames != null & !FriendNames.isEmpty()){
            Log.e("Friendsize",String.valueOf(FriendNames.size()));
            Log.e("Friendsize",String.valueOf(FriendNames.get(0)));
            data  = new ArrayList<Map<String, Object>>(FriendNames.size());
            //пакуем и отправляем
            PackAndSendData(v);
        }
        else {
            PostToWallButton.setVisibility(View.VISIBLE);
            NoFriendsText.setVisibility(View.VISIBLE);
        }
        return v;
    }

    //Пакуем и отправляем дату
    private void PackAndSendData(View v)
    {
        for(int i=0;i<NUMBER_OF_SHOWING_USERS; i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT_NAME, FriendNames.get(i));
            m.put(ATTRIBUTE_NAME_TEXT_PLACE, Places[i]);
            m.put(ATTRIBUTE_NAME_IMAGE, PhotoAsBytesList.get(i));

            data.add(m);


        }
        // создаем адаптер
        sAdapter1 = new SimpleAdapter(getActivity(), data, R.layout.vk_row,from, to);
    }

    public void LoadFriends(Context context){
        //TODO Progress dialog
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        for(int i=0;i<NUMBER_OF_SHOWING_USERS; i++){
            if (prefs != null){
                String str = prefs.getString("1FriendFirstName" + String.valueOf(i), null);
                if(str != null && !str.isEmpty()) {
                    FriendNames.add(prefs.getString("1FriendFirstName" + String.valueOf(i), null) +
                            " " + prefs.getString("1FriendLastName" + String.valueOf(i), null));
                    String PhotoEncoded = prefs.getString("1FriendPhoto" + String.valueOf(i), null);
                    //Log.e("Tob_tab2",PhotoEncoded);
                    byte[] b = PhotoEncoded.getBytes();
                    byte[] PhotoAsBytes = Base64.decode(b, Base64.DEFAULT);
                    PhotoAsBytesList.add(PhotoAsBytes);
                }
            }
        }
        Log.i("Top_Tab2","Loading Friends");
    }
    private void StartUI(View v)
    {
        type_thin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");

        PostToWallButton = (Button)v.findViewById(R.id.PostToWallButton);
        PostToWallButton.setVisibility(View.GONE);
        PostToWallButton.setOnClickListener(onWallButtonClickListener);

        //PostToWallButton.setTypeface(type_thin);
        PostToWallButton.setBackgroundColor(getActivity().getResources().getColor(R.color.ColorPrimaryDark));
        PostToWallButton.setTextColor(Color.WHITE);
        //PostToWallButton.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Light.ttf"));

        NoFriendsText = (TextView)v.findViewById(R.id.NoFriendsText);
        NoFriendsText.setVisibility(View.GONE);
        NoFriendsText.setTypeface(type_thin);

        VkRowListView1 = (ListView) v.findViewById(R.id.VkRowListView1);
        VkRowListView1.setAdapter(sAdapter1);

    }

    private View.OnClickListener onWallButtonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Поделиться")
                    .setMessage("Добавить запись на стену Вконтакте?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    PostToWallButton.setVisibility(View.GONE);
                                    makePost(null, "Test!");

                                    //if(!isPosted)
                                        //Toast.makeText(getActivity(), "Запись не добавлена", Toast.LENGTH_SHORT).show();
                                    //getActivity().onBackPressed();
                                }
                            }).create().show();
        }
    };
    private void makePost(VKAttachments attachments, String message) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        long id = 0;
        if( prefs != null ) {
            id = prefs.getLong("AccountId", 0);
        }
        if(id!= 0) {
            VKRequest post = VKApi.wall().post(VKParameters.from
                    (VKApiConst.OWNER_ID, id, VKApiConst.ATTACHMENTS, attachments, VKApiConst.MESSAGE, message));

        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                isPosted = true;
                Toast.makeText(getActivity(), "Запись успешно добавлена", Toast.LENGTH_SHORT).show();
                super.onComplete(response);
            }

            /*@Override
            public void onError(VKError error) {
                showError(error.apiError != null ? error.apiError : error);
            }*/
        });
    }}
    private void showError(VKError error) {
        new AlertDialog.Builder(getActivity())
                .setMessage(error.errorMessage)
                .setPositiveButton("OK", null)
                .show();
    }
}