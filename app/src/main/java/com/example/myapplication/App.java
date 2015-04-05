package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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

/**
 * Created by Жамбыл on 26.03.2015.
 */
public class App  extends ActionBarActivity {
    //Главная
    String TITLES[] = {"Главная","Рейтинг","Выход"};
    int ICONS[] = {R.drawable.ic_action,R.drawable.ic_raiting,R.drawable.ic_quit};
    TextView functional;
   // LinearLayout layoutFromRecycler;
   // Typeface type_thin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
    //Typeface type_medium = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME = "Buf";
    int POINTS = Account.getPoints();
    Bitmap PROFILE_PHOTO;

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
        //TODO: проверка интеренет конекшна
        getUserData();
        startUI();


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
                            VKSdk.logout();
                            startActivity(new Intent(App.this, MainActivity.class));
                            finish();
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

        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }



        };
        mDrawerToggle.syncState();

    }
    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
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
                editor.putString("AccountFirstName", MainUser.get(0).first_name);
                editor.putString("AccountLastName", MainUser.get(0).last_name);
                editor.putString("AccountPhoto", MainUser.get(0).photo_100);
                editor.putLong("AccountId", MainUser.get(0).id);
                editor.commit();

                Account.setAccountData(App.this);
                NAME = Account.getName();
                PROFILE_PHOTO = Account.getPhoto();

                mAdapter = new MyAdapter(TITLES,ICONS,NAME,POINTS,PROFILE_PHOTO,App.this);
                mRecyclerView.setAdapter(mAdapter);

            }
    });}


    private void startUI()
    {

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Главная");

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);

        //NAME = Account.getName();
        mAdapter = new MyAdapter(TITLES,ICONS,NAME,POINTS,PROFILE_PHOTO,App.this);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayoutMain);
        Drawer.setDrawerListener(mDrawerToggle);

        layoutFromRecycler = (LinearLayout)findViewById(R.id.layoutFromRecycler);

        functional = (TextView)findViewById(R.id.functional);
        Typeface type_thin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        functional.setTypeface(type_thin);
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