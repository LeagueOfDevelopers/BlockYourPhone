package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.perm.kate.api.Api;

/**
 * Created by Жамбыл on 26.03.2015.
 */
public class App extends ActionBarActivity {

    Button exitButton;
    private Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;

    String TITLES[] = {"Me","Find"};
    int ICONS[] = {R.drawable.ic_action,R.drawable.ic_event};

    String NAME = "Zhambul Ermagambet";
    String EMAIL = "zhambul-96@mail.ru";
    int PROFILE = R.drawable.zhambul;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app);
        setupUI();


        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

    };
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
    }

    private View.OnClickListener exitClick=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            logOut();
        }
    };

    private void setupUI(){
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);

        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        exitButton = (Button)findViewById(R.id.exitButton);
        exitButton.setOnClickListener(exitClick);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
    }

    private void logOut() {
        Vk.api = null;
        Vk.account.access_token=null;
        Vk.account.user_id=0;
        Vk.account.save(App.this);
        //showButtons();
        finish();
    }
}
