package db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Жамбыл on 19.04.2015.
 */
public class DB_read_by_id extends AsyncTask<String, String, String> {

    Context context;
    private static String url_read_by_id = "http://women.egeshki.ru/blockphonedb/read_by_id.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USERS = "users";
    private static final String TAG_POINTS = "points";


    JSONArray users = null;
    String vk_id;
    String points;

    JSONParser jParser = new JSONParser();

    public DB_read_by_id(String _vk_id, Context _context){
        vk_id = _vk_id;
        context = _context;
        url_read_by_id+="?id="+ vk_id;
        Log.e("DB_read_by_id", "Constructor");
    }
    @Override
    protected String doInBackground(String... strings) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair("id",vk_id));

        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_read_by_id, "GET", params);
        Log.e("DB_read_by_id","doInBackground");
        Log.e("DB_read_by_id: ", url_read_by_id);
        Log.e("DB_read_by_id json ", json.toString());
        // Check your log cat for JSON response
        //Log.e("Users_all: ", json.toString());
        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);
            String message = json.getString(TAG_MESSAGE);
            if(message.equals("No user found")){
                Log.e("DB_read_by_id","No user found");
            }
            else if(message.equals("Required field(s) is missing"))
            {
                Log.e("DB_read_by_id","Required field(s) is missing");
            }

            users = json.getJSONArray(TAG_USERS);
            Log.e("DB_read_by_id","Attempt to get from db");

            if (success == 1) {
                JSONObject c = users.getJSONObject(0);
                points= c.getString(TAG_POINTS);
                Log.e("DB_read_by_id Points",points);
            }
        } catch (JSONException e) {
            Log.e("DB_read_by_id"," error");
            e.printStackTrace();
        }
        catch (Exception e){
            Log.e("DB_read_by_id"," error1");
        }

        return null;
    }
}
