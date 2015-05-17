package activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blockphone.Account;
import com.example.blockphone.R;
import com.example.blockphone.TopTab;
import com.example.blockphone.Vk_row_adapter;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
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
public final class TopTab1 extends TopTab {

    ListView VkRowListView1;
    Button PostToWallButton;
    TextView NoFriendsText;
    Typeface type_thin;
    Boolean isPosted = false;
    Boolean isReady = false;



    List<String> FriendNames =  new ArrayList<String>();
    List<String> PointsList = new ArrayList<String>();
    List<byte[]>PhotoAsBytesList = new ArrayList<byte[]>();

    String[] from = {ATTRIBUTE_NAME_TEXT_NAME, ATTRIBUTE_NAME_TEXT_PLACE,
            ATTRIBUTE_NAME_IMAGE };
    int[] to = { R.id.vk_name, R.id.vk_raiting, R.id.vk_photo };
    Vk_row_adapter sAdapter1;
    View _v;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.top_tab_1,container,false);
        _v = v;
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                loadFriends(getActivity());
            }
        };

        thread1.start();

        startLocalUI(v);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(prefs.getBoolean("hasFriends",false)){
            data  = new ArrayList<>(FriendNames.size());
            Thread thread = new Thread() {
                public void run() {
                    try {
                        if (!isReady) Thread.sleep(300);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PackAndSendData(_v);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
        else {
            PostToWallButton.setVisibility(View.VISIBLE);
            NoFriendsText.setVisibility(View.VISIBLE);
        }
        return v;
    }
    protected void PackAndSendData(View v)
    {
        for(int i=0;i<FriendNames.size(); i++) {
            m = new HashMap<>();
            m.put(ATTRIBUTE_NAME_TEXT_NAME, FriendNames.get(i));
            m.put(ATTRIBUTE_NAME_TEXT_PLACE,PointsList.get(i));
            m.put(ATTRIBUTE_NAME_IMAGE, PhotoAsBytesList.get(i));
            data.add(m);
        }

        VkRowListView1 = (ListView) v.findViewById(R.id.VkRowListView1);
        sAdapter1 = new Vk_row_adapter(getActivity(), data, R.layout.vk_row,
                from, to,1);
        VkRowListView1.setAdapter(sAdapter1);
    }

    protected void loadFriends(Context context){
        //TODO Progress dialog

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        for(int i=0;i<NUMBER_OF_SHOWING_USERS; i++){
            if (prefs != null){
                String str = prefs.getString("FriendPhoto" + String.valueOf(i), null);
                if(str != null && !str.isEmpty()) {
                    FriendNames.add(prefs.getString("FriendFirstName" + String.valueOf(i), null) +
                            " " + prefs.getString("FriendLastName" + String.valueOf(i), null));
                    PointsList.add(prefs.getString("FriendPoints" + String.valueOf(i), null));
                    String PhotoEncoded = prefs.getString("FriendPhoto" + String.valueOf(i), null);
                    byte[] b = PhotoEncoded.getBytes();
                    byte[] PhotoAsBytes = Base64.decode(b, Base64.DEFAULT);
                    PhotoAsBytesList.add(PhotoAsBytes);
                }
            }
        }
        isReady = true;
    }
    public void startLocalUI(View v)
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
                                }
                            }).create().show();
        }
    };

    private void makePost(VKAttachments attachments, String message) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        long id = 0;
        if( prefs != null ) {
            id = Integer.valueOf(Account.getVkId());
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
        });
    }}
}