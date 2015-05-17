package com.example.blockphone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import db.DB_read_all;

/**
 * Created by Жамбыл on 26.03.2015.
 */

public class App  extends AppActivity  {
    //Главная

    //

    Button MainBlockButton;
    TextView BlockTime, UnlockTime;
    static String  dropdown1value, dropdown2value;
    String[] Seconds = new String[] {"1 секунда","2 секунды","3 секунды","4 секунды"};
    String[] Hours = new String[] {"1 час","2 часа","3 часа","4 часа"};
    Spinner dropdown1, dropdown2;

    /*
        Constructor
     */
    public App(){
        super(R.id.tool_bar, "Блокировка", R.id.RecyclerView, R.id.DrawerLayoutMain, R.id.layoutFromRecycler, 1);
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i("App", "OnCreate");
        setContentView(R.layout.main_app);
        LockScreenService.isMustBeLocked = false;

        if(!Internet.isNetworkConnection(App.this)){
            Log.e("App","Restoring Acc data");
            Account.restore(App.this, false);
        }
        else
            Account.restore(App.this, true);
            getUserData();
        startUI();
        startLocalUI();

    }

    public void startLocalUI(){

        //region dropdown1
        dropdown1 = (Spinner)findViewById(R.id.dropdown1);
        dropdown1.setBackgroundColor(App.this.getResources().getColor(R.color.ColorPrimaryDark));

        dropdown1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                dropdown1value = parent.getItemAtPosition(pos).toString();
                try {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parent.getChildAt(0)).setTextSize(20);
                    ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);
                } catch (Exception e) {
                    Log.e("App", "dropdown1 error");
                }
                //((TextView) parent.getChildAt(0)).setTypeface(type);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Hours);
        dropdown1.setAdapter(adapter1);

        //endregion

        //region dropdown2
        dropdown2 = (Spinner)findViewById(R.id.dropdown2);
        dropdown2.setBackgroundColor(App.this.getResources().getColor(R.color.ColorPrimaryDark));

        dropdown2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                dropdown2value = parent.getItemAtPosition(pos).toString();
                try {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parent.getChildAt(0)).setTextSize(20);
                    ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);
                } catch (Exception e) {
                    Log.e("App", "dropdown2 error ");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Seconds);
        dropdown2.setAdapter(adapter2);
        //endregion


        BlockTime = (TextView)findViewById(R.id.Text1);
        BlockTime.setTypeface(type_thin);

        UnlockTime = (TextView)findViewById(R.id.Text2);
        UnlockTime.setTypeface(type_thin);


        MainBlockButton= (Button)findViewById(R.id.MainBlockButton);
        MainBlockButton.setBackgroundColor(App.this.getResources().getColor(R.color.ColorPrimaryDark));
        MainBlockButton.setTextColor(Color.WHITE);
        MainBlockButton.setText("Заблокировать");
        MainBlockButton.setTransformationMethod(null);
        //MainBlockButton.setTypeface(type);
        MainBlockButton.setOnClickListener(onMainBlockButtonClickListener);


    }

    private View.OnClickListener onMainBlockButtonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
           //THE MAIN FUNCTIONALITY
           Lock();
        }};

    private void Lock(){
        kl.disableKeyguard();
        startService(new Intent(this, LockScreenService.class));
        LockScreenService.isMustBeLocked = true;
        Intent localIntent = new Intent(this, LockScreenActivity.class);
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        startActivity(localIntent);
    }
    @Override
    protected void onResume() {
        reenable();
        reenabled = false;
        Log.i("App","onResume");
        LockScreenService.isMustBeLocked = false;
        super.onResume();
        VKUIHelper.onResume(this);
        setLocalData();

    }
    public static void reenable(){
        if(!reenabled) {
            kl.reenableKeyguard();
            reenabled = true;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserData(){
        Log.i("App", "getting user data");
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,
                "id,first_name,last_name,photo_100,"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                String encodedPhoto = null;

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();

                VKList<VKApiUser> MainUser = (VKList<VKApiUser>) response.parsedModel;
                Log.e("App", "Getting user data");
                String photoUrl = MainUser.get(0).photo_100;
                if (photoUrl != null) {
                    Bitmap photoBm = null;
                    String previousUrl = null;
                    if (Account.getPhotoUrl(App.this) != null)
                        previousUrl = Account.getPhotoUrl(App.this);
                    if (!photoUrl.equals(previousUrl)) {
                        try {
                            photoBm = Internet.convertUrlToImage(photoUrl);


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    if (photoBm != null)
                        photoBm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();
                    encodedPhoto = Base64.encodeToString(b, Base64.DEFAULT);
                    if (!encodedPhoto.equals(null)) {
                        Log.i("App", "getting user data SUCCESS");
                        setLocalData();
                    }
                        } catch (Exception e) {
                            Logger logger = Logger.getAnonymousLogger();
                            logger.log(Level.SEVERE, "an exception was thrown while converting", e);
                        }
                    }
                    //else Log.e("App","same");

                    Account.setAccountData(App.this, MainUser.get(0).first_name, MainUser.get(0).last_name,
                            String.valueOf(MainUser.get(0).id),
                            encodedPhoto, MainUser.get(0).photo_100);
                }
                setLocalData();

                //getting data from db
                new DB_read_all(App.this).start();

            }
        });}

    private void setLocalData(){
        Log.i("App", "Setting Local Data");

        NAME = Account.getFullName();
        PROFILE_PHOTO = Account.getPhotoAsBytes();
        POINTS = Account.getPoints();

        mAdapter = new DrawableAdapter(TITLES,ICONS,NAME,POINTS,PROFILE_PHOTO,App.this);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Выход")
                .setMessage("Вы уверены, что хотите выйти?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton("Да",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                App.super.onBackPressed();
                                Log.i("App", "Exit");
                                finish();
                            }
                        }).create().show();

        }
    }