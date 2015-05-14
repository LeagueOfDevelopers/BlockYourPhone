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
    public static boolean isFriendsReady= false;
    boolean isReady = false;
    
    public static List<String> ListOfFName = new ArrayList<String>();
    public static List<String> ListOfLName = new ArrayList<String>();
    public static List<String> ListOfVkId = new ArrayList<String>();
    public static List<String> ListOfPoints = new ArrayList<String>();
    public static List<String> ListOfEncPhoto = new ArrayList<String>();

    public  VK_Friends(Context _context){context = _context;}

    @Override
    protected String doInBackground(String... strings) {
        Log.e("VK_Friends", "getting friends data");
        //todo Change to getAppUsers
        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS,
                "id,first_name,last_name,photo_100,"));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(final VKResponse response) {
                super.onComplete(response);
                Log.e("Vk_Friends","Request Complete");
                    VKList<VKApiUser> _Friends = (VKList<VKApiUser>) response.parsedModel;
                    if(_Friends != null){
                        if(_Friends.size()!= 0) {
                            while (!isFriendsReady)
                                try {
                                    Thread.sleep(300);
                                    Log.e("VK_Friends", "Sleeping 1");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            for (int i = 0; i < _Friends.size(); ++i) {
                                int thisPoints = -3;
                                thisPoints = DB_read_all.searchPoints(String.valueOf(_Friends.get(i).id));
                                if (thisPoints != -2) {
                                    Log.e("VK_Friends",_Friends.get(i).first_name);
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("hasFriends", true);
                                    editor.apply();

                                    String photoUrl = _Friends.get(i).photo_100;
                                    Bitmap photoBm = null;
                                    if (photoUrl != null) {
                                        String previousUrl = prefs.getString("FriendPhotoUrl" + String.valueOf(i), null);
                                        if (!photoUrl.equals(previousUrl)) try {
                                            photoBm = Internet.convertUrlToImage(photoUrl);
                                        } catch (Exception e) {
                                            Logger logger = Logger.getAnonymousLogger();
                                            logger.log(Level.SEVERE, "an exception was thrown while converting", e);
                                        }
                                    }
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    if (photoBm != null)
                                        photoBm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                    byte[] b = baos.toByteArray();
                                    String encodedPhoto = Base64.encodeToString(b, Base64.DEFAULT);
                                    ListOfFName.add(_Friends.get(i).first_name);
                                    ListOfLName.add(_Friends.get(i).last_name);
                                    ListOfPoints.add(String.valueOf(thisPoints));
                                    ListOfVkId.add(String.valueOf(_Friends.get(i).id));
                                    ListOfEncPhoto.add(encodedPhoto);

                                }
                                if (i == _Friends.size() - 1) {
                                    isReady = true;
                                }
                            }
                            //bubble
                            for (int k = 0; k < ListOfFName.size(); k++)
                                for (int j = 0; j < ListOfFName.size() - k - 1; j++)
                                    if (Integer.valueOf(ListOfPoints.get(j)) < Integer.valueOf(ListOfPoints.get(j + 1))) {
                                        Collections.swap(ListOfFName, j, j + 1);
                                        Collections.swap(ListOfLName, j, j + 1);
                                        Collections.swap(ListOfPoints, j, j + 1);
                                        Collections.swap(ListOfVkId, j, j + 1);
                                        Collections.swap(ListOfEncPhoto, j, j + 1);
                                    }


                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = prefs.edit();

                            for (int k = 0; k < ListOfFName.size(); k++) {
                                editor.putString("FriendPhoto" + String.valueOf(k), ListOfEncPhoto.get(k));
                                editor.putString("FriendFirstName" + String.valueOf(k), ListOfFName.get(k));
                                editor.putString("FriendLastName" + String.valueOf(k), ListOfLName.get(k));
                                editor.putString("FriendPoints" + String.valueOf(k), ListOfPoints.get(k));
                                //editor.putString("FriendPhotoUrl" + String.valueOf(i), Friends.get(i).photo_100);
                                editor.putString("FriendVkId" + String.valueOf(k), ListOfVkId.get(k));
                                editor.apply();
                            }
                            Log.e("VK_Friends","Success");
                        }
                        else{Log.e("","Нет друзей");}}else{Log.e("","Нет друзей");}
            }
        });
        return null;
    }

    public static void wipeFriendsData(Context thiscontext){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(thiscontext);
        SharedPreferences.Editor editor = prefs.edit();

        for(int k = 0; k<ListOfFName.size();k++) {
            editor.putString("FriendPhoto" + String.valueOf(k), null);
            editor.putString("FriendFirstName" + String.valueOf(k), null);
            editor.putString("FriendLastName" + String.valueOf(k), null);
            editor.putString("FriendPoints" + String.valueOf(k), null);
            //editor.putString("FriendPhotoUrl" + String.valueOf(i), null);
            editor.putString("FriendVkId" + String.valueOf(k), null);
            editor.apply();
        }

    }
}
