package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    String NAME = "Zhambul Ermagambet";
    String EMAIL = "zhambul-96@mail.ru";
    int PROFILE = R.drawable.zhambul;

    private Toolbar toolbar;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;
    LinearLayout layoutFromRecycler;
   // Account account = new Account();
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app);
        startUI();
        if(Account.user_id == 0)
            Account.restore(this);
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
                            MainActivity.api = null;
                            Vk.api = null;
                            Account.access_token=null;
                            Account.user_id=0;
                            Account.save(App.this);

                            intent = new Intent(App.this, MainActivity.class);
                            startActivity(intent);
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

    private void startUI()
    {

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Главная");

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE,App.this);


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
                .setPositiveButton(android.R.string.yes,
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

    /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}