package db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Жамбыл on 19.04.2015.
 */
public final class DB_update_by_id extends AsyncTask<String, String, String> {

    Context context;
    private static String url_update_by_id = "http://women.egeshki.ru/blockphonedb/update_one.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String vk_id;
    String points;



    JSONParser jParser = new JSONParser();

    public DB_update_by_id(Context _context, String _points,
                     String _vk_id ){

        if(!_vk_id.equals(null)|!_points.equals(null)) {
            vk_id = _vk_id;
            points = _points;
            context = _context;
            url_update_by_id += "?id=" + vk_id + "&points=" + points;
            //Log.e("DB_update_by_id",url_update_by_id);
        }
        else
            Log.e("DB_update_by_id","NullPointerException");
    }
    @Override
    protected String doInBackground(String... strings) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_update_by_id, "GET", params);

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);
            String message = json.getString(TAG_MESSAGE);
            if (success == 1) {
                Log.e("DB_update_by_id", "success Points Updated");
            } else if(message.equals("Oops! An error occurred.")){
                Log.e("DB_update_by_id","Acc is already created");
            }
            else if(message.equals("Required field(s) is missing"))
            {
                Log.e("DB_update_by_id","Required field(s) is missing");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            Log.e("DB_update_by_id","Db Error!");
        }

        return null;
    }
}
