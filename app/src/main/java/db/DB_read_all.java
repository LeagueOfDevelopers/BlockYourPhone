package db;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Жамбыл on 18.04.2015.
 */
public class DB_read_all  extends AsyncTask<String, String, String> {
    Context context;
    private static String url_read_all = "http://women.egeshki.ru/blockphonedb/read_all.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "users";
    private static final String TAG_ID = "id";
    private static final String TAG_FIRST_NAME = "first_name";
    private static final String TAG_LAST_NAME = "last_name";
    private static final String TAG_VK_ID = "vk_id";
    private static final String TAG_POINTS = "points";

    String id;
    String first_name;
    String last_name;
    String vk_id;
    String vk_id1;
    String points;
    int I=0;
    boolean isReady = false;


    String photo_url;

    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> usersList;

    JSONArray users = null;
    public DB_read_all(Context _context){
        context = _context;
    }
    @Override
    protected String doInBackground(String... strings) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_read_all, "GET", params); //TODO error ut8

        // Check your log cat for JSON reponse
        if(json!=null){
            Log.e("Users_all: ", json.toString());
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    users = json.getJSONArray(TAG_USERS);

                    // looping through All Products
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject c = users.getJSONObject(i);
                        //I=i;
                        // Storing each json item in variable
                        id = c.getString(TAG_ID);
                        first_name = c.getString(TAG_FIRST_NAME);
                        last_name = c.getString(TAG_LAST_NAME);
                        vk_id = c.getString(TAG_VK_ID);
                        points= c.getString(TAG_POINTS);
                        setUserPhotoUrl(vk_id, first_name, last_name,vk_id, points);

                        Log.e("first", String.valueOf(i));

                        Log.e("Db",id);
                        Log.e("Db",first_name);
                        Log.e("Db",last_name);
                        Log.e("Db",vk_id);
                        Log.e("Db",points);

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = prefs.edit();
                        if(first_name.equals(null) | last_name.equals(null)
                                | vk_id.equals(null)| points.equals(null))
                            Log.e("DB_read_all", "SMTH IS NULL");

                        isReady = false;
                    }
                } else {
                      Log.e("DB_read_all","no users found");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else{
            Log.e("DB_read_all","JSON ERROR");
        }

        return null;
    }
    private void setUserPhotoUrl(final String vk_id, final String _first_name,
                                 final String _last_name,final String _vk_id,final String _points){
        //final String[] url = new String[1];
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, vk_id,
                VKApiConst.FIELDS, "photo_100"));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(final VKResponse response) {
                super.onComplete(response);
                        VKList<VKApiUser> User = (VKList<VKApiUser>) response.parsedModel;
                        photo_url = User.get(0).photo_100;
                        //Log.e("PhotoUrl", photo_url);

                        Bitmap photoBm = null;

                        //Log.e("DB_read_all photo_url",photo_url);
                        Log.e("second i", String.valueOf(I)+" " + _first_name);
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = prefs.edit();
                        if(photo_url !=null){
                            //Log.e("DB_read_all photo_url",photo_url);
                            //String previousUrl =  prefs.getString("FriendPhotoUrl" + String.valueOf(i), null);
                            //if(photoUrl!=previousUrl) {
                            try {
                                photoBm = Internet.convertUrlToImage(photo_url);
                            }
                            catch (Exception e)
                            {
                                Logger logger = Logger.getAnonymousLogger();
                                logger.log(Level.SEVERE, "an exception was thrown while converting", e);
                            }
                            // }
                        }

                        //Log.e("second", String.valueOf(I));
                        //editor.putString("FriendPhoto" + String.valueOf(i), Friends.get(i).photo_100);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        if(photoBm!= null)
                            photoBm.compress(Bitmap.CompressFormat.PNG, 100, baos);

                        byte[] b = baos.toByteArray();
                        String encodedPhoto = Base64.encodeToString(b, Base64.DEFAULT);

                        //editor.putString("UserPhotoUrl" + String.valueOf(I), photo_url);

                editor.putString("UserFirstName" + String.valueOf(I), _first_name);
                editor.putString("UserLastName" + String.valueOf(I), _last_name);
                editor.putString("UserVkId" + String.valueOf(I), _vk_id);
                editor.putString("UserPoints" + String.valueOf(I), _points);

                editor.putString("UserPhoto"+String.valueOf(I),encodedPhoto);
                        editor.commit();
                isReady = true;
                I++;
            }});
        //return url[0];
    }

}

