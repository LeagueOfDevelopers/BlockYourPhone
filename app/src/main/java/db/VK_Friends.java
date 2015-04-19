package db;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.example.blockphone.Internet;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Жамбыл on 19.04.2015.
 */
public class VK_Friends extends AsyncTask<String, String, String> {
    Context context;
    public static int NumberOfFriend = 0;
    public static boolean isFriendsReady= false;

    public  VK_Friends(Context _context){context = _context;}

    @Override
    protected String doInBackground(String... strings) {
        Log.e("App", "getting friends data");
        //todo Change to getAppUsers
        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS,
                "id,first_name,last_name,photo_100,"));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(final VKResponse response) {
                super.onComplete(response);
                    VKList<VKApiUser> _Friends = (VKList<VKApiUser>) response.parsedModel;
                    if(_Friends != null){
                        if(_Friends.size()!= 0){
                            while(!isFriendsReady)
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            for (int i = 0; i < _Friends.size(); ++i) {
                                int thisPoints = -3;
                                thisPoints = DB_read_all.searchPoints(String.valueOf(_Friends.get(i).id));
                                if(thisPoints!=-2)
                                {
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    String photoUrl = _Friends.get(i).photo_100;
                                    Bitmap photoBm = null;
                                    if (photoUrl != null) {
                                        String previousUrl = prefs.getString("FriendPhotoUrl" + String.valueOf(i), null);
                                        if (!photoUrl.equals(previousUrl)) {
                                            try {
                                                photoBm = Internet.convertUrlToImage(photoUrl);
                                            } catch (Exception e) {
                                                Logger logger = Logger.getAnonymousLogger();
                                                logger.log(Level.SEVERE, "an exception was thrown while converting", e);
                                            }
                                        }
                                    }
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    if (photoBm != null)
                                        photoBm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                    Log.e("Friends",String.valueOf(NumberOfFriend));
                                    byte[] b = baos.toByteArray();
                                    String encodedPhoto = Base64.encodeToString(b, Base64.DEFAULT);

                                    editor.putString("FriendPhoto" + String.valueOf(NumberOfFriend), encodedPhoto);
                                    editor.putString("FriendFirstName" + String.valueOf(NumberOfFriend), _Friends.get(i).first_name);
                                    editor.putString("FriendLastName" + String.valueOf(NumberOfFriend), _Friends.get(i).last_name);
                                    editor.putString("FriendPoints" + String.valueOf(NumberOfFriend),String.valueOf(thisPoints));
                                    //editor.putString("FriendPhotoUrl" + String.valueOf(i), Friends.get(i).photo_100);
                                    editor.putLong("FriendId" + String.valueOf(NumberOfFriend), _Friends.get(i).id);
                                    NumberOfFriend++;
                                    editor.apply();
                                }
                            }}
                        else{Log.e("","Нет друзей");}}else{Log.e("","Нет друзей");}
            }
        });
        return null;
    }
}
