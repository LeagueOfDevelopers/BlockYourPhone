package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.perm.kate.api.Api;

/**
 * Created by Жамбыл on 26.03.2015.
 */
public class App extends Activity {

    Button exitButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app);

        exitButton = (Button)findViewById(R.id.exitButton);
        exitButton.setOnClickListener(exitClick);
    }
    private View.OnClickListener exitClick=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            logOut();
        }
    };

    private void logOut() {
        Vk.api = null;
        Vk.account.access_token=null;
        Vk.account.user_id=0;
        Vk.account.save(App.this);
        //showButtons();
        finish();
    }
}
