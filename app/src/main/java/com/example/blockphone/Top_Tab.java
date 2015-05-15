package com.example.blockphone;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Æאלבûכ on 15.05.2015.
 */
abstract class Top_Tab extends Fragment {

    final String ATTRIBUTE_NAME_TEXT_NAME = "text_name";
    final String ATTRIBUTE_NAME_TEXT_PLACE = "text_place";
    final String ATTRIBUTE_NAME_IMAGE = "image";
    final String ATTRIBUTE_NAME_VKID = "vk_id";
    final int NUMBER_OF_SHOWING_USERS = 12;//TODO change

    /*
        Methods
     */
    abstract void PackAndSendData(View v);
    abstract void LoadFriends(Context context);
    abstract void StartUI(View v);

}
