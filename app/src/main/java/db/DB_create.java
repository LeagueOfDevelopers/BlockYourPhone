package db;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Жамбыл on 19.04.2015.
 */
public class DB_create extends AsyncTask<String, String, String> {

    Context context;
    private static String url_create = "http://women.egeshki.ru/blockphonedb/create_one.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String photo_url;

    String id;
    String first_name;
    String last_name;
    String vk_id;
    String points;
    int I=0;
    boolean isReady = false;


    JSONParser jParser = new JSONParser();

    public DB_create(Context _context, String _first_name, String _last_name,
                     String _vk_id ){
        first_name = _first_name;
        last_name =_last_name;
        vk_id = _vk_id;
        points = "0";
        context = _context;
        url_create+="?first_name="+first_name+"&last_name="+last_name+"&vk_id="+vk_id+"&points="+points;
        Log.e("DB_create","Constructor");
    }
    @Override
      protected String doInBackground(String... strings) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_create, "GET", params);
        Log.e("DB_create","doInBackground");
        Log.e("DB_create: ", url_create);
        Log.e("DB_create: ", json.toString());
        // Check your log cat for JSON reponse
        Log.e("DB_create","Attempt to add to db");
        //Log.e("Users_all: ", json.toString());
        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);
            String message = json.getString(TAG_MESSAGE);
            if (success == 1) {
                Log.e("DB_create","success new acc created");
            } else if(message.equals("Oops! An error occurred.")){
                Log.e("DB_create","Acc is already created");
            }
            else if(message.equals("Required field(s) is missing"))
            {
                Log.e("DB_create","Required field(s) is missing");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
