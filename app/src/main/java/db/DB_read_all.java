package db;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.example.blockphone.Account;
import com.example.blockphone.App;
import com.example.blockphone.Internet;
import com.example.blockphone.Top_Tab2;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Жамбыл on 18.04.2015.
 */
public final class DB_read_all  extends AsyncTask<String, String, String> {
    Context context;
    private static String url_read_all = "http://women.egeshki.ru/blockphonedb/read_all.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "users";
    private static final String TAG_ID = "id";
    private static final String TAG_FIRST_NAME = "first_name";
    private static final String TAG_LAST_NAME = "last_name";
    private static final String TAG_VK_ID = "vk_id";
    private static final String TAG_POINTS = "points";


    public static List<String> ListOfFName = new ArrayList<String>();
    public static List<String> ListOfLName = new ArrayList<String>();
    public static List<String> ListOfVkId = new ArrayList<String>();
    public static List<String> ListOfPoints = new ArrayList<String>();

    String id;
    String first_name;
    String last_name;
    String vk_id;
    String points;
    int I=0;
    boolean isReady = false;

    String photo_url;

    JSONParser jParser = new JSONParser();


    JSONArray users = null;
    public static int searchPoints(String thisVkId){
        for(int i = 0; i<ListOfVkId.size();i++){
            if(ListOfVkId.get(i).equals(thisVkId))
                return Integer.valueOf(ListOfPoints.get(i));
        }
        return -2;

    }

    public DB_read_all(Context _context){
        context = _context;
    }
    @Override
    protected String doInBackground(String... strings) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_read_all, "GET", params);

        // Check your log cat for JSON reponse
        if(json!=null){
            //Log.e("Users_all: ", json.toString());
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    users = json.getJSONArray(TAG_USERS);
                    Log.e("Succes getting users, users amount", String.valueOf(users.length()));

                    //iters  = users.length()<Top_Tab2.NUMBER_OF_SHOWING_USERS
                    //      ?users.length():Top_Tab2.NUMBER_OF_SHOWING_USERS;


                    for (int i = 0; i < users.length(); i++) {

                        JSONObject c = users.getJSONObject(i);
                        id = c.getString(TAG_ID);
                        first_name = c.getString(TAG_FIRST_NAME); //
                        String s = new String(first_name.getBytes("ISO-8859-1"), "Windows-1251");
                        first_name = new String(("\uFEFF" + s).getBytes("UTF-8"));
                        ListOfFName.add(first_name);
                        //Log.e("test",first_name);

                        last_name = c.getString(TAG_LAST_NAME);   //
                        String l = new String(last_name.getBytes("ISO-8859-1"), "Windows-1251");
                        last_name = new String(("\uFEFF" + l).getBytes("UTF-8"));
                        ListOfLName.add(last_name);

                        vk_id = c.getString(TAG_VK_ID);
                        ListOfVkId.add(vk_id);

                        points= c.getString(TAG_POINTS);
                        ListOfPoints.add(points);
                    }
                } else {
                    Log.e("DB_read_all","Db Error!");
                }
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.SEVERE, "an exception was thrown while converting", e);
                Log.e("DB_read_all","Db Error!");
            }
        } else{
            Log.e("DB_read_all","JSON ERROR");
        }

        //Sort users by points
        for(int i = 0; i<ListOfFName.size();i++)
            for(int j = 0; j < ListOfFName.size() - i - 1; j++)
                if(Integer.valueOf(ListOfPoints.get(j))<Integer.valueOf(ListOfPoints.get(j + 1))){
                    Collections.swap(ListOfFName, j, j + 1);
                    Collections.swap(ListOfLName, j, j + 1);
                    Collections.swap(ListOfPoints, j, j + 1);
                    Collections.swap(ListOfVkId, j, j + 1);
                }

        Account.setPoints(context,searchPoints(Account.getVkId()),false);
        new DB_create(context, Account.getFirstName(), Account.getLastName(),
                Account.getVkId()).execute();
        VK_Friends.isFriendsReady = true;

        for(int i = 0; i<ListOfFName.size();i++){
            setUserPhotoUrl(ListOfFName.get(i), ListOfLName.get(i), ListOfVkId.get(i), ListOfPoints.get(i));
            while (!isReady)
                try {
                    Thread.sleep(50);
                    //todo change
                    //Log.e("DB_read_all","Sleeping");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            isReady = false;
        }
        return null;
    }
    private void setUserPhotoUrl(final String _first_name,
                                 final String _last_name,final String _vk_id,final String _points){
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, _vk_id,
                VKApiConst.FIELDS, "photo_100"));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(final VKResponse response) {
                super.onComplete(response);
                VKList<VKApiUser> User = (VKList<VKApiUser>) response.parsedModel;
                if(User.get(0).photo_100!=null)
                    photo_url = User.get(0).photo_100;

                Bitmap photoBm = null;

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                if(photo_url !=null){
                    try {
                        photoBm = Internet.convertUrlToImage(photo_url);
                    }
                    catch (Exception e)
                    {
                        Logger logger = Logger.getAnonymousLogger();
                        logger.log(Level.SEVERE, "an exception was thrown while converting", e);
                    }
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if(photoBm!= null)
                    photoBm.compress(Bitmap.CompressFormat.PNG, 100, baos);

                byte[] b = baos.toByteArray();
                String encodedPhoto = Base64.encodeToString(b, Base64.DEFAULT);

                editor.putString("UserFirstName" + String.valueOf(I), _first_name);
                editor.putString("UserLastName" + String.valueOf(I), _last_name);
                editor.putString("UserVkId" + String.valueOf(I), _vk_id);
                editor.putString("UserPoints" + String.valueOf(I), _points);
                editor.putString("UserPhoto"+String.valueOf(I),encodedPhoto);
                editor.apply();
                if(I == users.length() - 1){
                    Log.e("DB_read_all","Success");
                }
                isReady = true;
                I++;
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Log.e("","Я старался");
            } });

    }
}



