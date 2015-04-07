package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKUsersArray;

import java.io.ByteArrayOutputStream;

/**
 * Created by Жамбыл on 26.03.2015.
 */
public class App  extends ActionBarActivity {
    //Главная
    String TITLES[] = {"Главная","Рейтинг","Выход"};
    int ICONS[] = {R.drawable.ic_action,R.drawable.ic_raiting,R.drawable.ic_quit};
    Button MainBlockButton;
   // LinearLayout layoutFromRecycler;
   // Typeface type_thin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
    //Typeface type_medium = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME = "Buf";
    int POINTS = Account.getPoints();
    byte [] PROFILE_PHOTO;

    private Toolbar toolbar;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;
    LinearLayout layoutFromRecycler;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app);
        if(!Internet.isNetworkConnection(App.this))
            Internet.Error(App.this);
        getUserData();
        getFriends();
        startUI();
        setLocalData();


    }
    private void startUI(){

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Главная");

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MyAdapter(TITLES,ICONS,NAME,POINTS,PROFILE_PHOTO,App.this);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayoutMain);

        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){
            @Override
            public void onDrawerOpened(View drawerView) {super.onDrawerOpened(drawerView);}
            @Override
            public void onDrawerClosed(View drawerView) {super.onDrawerClosed(drawerView);}
        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //NAME = Account.getName();

        layoutFromRecycler = (LinearLayout)findViewById(R.id.layoutFromRecycler);

        MainBlockButton= (Button)findViewById(R.id.MainBlockButton);
        Typeface type_thin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        MainBlockButton.setBackgroundColor(App.this.getResources().getColor(R.color.ColorPrimaryDark));
        MainBlockButton.setTextColor(Color.WHITE);
        //Toast.makeText(this, String.valueOf(Account.user_id), Toast.LENGTH_LONG).show();
        final GestureDetector mGestureDetector =
                new GestureDetector(App.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());

                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
                    Intent intent = new Intent();
                    Drawer.closeDrawers();
                    switch(recyclerView.getChildPosition(child))
                    {
                        case 1:
                            break;
                        case 2:
                            intent = new Intent(App.this, Top.class);
                            startActivity(intent);
                            break;
                        case 3:
                            new AlertDialog.Builder(App.this)
                                    .setTitle("Выход")
                                    .setMessage("Вы уверены, что хотите выйти из аккаунта?")
                                    .setNegativeButton(android.R.string.no, null)
                                    .setPositiveButton("Да",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    VKSdk.logout();
                                                    startActivity(new Intent(App.this, MainActivity.class));
                                                    finish();
                                                }
                                            }).create().show();
                            break;
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
        setLocalData();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    private void getUserData(){
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,
                "id,first_name,last_name,photo_100,"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList<VKApiUser> MainUser = (VKList<VKApiUser>)response.parsedModel;

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.this);
                SharedPreferences.Editor editor = prefs.edit();

                String photoUrl = MainUser.get(0).photo_100;
                Bitmap photoBm = null;
                if(photoUrl!=null){
                    String previousUrl =  prefs.getString("AccountPhotoUrl", null);
                    if(photoUrl!=previousUrl) {
                        photoBm = Internet.convertUrlToImage(photoUrl);
                    }
                }
                //editor.putString("FriendPhoto" + String.valueOf(i), Friends.get(i).photo_100);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if(photoBm!= null)
                    photoBm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();
                String encodedPhoto = Base64.encodeToString(b, Base64.DEFAULT);
                if(encodedPhoto == null)
                    Toast.makeText(App.this,"Интернет пиздец asd",Toast.LENGTH_LONG).show();

                editor.putString("AccountFirstName", MainUser.get(0).first_name);
                editor.putString("AccountLastName", MainUser.get(0).last_name);
                editor.putString("AccountPhoto", encodedPhoto);
                editor.putString("AccountPhotoUrl",MainUser.get(0).photo_100);
                editor.putLong("AccountId", MainUser.get(0).id);
                editor.commit();

            }
    });}

    public void getFriends(){
        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS,
                "id,first_name,last_name,photo_100,"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(final VKResponse response) {
                super.onComplete(response);
                new Thread(){
                    @Override
                    public void run() {
                        VKList<VKApiUser> Friends = (VKList<VKApiUser>) response.parsedModel;
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.this);
                        SharedPreferences.Editor editor = prefs.edit();

                        for (int i = 0; i < Friends.size(); ++i) {
                            String photoUrl = Friends.get(i).photo_100;
                            Bitmap photoBm = null;
                            if(photoUrl!=null){
                                String previousUrl =  prefs.getString("FriendPhotoUrl" + String.valueOf(i), null);
                                if(photoUrl!=previousUrl) {
                                    photoBm =Internet.convertUrlToImage(photoUrl);
                                }
                            }
                            //editor.putString("FriendPhoto" + String.valueOf(i), Friends.get(i).photo_100);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            if(photoBm!= null)
                            photoBm.compress(Bitmap.CompressFormat.PNG, 100, baos);

                            byte[] b = baos.toByteArray();
                            String encodedPhoto = Base64.encodeToString(b, Base64.DEFAULT);

                            editor.putString("FriendPhoto"+String.valueOf(i),encodedPhoto);
                            editor.putString("FriendFirstName" + String.valueOf(i), Friends.get(i).first_name);
                            editor.putString("FriendLastName" + String.valueOf(i), Friends.get(i).last_name);
                            editor.putString("FriendPhotoUrl" + String.valueOf(i), Friends.get(i).photo_100);
                            editor.putLong("FriendId" + String.valueOf(i), Friends.get(i).id);
                            editor.commit();
                        }
                    }}.start();
            }
        });
    }
    private void setLocalData(){
        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(App.this);
        //Account.setAccountData(App.this);
        //NAME = Account.getName();
        if(prefs1!= null) {
            NAME = prefs1.getString("AccountFirstName", null) + " "+  prefs1.getString("AccountLastName", null);
            String PhotoEncoded = prefs1.getString("AccountPhoto", null);
            if(PhotoEncoded != null) {
                byte[] asd = PhotoEncoded.getBytes();
                PROFILE_PHOTO = Base64.decode(asd, Base64.DEFAULT);
            }
        }
        //PROFILE_PHOTO = Account.getPhotoAsBytes();

        mAdapter = new MyAdapter(TITLES,ICONS,NAME,POINTS,PROFILE_PHOTO,App.this);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onBackPressed() {
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
                                finish();
                            }
                        }).create().show();
    }
}