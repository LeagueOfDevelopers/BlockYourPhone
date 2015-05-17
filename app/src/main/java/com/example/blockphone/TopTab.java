package com.example.blockphone;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Жамбыл on 15.05.2015.
 */
abstract public class TopTab extends Fragment{

    final public String ATTRIBUTE_NAME_TEXT_NAME = "text_name";
    final public String ATTRIBUTE_NAME_TEXT_PLACE = "text_place";
    final public String ATTRIBUTE_NAME_IMAGE = "image";
    final public String ATTRIBUTE_NAME_VKID = "vk_id";
    final public int NUMBER_OF_SHOWING_USERS = 13;//TODO change

    /*
        Methods
     */
    protected abstract void PackAndSendData(View v);
    protected abstract void loadFriends(Context context);

}
